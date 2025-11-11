package be.jonasboon.book_keeping_tool.domain.cost_centers.repository;

import be.jonasboon.book_keeping_tool.domain.cost_centers.entity.CostCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
public interface CostCenterRepository extends JpaRepository<CostCenter, String> {

    List<CostCenter> findByIsCost(Boolean isCost);
    Optional<CostCenter> findByCostCenter(String costCenter);

    // Bulk update query resetting all total amounts. Clear persistence context to avoid stale entities.
    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Transactional
    @Query(value = "UPDATE public.cost_centers SET total_amount = 0", nativeQuery = true)
    int resetTotalAllAmounts();

}
