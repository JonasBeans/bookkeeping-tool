package be.jonasboon.book_keeping_tool.mapper;

import be.jonasboon.book_keeping_tool.DTO.TransactionDTO;
import be.jonasboon.book_keeping_tool.persistence.entity.TransactionEntity;
import be.jonasboon.book_keeping_tool.restore.common.RestoreEntityMapper;
import be.jonasboon.book_keeping_tool.utils.mapper.CSVObject;

public class TransactionEntityMapper implements RestoreEntityMapper<TransactionEntity> {

    @Override
    public TransactionEntity map(CSVObject restoreModel) {
        if (restoreModel instanceof TransactionDTO dto) {
            return TransactionEntity.builder()
                    .withAmount(dto.getAmount())
                    .withBookDate(dto.getBookDate())
                    .withTransactionDate(dto.getTransactionDate())
                    .withNameOtherParty(dto.getNameOtherParty())
                    .build();
        }
        throw new ClassCastException();
    }
}
