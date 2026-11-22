package ma.emsi.app;

import org.springframework.data.jpa.repository.*;
import jakarta.persistence.LockModeType;
import java.util.*;
public interface AccountRepository extends JpaRepository<BankAccount,String> {
 List<BankAccount> findByCustomerId(Long id);
 @Lock(LockModeType.PESSIMISTIC_WRITE) @Query("select a from BankAccount a where a.id=:id") Optional<BankAccount> locked(String id);
}
