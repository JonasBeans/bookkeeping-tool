package be.jonasboon.book_keeping_tool.service.cost_center;

import be.jonasboon.book_keeping_tool.DTO.AccumulatedAmountsDTO;
import be.jonasboon.book_keeping_tool.DTO.CostCenterDTO;
import be.jonasboon.book_keeping_tool.mapper.CostCenterMapper;
import be.jonasboon.book_keeping_tool.model.AccumulatedAmounts;
import be.jonasboon.book_keeping_tool.model.TransactionDTO;
import be.jonasboon.book_keeping_tool.persistence.entity.CostCenterEntity;
import be.jonasboon.book_keeping_tool.persistence.repository.CostCenterCustomRepository;
import be.jonasboon.book_keeping_tool.persistence.repository.CostCenterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class CostCenterService {

    private final CostCenterRepository costCenterRepository;
    private final CostCenterCustomRepository costCenterCustomRepository;

    public List<CostCenterDTO> getAllCostCenters() {
        return costCenterRepository.findAll().stream().map(CostCenterMapper::fromEntity).toList();
    }

    public void updateTotalAmounts(List<TransactionDTO> transactions) {
        costCenterCustomRepository.resetTotalAllAmounts();

        transactions.forEach(transaction -> {
            Optional<CostCenterEntity> foundEntity = costCenterRepository.findById(transaction.getCostCenterId());
            foundEntity.ifPresent(costCenterEntity -> {
                costCenterEntity.addToTotalAmount(transaction.getAmount().abs());
                costCenterRepository.save(costCenterEntity);
            });
        });
    }

    public AccumulatedAmountsDTO getAccumulatedAmounts() {
        AccumulatedAmounts totalAmounts = new AccumulatedAmounts();
        costCenterRepository.findByIsCost(true).stream().map(CostCenterEntity::getTotalAmount).forEach(totalAmounts::addCost);
        costCenterRepository.findByIsCost(false).stream().map(CostCenterEntity::getTotalAmount).forEach(totalAmounts::addIncome);
        return new AccumulatedAmountsDTO(totalAmounts.getTotalIncome(), totalAmounts.getTotalCost());
    }
}