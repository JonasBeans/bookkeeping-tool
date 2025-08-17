package be.jonasboon.book_keeping_tool.backup.transaction.mapper;

import be.jonasboon.book_keeping_tool.backup.common.BackupModelMapper;
import be.jonasboon.book_keeping_tool.backup.transaction.model.TransactionBackupModel;
import be.jonasboon.book_keeping_tool.persistence.entity.TransactionEntity;

public class TransactionMapper implements BackupModelMapper<TransactionEntity, TransactionBackupModel> {

    public static final TransactionMapper INSTANCE = new TransactionMapper();

    public TransactionBackupModel of(TransactionEntity transaction) {
        return TransactionBackupModel.builder()
                .amount(transaction.getAmount())
                .transactionDate(transaction.getTransactionDate())
                .bookDate(transaction.getBookDate())
                .nameOtherParty(transaction.getNameOtherParty())
                .costCenterId(transaction.getCostCenterId())
                .build();
    }
}
