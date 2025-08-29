package be.jonasboon.book_keeping_tool.mapper;

import be.jonasboon.book_keeping_tool.DTO.TransactionDTO;
import be.jonasboon.book_keeping_tool.persistence.entity.Transaction;
import be.jonasboon.book_keeping_tool.restore.common.RestoreEntityMapper;
import be.jonasboon.book_keeping_tool.utils.mapper.CSVObject;

public class TransactionEntityMapper implements RestoreEntityMapper<Transaction> {

    @Override
    public Transaction map(CSVObject restoreModel) {
        if (restoreModel instanceof TransactionDTO dto) {
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
