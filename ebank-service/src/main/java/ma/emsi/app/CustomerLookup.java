package ma.emsi.app;

import org.springframework.stereotype.Service;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
@Service
public class CustomerLookup {
 private final CustomerClient client; private final CircuitBreakerFactory<?,?> breakers;
 public CustomerLookup(CustomerClient c,CircuitBreakerFactory<?,?> b){client=c;breakers=b;}
 public CustomerStatus read(Long id){
  return breakers.create("customer").run(()->new CustomerStatus(client.get(id),true),error->new CustomerStatus(new CustomerClient.Customer(id,"Indisponible",null),false));
 }
 // Creation must verify customer existence; the read fallback is never used to authorize creation.
 public void require(Long id){client.get(id);}
 public record CustomerStatus(CustomerClient.Customer customer,boolean available) {}
}
