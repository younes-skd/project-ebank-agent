package ma.emsi.app;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.math.BigDecimal;
import java.util.List;
@Service @Transactional
public class AccountService {
 private final AccountRepository repo;private final OperationRepository operations;private final CustomerLookup customers;
 public AccountService(AccountRepository r,OperationRepository o,CustomerLookup c){repo=r;operations=o;customers=c;}
 public List<BankAccount> all(){return repo.findAll();} public List<BankAccount> byCustomer(Long id){return repo.findByCustomerId(id);}
 public BankAccount get(String id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found"));}
 public AccountDetails details(String id){var a=get(id);return new AccountDetails(a,customers.read(a.getCustomerId()));}
 public BankAccount create(AccountRequest r){customers.require(r.customerId());return demoCreate(r);}
 BankAccount demoCreate(AccountRequest r){BankAccount a=new BankAccount();a.setCustomerId(r.customerId());a.setBalance(r.balance());a.setCurrency(r.currency());a.setType(r.type());return repo.save(a);}
 public BankAccount change(String id,BigDecimal amount,boolean credit){
   if(amount==null || amount.signum()<=0 || amount.scale()>2)throw new IllegalArgumentException("Amount must be positive, with at most two decimals");
   BankAccount a=repo.locked(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Account not found"));
   if(!credit && a.getBalance().compareTo(amount)<0)throw new IllegalArgumentException("Insufficient balance");
   a.setBalance(credit?a.getBalance().add(amount):a.getBalance().subtract(amount));
   operations.save(new AccountOperation(id,credit?"CREDIT":"DEBIT",amount));return repo.save(a);
 }
 public void transfer(String from,String to,BigDecimal amount){
   if(from.equals(to))throw new IllegalArgumentException("Accounts must differ");
   // Acquire account locks in a consistent order to avoid opposite-transfer deadlocks.
   String first=from.compareTo(to)<0?from:to;String second=from.compareTo(to)<0?to:from;
   var a=repo.locked(first).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
   var b=repo.locked(second).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND));
   if(!a.getCurrency().equals(b.getCurrency()))throw new IllegalArgumentException("Currencies must match");
   change(from,amount,false);change(to,amount,true);
 }
 public List<AccountOperation> history(String id){get(id);return operations.findByAccountIdOrderByTimestampDesc(id);}
 public record AccountDetails(BankAccount account,CustomerLookup.CustomerStatus customerStatus) {}
}
