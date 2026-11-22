package ma.emsi.app;

import org.springframework.stereotype.Component;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import java.util.List;
@Component
public class BankTools {
 private final AccountService service;public BankTools(AccountService s){service=s;}
 @Tool(description="List all fictional demonstration bank accounts and balances") public List<BankAccount> listAccounts(){return service.all();}
 @Tool(description="Get a fictional bank account by ID with customer information") public AccountService.AccountDetails accountDetails(String id){return service.details(id);}
 @Tool(description="List operation history for a fictional bank account") public List<AccountOperation> accountOperations(String id){return service.history(id);}
 @Tool(description="List fictional accounts for a customer ID") public List<BankAccount> customerAccounts(Long customerId){return service.byCustomer(customerId);}
 @Bean ToolCallbackProvider bankToolProvider(){return MethodToolCallbackProvider.builder().toolObjects(this).build();}
}
