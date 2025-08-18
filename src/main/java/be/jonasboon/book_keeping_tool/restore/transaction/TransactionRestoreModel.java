package be.jonasboon.book_keeping_tool.restore.transaction;

import be.jonasboon.book_keeping_tool.utils.mapper.CSVObject;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record TransactionRestoreModel(
        LocalDate bookDate,
        LocalDate transactionDate,
        BigDecimal amount,
        String nameOtherParty,
        String costCenterId
) implements CSVObject {
}
