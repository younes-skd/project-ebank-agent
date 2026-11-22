package ma.emsi.app;

import jakarta.persistence.*;
import java.time.Instant;
import java.math.BigDecimal;
@Entity
public class AccountOperation {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
 private String accountId; private Instant timestamp=Instant.now(); private String type;
 @Column(precision=19,scale=2) private BigDecimal amount;
 public AccountOperation(){} public AccountOperation(String a,String t,BigDecimal v){accountId=a;type=t;amount=v;}
 public Long getId(){return id;} public String getAccountId(){return accountId;} public Instant getTimestamp(){return timestamp;}
 public String getType(){return type;} public BigDecimal getAmount(){return amount;}
}
