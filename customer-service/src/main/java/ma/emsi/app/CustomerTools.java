package ma.emsi.app;

import org.springframework.stereotype.Component;
        import org.springframework.ai.tool.annotation.Tool;
        import org.springframework.ai.tool.ToolCallbackProvider;
        import org.springframework.ai.tool.method.MethodToolCallbackProvider;
        import org.springframework.context.annotation.Bean;
        import java.util.List;
        @Component
        public class CustomerTools {
          private final CustomerService service;
          public CustomerTools(CustomerService s){service=s;}
          @Tool(description="List fictional demo customers") public List<Customer> listCustomers(){return service.all();}
          @Tool(description="Read one fictional demo customer by ID") public Customer getCustomer(Long id){return service.get(id);}
          @Bean ToolCallbackProvider customerToolProvider(){return MethodToolCallbackProvider.builder().toolObjects(this).build();}
        }
