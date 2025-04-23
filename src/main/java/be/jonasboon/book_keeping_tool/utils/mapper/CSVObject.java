package be.jonasboon.book_keeping_tool.utils.mapper;

import java.math.BigDecimal;
import java.time.LocalDate;

public interface CSVObject {

    public LocalDate getBookDate();
    public LocalDate getTransactionDate();
    public BigDecimal getAmount();
    public String getNameOtherParty();

}
