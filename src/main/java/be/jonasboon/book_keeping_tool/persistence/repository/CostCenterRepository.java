package be.jonasboon.book_keeping_tool.persistence.repository;

import be.jonasboon.book_keeping_tool.persistence.entity.CostCenterEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface CostCenterRepository extends MongoRepository<CostCenterEntity, Long> {

    Optional<CostCenterEntity> findByIndex(Integer index);
}
