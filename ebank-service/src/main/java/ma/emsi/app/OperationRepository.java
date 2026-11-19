package ma.emsi.app;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface OperationRepository extends JpaRepository<AccountOperation,Long> { List<AccountOperation> findByAccountIdOrderByTimestampDesc(String id); }
