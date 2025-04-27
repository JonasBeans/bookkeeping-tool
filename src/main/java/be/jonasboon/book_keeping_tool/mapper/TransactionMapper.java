package be.jonasboon.book_keeping_tool.mapper;

import be.jonasboon.book_keeping_tool.model.Transaction;
import be.jonasboon.book_keeping_tool.persistence.entity.TransactionEntity;
import be.jonasboon.book_keeping_tool.utils.mapper.CSVObject;

public class TransactionMapper {

    public static TransactionEntity from(CSVObject dto) {
        return TransactionEntity.builder()
                .withAmount(dto.getAmount())
                .withBookDate(dto.getBookDate())
                .withTransactionDate(dto.getTransactionDate())
                .withNameOtherParty(dto.getNameOtherParty())
                .build();
    }

    public static Transaction from(TransactionEntity entity) {
        return Transaction.builder()
                .withId(entity.getId())
                .withAmount(entity.getAmount())
                .withBookDate(entity.getBookDate())
                .withTransactionDate(entity.getTransactionDate())
                .withNameOtherParty(entity.getNameOtherParty())
                .withCostCenterIndex(entity.getCostCenterIndex())
                .build();
    }

    public static TransactionEntity from(Transaction dto) {
        return TransactionEntity.builder()
                .withId(dto.getId())
                .withAmount(dto.getAmount())
                .withBookDate(dto.getBookDate())
                .withTransactionDate(dto.getTransactionDate())
                .withNameOtherParty(dto.getNameOtherParty())
                .withCostCenterIndex(dto.getCostCenterIndex())
                .build();
    }
}
