package be.jonasboon.book_keeping_tool.domain.transactions.mapper;

import be.jonasboon.book_keeping_tool.domain.cost_centers.entity.CostCenter;
import be.jonasboon.book_keeping_tool.domain.cost_centers.repository.CostCenterRepository;
import be.jonasboon.book_keeping_tool.domain.transactions.DTO.TransactionCSVObject;
import be.jonasboon.book_keeping_tool.domain.transactions.DTO.TransactionDTO;
import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;
import be.jonasboon.book_keeping_tool.utils.mapper.CSVObject;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import static io.micrometer.common.util.StringUtils.isNotBlank;

@Component
@AllArgsConstructor
public class TransactionMapper {

    private CostCenterRepository costCenterRepository;

    public TransactionDTO from(Transaction entity) {
        return TransactionDTO.builder()
                .withId(entity.getId())
                .withAmount(entity.getAmount())
                .withBookDate(entity.getBookDate())
                .withBookYear(entity.getBookDate().getYear())
                .withTransactionDate(entity.getTransactionDate())
                .withDescription(entity.getDescription())
                .withNameOtherParty(entity.getNameOtherParty())
                .withMessage(entity.getMessage())
                .withCostCenterReference(entity.getCostCenter().getCostCenter())
                .withVersion(entity.getVersion())
                .build();
    }

    public Transaction from(TransactionDTO dto) {
        return Transaction.builder()
                .withId(dto.getId())
                .withAmount(dto.getAmount())
                .withBookDate(dto.getBookDate())
                .withTransactionDate(dto.getTransactionDate())
                .withDescription(dto.getDescription())
                .withNameOtherParty(dto.getNameOtherParty())
                .withMessage(dto.getMessage())
                .withCostCenter(getCostCenter(dto))
                .withVersion(dto.getVersion())
                .build();
    }

    private CostCenter getCostCenter(TransactionDTO dto) {
        CostCenter costCenter = null;

        if (isNotBlank( dto.getCostCenterReference())) {
             costCenter = costCenterRepository.findById(dto.getCostCenterReference())
                    .orElseThrow(() -> new IllegalArgumentException("CostCenter not found with id: " + dto.getCostCenterReference()));
        }
        return costCenter;
    }

    public Transaction map(CSVObject restoreModel) {
        if (restoreModel instanceof TransactionCSVObject dto) {
            return Transaction.builder()
                    .withAmount(dto.getAmount())
                    .withBookDate(dto.getBookDate())
                    .withTransactionDate(dto.getTransactionDate())
                    .withDescription(dto.getDescription())
                    .withNameOtherParty(dto.getNameOtherParty())
                    .withMessage(dto.getMessage())
                    .build();
        }
        throw new ClassCastException();
    }
}
