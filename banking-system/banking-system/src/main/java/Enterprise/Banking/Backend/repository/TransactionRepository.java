package Enterprise.Banking.Backend.repository;

import Enterprise.Banking.Backend.entity.Transaction;
import Enterprise.Banking.Backend.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    Page<Transaction> findByAccountOrderByTransactionTimeDesc(
            Account account,
            Pageable pageable
    );
}
