package be.jonasboon.book_keeping_tool.mapper;

import be.jonasboon.book_keeping_tool.model.TransactionDTO;
import be.jonasboon.book_keeping_tool.persistence.entity.Transaction;

public class TransactionMapper {

    public static TransactionDTO from(Transaction entity) {
        return TransactionDTO.builder()
                .withId(entity.getId())
                .withAmount(entity.getAmount())
                .withBookDate(entity.getBookDate())
                .withTransactionDate(entity.getTransactionDate())
                .withNameOtherParty(entity.getNameOtherParty())
                .withCostCenterReference(entity.getCostCenter().getCostCenter())
                .build();
    }

    public static Transaction from(TransactionDTO dto) {
        return Transaction.builder()
                .withId(dto.getId())
                .withAmount(dto.getAmount())
                .withBookDate(dto.getBookDate())
                .withTransactionDate(dto.getTransactionDate())
                .withNameOtherParty(dto.getNameOtherParty())
                //.withCostCenter(dto.getCostCenterReference())
                .build();
    }
}
