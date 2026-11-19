package ma.emsi.app;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
@RestController @RequestMapping("/accounts") @CrossOrigin(origins="http://localhost:4200")
public class AccountController {
 private final AccountService s;public AccountController(AccountService s){this.s=s;}
 public record Amount(@NotNull @DecimalMin("0.01") @Digits(integer=16,fraction=2) BigDecimal amount) {}
 public record Transfer(@NotBlank String from,@NotBlank String to,@NotNull @DecimalMin("0.01") @Digits(integer=16,fraction=2) BigDecimal amount) {}
 @GetMapping public List<BankAccount> all(@RequestParam(required=false) Long customerId){return customerId==null?s.all():s.byCustomer(customerId);}
 @GetMapping("/{id}") public AccountService.AccountDetails get(@PathVariable String id){return s.details(id);}
 @PostMapping @ResponseStatus(HttpStatus.CREATED) public BankAccount create(@Valid @RequestBody AccountRequest r){return s.create(r);}
 @PostMapping("/{id}/credit") public BankAccount credit(@PathVariable String id,@Valid @RequestBody Amount r){return s.change(id,r.amount(),true);}
 @PostMapping("/{id}/debit") public BankAccount debit(@PathVariable String id,@Valid @RequestBody Amount r){return s.change(id,r.amount(),false);}
 @GetMapping("/{id}/operations") public List<AccountOperation> history(@PathVariable String id){return s.history(id);}
 @PostMapping("/transfer") @ResponseStatus(HttpStatus.NO_CONTENT) public void transfer(@Valid @RequestBody Transfer t){s.transfer(t.from(),t.to(),t.amount());}
}
