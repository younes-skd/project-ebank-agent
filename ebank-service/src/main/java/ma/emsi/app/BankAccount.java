package ma.emsi.app;

import jakarta.persistence.*;
import java.math.BigDecimal;
@Entity
public class BankAccount {
 @Id @GeneratedValue(strategy=GenerationType.UUID) private String id;
 @Version private Long version;
 private Long customerId;
 @Column(precision=19,scale=2,nullable=false) private BigDecimal balance;
 private String currency;
 @Enumerated(EnumType.STRING) private AccountType type;
 public enum AccountType { CURRENT,SAVING }
 public BankAccount(){}
 public String getId(){return id;} public Long getCustomerId(){return customerId;} public void setCustomerId(Long v){customerId=v;}
 public BigDecimal getBalance(){return balance;} public void setBalance(BigDecimal v){balance=v;}
 public String getCurrency(){return currency;} public void setCurrency(String v){currency=v;}
 public AccountType getType(){return type;} public void setType(AccountType v){type=v;}
 public Long getVersion(){return version;}
}
