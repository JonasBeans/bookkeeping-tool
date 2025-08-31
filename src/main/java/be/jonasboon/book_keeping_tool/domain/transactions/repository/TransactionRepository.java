package be.jonasboon.book_keeping_tool.domain.transactions.repository;

import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
