package ma.emsi.app;

import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;
    import org.springframework.web.server.ResponseStatusException;
    import org.springframework.http.HttpStatus;
    import java.util.List;
    @Service @Transactional
    public class CustomerService {
      private final CustomerRepository repo;
      public CustomerService(CustomerRepository r){repo=r;}
      public List<Customer> all(){return repo.findAll();}
      public Customer get(Long id){return repo.findById(id).orElseThrow(()->new ResponseStatusException(HttpStatus.NOT_FOUND,"Customer not found"));}
      public Customer create(CustomerRequest r){return apply(new Customer(),r);}
      public Customer update(Long id,CustomerRequest r){return apply(get(id),r);}
      private Customer apply(Customer e,CustomerRequest r){e.setName(r.name()); e.setEmail(r.email()); return repo.save(e);}
      public void delete(Long id){repo.delete(get(id));}

    }
