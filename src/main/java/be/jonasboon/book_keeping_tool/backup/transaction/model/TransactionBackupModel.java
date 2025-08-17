package be.jonasboon.book_keeping_tool.backup.transaction.model;

import be.jonasboon.book_keeping_tool.backup.common.BackupModel;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record TransactionBackupModel(

        BigDecimal amount,
        LocalDate transactionDate,
        LocalDate bookDate,
        String nameOtherParty,
        String costCenterId
) implements BackupModel {

    public String toBackupString() {
       return String.format("%s,%s,%s,%s,%s",
                bookDate,
                transactionDate,
                amount,
                nameOtherParty,
                costCenterId
        );
    }
}
