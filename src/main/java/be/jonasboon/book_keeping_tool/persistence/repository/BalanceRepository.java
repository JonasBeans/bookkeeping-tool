package be.jonasboon.book_keeping_tool.persistence.repository;

import be.jonasboon.book_keeping_tool.persistence.entity.BalancePostEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BalanceRepository extends MongoRepository<BalancePostEntity, String> {

    Optional<BalancePostEntity> findByTitle(String title);

}
