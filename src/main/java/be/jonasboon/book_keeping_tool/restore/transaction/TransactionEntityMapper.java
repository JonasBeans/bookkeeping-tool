package be.jonasboon.book_keeping_tool.restore.transaction;

import be.jonasboon.book_keeping_tool.persistence.entity.TransactionEntity;
import be.jonasboon.book_keeping_tool.restore.common.RestoreEntityMapper;
import be.jonasboon.book_keeping_tool.utils.mapper.CSVObject;

public class TransactionEntityMapper implements RestoreEntityMapper<TransactionEntity> {

    @Override
    public TransactionEntity map(CSVObject restoreModel) {
        if (restoreModel instanceof TransactionRestoreModel model) {
            return TransactionEntity.builder()
                    .withBookDate(model.bookDate())
                    .withTransactionDate(model.transactionDate())
                    .withAmount(model.amount())
                    .withNameOtherParty(model.nameOtherParty())
                    .withCostCenterId(model.costCenterId())
                    .build();
        } else {
            throw new ClassCastException();
        }
    }
}
