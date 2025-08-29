package be.jonasboon.book_keeping_tool.mapper;

import be.jonasboon.book_keeping_tool.model.TransactionDTO;
import be.jonasboon.book_keeping_tool.persistence.entity.CostCenter;
import be.jonasboon.book_keeping_tool.persistence.entity.Transaction;
import be.jonasboon.book_keeping_tool.persistence.repository.CostCenterRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class TransactionMapper {

    private CostCenterRepository costCenterRepository;

    public TransactionDTO from(Transaction entity) {
        return TransactionDTO.builder()
                .withAmount(entity.getAmount())
                .withBookDate(entity.getBookDate())
                .withTransactionDate(entity.getTransactionDate())
                .withNameOtherParty(entity.getNameOtherParty())
                .withCostCenterReference(entity.getCostCenter().getCostCenter())
                .build();
    }

    public Transaction from(TransactionDTO dto) {
        CostCenter costCenter = costCenterRepository.findById(dto.getCostCenterReference())
                .orElseThrow(() -> new IllegalArgumentException("CostCenter not found with id: " + dto.getCostCenterReference()));

        return Transaction.builder()
                .withAmount(dto.getAmount())
                .withBookDate(dto.getBookDate())
                .withTransactionDate(dto.getTransactionDate())
                .withNameOtherParty(dto.getNameOtherParty())
                .withCostCenter(costCenter)
                .build();
    }
}
