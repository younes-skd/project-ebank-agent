package ma.emsi.app;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;
public record AccountRequest(@NotNull @Positive Long customerId,@NotNull @DecimalMin("0") BigDecimal balance,@NotBlank @Pattern(regexp="[A-Z]{3}") String currency,@NotNull BankAccount.AccountType type) {}
