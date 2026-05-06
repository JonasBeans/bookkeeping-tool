package be.jonasboon.book_keeping_tool.domain.transactions.repository;

import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    List<Transaction> findByBookDateGreaterThanEqualAndBookDateLessThan(LocalDate startInclusive, LocalDate endExclusive);

    void deleteByBookDateGreaterThanEqualAndBookDateLessThan(LocalDate startInclusive, LocalDate endExclusive);

    @Query("select transaction.transactionHash from Transaction transaction where transaction.transactionHash in :transactionHashes")
    Set<String> findExistingTransactionHashes(@Param("transactionHashes") Set<String> transactionHashes);
}
