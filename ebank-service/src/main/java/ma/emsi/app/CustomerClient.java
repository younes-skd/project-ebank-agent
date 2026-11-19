package ma.emsi.app;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;
@FeignClient(name="customer-service",url="${clients.customer.url:}")
public interface CustomerClient {
 @GetMapping("/customers/{id}") Customer get(@PathVariable Long id);
 record Customer(Long id,String name,String email) {}
}
