package be.jonasboon.book_keeping_tool.domain.transactions.mapper;

import be.jonasboon.book_keeping_tool.domain.cost_centers.entity.CostCenter;
import be.jonasboon.book_keeping_tool.domain.cost_centers.repository.CostCenterRepository;
import be.jonasboon.book_keeping_tool.domain.transactions.DTO.TransactionCSVObject;
import be.jonasboon.book_keeping_tool.domain.transactions.DTO.TransactionDTO;
import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;
import be.jonasboon.book_keeping_tool.utils.mapper.CSVObject;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TransactionMapper {

    private CostCenterRepository costCenterRepository;

    public TransactionDTO from(Transaction entity) {
        return TransactionDTO.builder()
                .withId(entity.getId())
                .withAmount(entity.getAmount())
                .withBookDate(entity.getBookDate())
                .withTransactionDate(entity.getTransactionDate())
                .withNameOtherParty(entity.getNameOtherParty())
                .withCostCenterReference(entity.getCostCenter().getCostCenter())
                .withVersion(entity.getVersion())
                .build();
    }

    public Transaction from(TransactionDTO dto) {
        CostCenter costCenter = costCenterRepository.findById(dto.getCostCenterReference())
                .orElseThrow(() -> new IllegalArgumentException("CostCenter not found with id: " + dto.getCostCenterReference()));

        return Transaction.builder()
                .withId(dto.getId())
                .withAmount(dto.getAmount())
                .withBookDate(dto.getBookDate())
                .withTransactionDate(dto.getTransactionDate())
                .withNameOtherParty(dto.getNameOtherParty())
                .withCostCenter(costCenter)
                .withVersion(dto.getVersion())
                .build();
    }

    public Transaction map(CSVObject restoreModel) {
        if (restoreModel instanceof TransactionCSVObject dto) {
            return Transaction.builder()
                    .withAmount(dto.getAmount())
                    .withBookDate(dto.getBookDate())
                    .withTransactionDate(dto.getTransactionDate())
                    .withNameOtherParty(dto.getNameOtherParty())
                    .build();
        }
        throw new ClassCastException();
    }
}
