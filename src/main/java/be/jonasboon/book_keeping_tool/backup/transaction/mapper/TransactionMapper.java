package be.jonasboon.book_keeping_tool.backup.transaction.mapper;

import be.jonasboon.book_keeping_tool.backup.common.RestoreMapper;
import be.jonasboon.book_keeping_tool.backup.transaction.model.TransactionBackupModel;
import be.jonasboon.book_keeping_tool.persistence.entity.Transaction;

public class TransactionMapper implements RestoreMapper<Transaction, TransactionBackupModel> {

    public static final TransactionMapper INSTANCE = new TransactionMapper();

    public TransactionBackupModel of(Transaction transaction) {
        return TransactionBackupModel.builder()
                .amount(transaction.getAmount())
                .transactionDate(transaction.getTransactionDate())
                .bookDate(transaction.getBookDate())
                .nameOtherParty(transaction.getNameOtherParty())
                .costCenterReference(transaction.getCostCenter().getCostCenter())
                .build();
    }
}
