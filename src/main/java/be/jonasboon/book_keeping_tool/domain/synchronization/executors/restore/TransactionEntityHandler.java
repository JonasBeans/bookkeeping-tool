package be.jonasboon.book_keeping_tool.domain.synchronization.executors.restore;

import be.jonasboon.book_keeping_tool.domain.cost_centers.entity.CostCenter;
import be.jonasboon.book_keeping_tool.domain.cost_centers.repository.CostCenterRepository;
import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TransactionEntityHandler implements EntityHandler {

    private final CostCenterRepository costCenterRepository;

    @Override
    public <I> I handle(I entity) {
        if (entity instanceof Transaction transaction) {
            if (transaction.getCostCenter() != null && transaction.getCostCenter().getCostCenter() != null) {
                CostCenter managedCostCenter = costCenterRepository.findById(transaction.getCostCenter().getCostCenter()).orElse(null);
                transaction.setCostCenter(managedCostCenter);
            }
            // Optionally, set id and version to null if you want to insert as new
            transaction.setId(null);
            transaction.setVersion(null);
        }
        return entity;
    }
}

