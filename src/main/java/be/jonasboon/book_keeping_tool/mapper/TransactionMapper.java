package be.jonasboon.book_keeping_tool.mapper;

import be.jonasboon.book_keeping_tool.model.TransactionDTO;
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

    public static TransactionDTO from(TransactionEntity entity) {
        return TransactionDTO.builder()
                .withId(entity.getId())
                .withAmount(entity.getAmount())
                .withBookDate(entity.getBookDate())
                .withTransactionDate(entity.getTransactionDate())
                .withNameOtherParty(entity.getNameOtherParty())
                .withCostCenterId(entity.getCostCenterId())
                .build();
    }

    public static TransactionEntity from(TransactionDTO dto) {
        return TransactionEntity.builder()
                .withId(dto.getId())
                .withAmount(dto.getAmount())
                .withBookDate(dto.getBookDate())
                .withTransactionDate(dto.getTransactionDate())
                .withNameOtherParty(dto.getNameOtherParty())
                .withCostCenterId(dto.getCostCenterId())
                .build();
    }
}
