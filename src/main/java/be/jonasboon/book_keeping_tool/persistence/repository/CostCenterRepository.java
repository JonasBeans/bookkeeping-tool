package be.jonasboon.book_keeping_tool.persistence.repository;

import be.jonasboon.book_keeping_tool.persistence.entity.CostCenter;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CostCenterRepository extends JpaRepository<CostCenter, String> {

    List<CostCenter> findByIsCost(Boolean isCost);
    Optional<CostCenter> findByCostCenter(String costCenter);

}
