package ma.emsi.app;

import org.springframework.web.bind.annotation.*;
    import org.springframework.http.HttpStatus;
    import jakarta.validation.Valid;
    import java.util.List;
    @RestController @RequestMapping("/customers") @CrossOrigin(origins="http://localhost:4200")
    public class CustomerController {
      private final CustomerService service;
      public CustomerController(CustomerService s){service=s;}
      @GetMapping public List<Customer> all(){return service.all();}
      @GetMapping("/{id}") public Customer get(@PathVariable Long id){return service.get(id);}
      @PostMapping @ResponseStatus(HttpStatus.CREATED) public Customer create(@Valid @RequestBody CustomerRequest r){return service.create(r);}
      @PutMapping("/{id}") public Customer update(@PathVariable Long id,@Valid @RequestBody CustomerRequest r){return service.update(id,r);}
      @DeleteMapping("/{id}") @ResponseStatus(HttpStatus.NO_CONTENT) public void delete(@PathVariable Long id){service.delete(id);}

    }
