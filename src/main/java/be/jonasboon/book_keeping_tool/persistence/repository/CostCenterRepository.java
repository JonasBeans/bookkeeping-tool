package be.jonasboon.book_keeping_tool.persistence.repository;

import be.jonasboon.book_keeping_tool.persistence.entity.CostCenterEntity;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface CostCenterRepository extends MongoRepository<CostCenterEntity, String> {

    List<CostCenterEntity> findByIsCost(Boolean isCost);
    Optional<CostCenterEntity> findByCostCenter(String costCenter);

}
