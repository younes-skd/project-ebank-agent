package ma.emsi.app;

import org.springframework.context.annotation.*;
import org.springframework.boot.CommandLineRunner;
import java.math.BigDecimal;
@Configuration
public class DemoData {
 @Bean CommandLineRunner demo(AccountService s){return args->{s.demoCreate(new AccountRequest(1L,new BigDecimal("2500"),"MAD",BankAccount.AccountType.CURRENT));s.demoCreate(new AccountRequest(2L,new BigDecimal("7000"),"MAD",BankAccount.AccountType.SAVING));};}
}
