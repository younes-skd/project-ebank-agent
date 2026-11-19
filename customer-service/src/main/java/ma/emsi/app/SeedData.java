package ma.emsi.app;

import org.springframework.context.annotation.*;
import org.springframework.boot.CommandLineRunner;
@Configuration
public class SeedData { @Bean CommandLineRunner seed(CustomerService s){return args->{s.create(new CustomerRequest("Client Démo","demo@example.com"));s.create(new CustomerRequest("Client Test","test@example.com"));};} }
