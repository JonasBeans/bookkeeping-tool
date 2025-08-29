package be.jonasboon.book_keeping_tool.persistence.repository;

import be.jonasboon.book_keeping_tool.persistence.entity.CostCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CostCenterCustomRepository extends JpaRepository<CostCenter, Long> {

    @Query("UPDATE CostCenter c SET c.totalAmount = 0")
    void resetTotalAllAmounts();
}
