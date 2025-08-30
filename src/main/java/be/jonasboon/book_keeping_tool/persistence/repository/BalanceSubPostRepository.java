package be.jonasboon.book_keeping_tool.persistence.repository;

import be.jonasboon.book_keeping_tool.persistence.entity.BalanceSubPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BalanceSubPostRepository extends JpaRepository<BalanceSubPost, String> {
}
