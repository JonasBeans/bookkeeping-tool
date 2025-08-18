package be.jonasboon.book_keeping_tool.restore.transaction;

import be.jonasboon.book_keeping_tool.utils.mapper.CSVFileMapper;
import be.jonasboon.book_keeping_tool.utils.mapper.CSVObject;

import java.math.BigDecimal;
import java.time.LocalDate;

public class TransactionCSVMapper implements CSVFileMapper {

    private final Integer BOOK_DATE = 0;
    private final Integer TRANSACTION_DATE = 1;
    private final Integer AMOUNT = 2;
    private final Integer NAME_OTHER_PARTY = 3;
    private final Integer COST_CENTER_ID = 4;

    @Override
    public CSVObject mapToObject(String[] seperatedValues) {
        return TransactionRestoreModel.builder()
                .bookDate(LocalDate.parse(seperatedValues[BOOK_DATE]))
                .transactionDate(LocalDate.parse(seperatedValues[TRANSACTION_DATE]))
                .amount(new BigDecimal(seperatedValues[AMOUNT]))
                .nameOtherParty(seperatedValues[NAME_OTHER_PARTY])
                .costCenterId(seperatedValues[COST_CENTER_ID].isBlank() ? null : seperatedValues[COST_CENTER_ID])
                .build();
    }
}
