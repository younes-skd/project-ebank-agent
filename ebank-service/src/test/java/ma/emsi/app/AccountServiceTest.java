package ma.emsi.app;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import java.math.BigDecimal;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;
@SpringBootTest(properties={"spring.cloud.config.enabled=false","eureka.client.enabled=false"})
class AccountServiceTest {
 @Autowired AccountService service; @Autowired CustomerLookup lookup; @MockitoBean CustomerClient customer;
 @Test void transferIsAtomicAndRecordsHistory(){
  var a=service.demoCreate(new AccountRequest(1L,new BigDecimal("100"),"MAD",BankAccount.AccountType.CURRENT));var b=service.demoCreate(new AccountRequest(1L,new BigDecimal("50"),"MAD",BankAccount.AccountType.CURRENT));
  service.transfer(a.getId(),b.getId(),new BigDecimal("25"));
  assertThat(service.get(a.getId()).getBalance()).isEqualByComparingTo("75");assertThat(service.get(b.getId()).getBalance()).isEqualByComparingTo("75");
  assertThat(service.history(a.getId())).hasSize(1);
  assertThatThrownBy(()->service.transfer(a.getId(),b.getId(),new BigDecimal("1000"))).isInstanceOf(IllegalArgumentException.class);
  assertThat(service.get(a.getId()).getBalance()).isEqualByComparingTo("75");assertThat(service.get(b.getId()).getBalance()).isEqualByComparingTo("75");
 }
 @Test void fallbackIsExplicitAndCreationFailsClosed(){
  when(customer.get(999L)).thenThrow(new RuntimeException("Offline"));
  assertThat(lookup.read(999L).available()).isFalse();
  assertThatThrownBy(()->service.create(new AccountRequest(999L,BigDecimal.ZERO,"MAD",BankAccount.AccountType.CURRENT))).isInstanceOf(RuntimeException.class);
 }
 @Test void rejectsNegativeAndCrossCurrencyChanges(){
  var a=service.demoCreate(new AccountRequest(1L,BigDecimal.TEN,"MAD",BankAccount.AccountType.CURRENT));var b=service.demoCreate(new AccountRequest(1L,BigDecimal.TEN,"EUR",BankAccount.AccountType.CURRENT));
  assertThatThrownBy(()->service.change(a.getId(),BigDecimal.ZERO,true)).isInstanceOf(IllegalArgumentException.class);
  assertThatThrownBy(()->service.transfer(a.getId(),b.getId(),BigDecimal.ONE)).isInstanceOf(IllegalArgumentException.class);
 }
}
