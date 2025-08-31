package be.jonasboon.book_keeping_tool.domain.cost_centers.service;

import be.jonasboon.book_keeping_tool.domain.cost_centers.DTO.AccumulatedAmountsDTO;
import be.jonasboon.book_keeping_tool.domain.cost_centers.DTO.AddCostCenterDTO;
import be.jonasboon.book_keeping_tool.domain.cost_centers.DTO.CostCenterDTO;
import be.jonasboon.book_keeping_tool.domain.cost_centers.entity.CostCenter;
import be.jonasboon.book_keeping_tool.domain.cost_centers.mapper.CostCenterMapper;
import be.jonasboon.book_keeping_tool.domain.cost_centers.model.AccumulatedAmounts;
import be.jonasboon.book_keeping_tool.domain.cost_centers.repository.CostCenterRepository;
import be.jonasboon.book_keeping_tool.domain.transactions.DTO.TransactionDTO;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
@AllArgsConstructor
public class CostCenterService {

    private final String SKIP_COST_CENTER = "Skip";
    private final CostCenterRepository costCenterRepository;
    private final EntityManager entityManager;

    public List<CostCenterDTO> getAllCostCenters() {
        return costCenterRepository.findAll().stream()
                .map(CostCenterMapper::of).toList();
    }

    public void resetAllTotalAmounts() {
        costCenterRepository.resetTotalAllAmounts();
        entityManager.flush();
    }

    public void updateTotalAmounts(List<TransactionDTO> transactions) {
        this.resetAllTotalAmounts();

        transactions.forEach(transaction -> {
            Optional<CostCenter> foundEntity = costCenterRepository.findById(transaction.getCostCenterReference());
            foundEntity.ifPresent(costCenterEntity -> {
                costCenterEntity.addToTotalAmount(transaction.getAmount().abs());
                costCenterRepository.save(costCenterEntity);
            });
        });
    }

    public AccumulatedAmountsDTO getAccumulatedAmounts() {
        AccumulatedAmounts totalAmounts = new AccumulatedAmounts();
        costCenterRepository.findByIsCost(true).stream().map(CostCenter::getTotalAmount).forEach(totalAmounts::addCost);
        costCenterRepository.findByIsCost(false).stream()
                .filter(income -> !SKIP_COST_CENTER.equals(income.getCostCenter()))
                .map(CostCenter::getTotalAmount)
                .forEach(totalAmounts::addIncome);
        return new AccumulatedAmountsDTO(totalAmounts.getTotalIncome(), totalAmounts.getTotalCost());
    }

    public void addCostCenter(AddCostCenterDTO costCenterDTO) throws CostCenterException {
        costCenterRepository.findByCostCenter(costCenterDTO.costCenter()).ifPresent(result -> CostCenterException.throwAlreadyExists(result.getCostCenter()));
        costCenterRepository.save(CostCenterMapper.of(costCenterDTO));
    }

    public static class CostCenterException extends RuntimeException {
        public CostCenterException(String message) {
            super(message);
        }

        public static void throwAlreadyExists(String costCenter) {
            throw new CostCenterException("Cost center already exists: " + costCenter);
        }

    }
}