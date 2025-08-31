package be.jonasboon.book_keeping_tool.domain.balance_posts.repository;

import be.jonasboon.book_keeping_tool.domain.balance_posts.entity.BalancePost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceRepository extends JpaRepository<BalancePost, Long> {

    Optional<BalancePost> findByTitle(String title);

}
