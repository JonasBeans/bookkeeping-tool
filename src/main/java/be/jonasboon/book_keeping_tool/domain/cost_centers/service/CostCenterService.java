package be.jonasboon.book_keeping_tool.domain.cost_centers.service;

import be.jonasboon.book_keeping_tool.domain.cost_centers.DTO.AccumulatedAmountsDTO;
import be.jonasboon.book_keeping_tool.domain.cost_centers.DTO.AddCostCenterDTO;
import be.jonasboon.book_keeping_tool.domain.cost_centers.DTO.CostCenterDTO;
import be.jonasboon.book_keeping_tool.domain.cost_centers.entity.CostCenter;
import be.jonasboon.book_keeping_tool.domain.cost_centers.mapper.CostCenterMapper;
import be.jonasboon.book_keeping_tool.domain.cost_centers.model.AccumulatedAmounts;
import be.jonasboon.book_keeping_tool.domain.cost_centers.repository.CostCenterRepository;
import be.jonasboon.book_keeping_tool.domain.transactions.DTO.TransactionDTO;
import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;
import be.jonasboon.book_keeping_tool.domain.transactions.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static be.jonasboon.book_keeping_tool.utils.BookYearUtils.endExclusiveOf;
import static be.jonasboon.book_keeping_tool.utils.BookYearUtils.startOf;
import static io.micrometer.common.util.StringUtils.isNotBlank;

@Service
@Transactional
@AllArgsConstructor
public class CostCenterService {

    private final String SKIP_COST_CENTER = "Skip";
    private final CostCenterRepository costCenterRepository;
    private final TransactionRepository transactionRepository;
    private final EntityManager entityManager;

    public List<CostCenterDTO> getAllCostCenters() {
        return getAllCostCenters(null);
    }

    public List<CostCenterDTO> getAllCostCenters(Integer bookYear) {
        Map<String, BigDecimal> totalsByCostCenter = getTransactions(bookYear).stream()
                .filter(transaction -> isNotBlank(transaction.getCostCenter().getCostCenter()))
                .collect(Collectors.groupingBy(
                        transaction -> transaction.getCostCenter().getCostCenter(),
                        Collectors.reducing(
                                BigDecimal.ZERO,
                                transaction -> transaction.getAmount() == null ? BigDecimal.ZERO : transaction.getAmount().abs(),
                                BigDecimal::add
                        )
                ));

        return costCenterRepository.findAll().stream()
                .map(CostCenterMapper::of)
                .peek(costCenter -> costCenter.setTotalAmount(totalsByCostCenter.getOrDefault(costCenter.getCostCenter(), BigDecimal.ZERO)))
                .toList();
    }

    public void resetAllTotalAmounts() {
        int affected = costCenterRepository.resetTotalAllAmounts();
        // Clear persistence context so subsequent entity loads see updated values from DB
        entityManager.clear();
        // Optionally could log affected count
    }

    public void updateTotalAmounts(List<TransactionDTO> transactions) {
        this.resetAllTotalAmounts();

        transactions.stream()
                .filter(transaction -> isNotBlank(transaction.getCostCenterReference()))
                .forEach(transaction -> {
                    Optional<CostCenter> foundEntity = costCenterRepository.findById(transaction.getCostCenterReference());
                    foundEntity.ifPresent(costCenterEntity -> {
                        costCenterEntity.addToTotalAmount(transaction.getAmount().abs());
                        costCenterRepository.save(costCenterEntity);
                    });
                });
    }

    public AccumulatedAmountsDTO getAccumulatedAmounts() {
        return getAccumulatedAmounts(null);
    }

    public AccumulatedAmountsDTO getAccumulatedAmounts(Integer bookYear) {
        AccumulatedAmounts totalAmounts = new AccumulatedAmounts();
        List<CostCenterDTO> costCenters = getAllCostCenters(bookYear);
        costCenters.stream()
                .filter(CostCenterDTO::getIsCost)
                .map(CostCenterDTO::getTotalAmount)
                .forEach(totalAmounts::addCost);
        costCenters.stream()
                .filter(costCenter -> !costCenter.getIsCost())
                .filter(income -> !SKIP_COST_CENTER.equals(income.getCostCenter()))
                .map(CostCenterDTO::getTotalAmount)
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

    private List<Transaction> getTransactions(Integer bookYear) {
        if (bookYear == null) {
            return transactionRepository.findAll();
        }
        return transactionRepository.findByBookDateGreaterThanEqualAndBookDateLessThan(startOf(bookYear), endExclusiveOf(bookYear));
    }
}
