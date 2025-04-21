package be.jonasboon.book_keeping_tool.utils.mapper;

import be.jonasboon.book_keeping_tool.DTO.TransactionDTO;

import java.math.BigDecimal;
import java.time.LocalDate;

import static be.jonasboon.book_keeping_tool.constants.DateConstants.TRANSACTION_DATE_TIME_FORMATTER;
import static be.jonasboon.book_keeping_tool.utils.StringUtils.convertEuropeanDecimal;
import static be.jonasboon.book_keeping_tool.utils.StringUtils.validateString;

public class TransactionCSVMapper implements CSVFileMapper {


    //Indexes of given value in the CSV file
    private final Integer BOOK_DATE = 1;
    private final Integer AMOUNT = 5;
    private final Integer TRANSACTION_DATE = 7;
    private final Integer NAME_OF_OTHER_PARTY = 9;

    @Override
    public CSVObject mapToObject(String[] seperatedValues) {
        LocalDate bookDate =  convertBookDate(seperatedValues[BOOK_DATE]);
        BigDecimal amount = convertAmount(seperatedValues[AMOUNT]);
        LocalDate transactionDate =  convertTransactionDate(seperatedValues[TRANSACTION_DATE]);
        String nameOtherParty = validateNameOfOtherParty(seperatedValues[NAME_OF_OTHER_PARTY]);

        return TransactionDTO.builder()
                .withBookDate(bookDate)
                .withAmount(amount)
                .withTransactionDate(transactionDate)
                .withNameOtherParty(nameOtherParty)
                .build();
    }

    private String validateNameOfOtherParty(String nameOtherParty) {
        if(validateString.test(nameOtherParty))
            return nameOtherParty;
        throw new RuntimeException("Name of other party is empty");
    }

    private BigDecimal convertAmount(String value){
        if (value == null) throw new NullPointerException("Amount is empty");
        value = convertEuropeanDecimal.apply(value);
        try {
            return new BigDecimal(value);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Could not convert amount to a number while reading CSV file");
        }

    }
    private LocalDate convertTransactionDate(String date){
        if(validateString.test(date))
            return LocalDate.parse(date, TRANSACTION_DATE_TIME_FORMATTER);
        throw new NullPointerException("Transaction date is empty");
    }
    private LocalDate convertBookDate(String date){
        if(validateString.test(date))
            return LocalDate.parse(date, TRANSACTION_DATE_TIME_FORMATTER);
        throw new NullPointerException("Book date is empty");
    }

}