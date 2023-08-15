package be.javabeans.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder(setterPrefix = "with")
@Getter
@Setter
public class Transaction {
    public LocalDate bookDate;
    private LocalDate transactionDate;
    private BigDecimal amount;
    private String nameOtherParty;
    private Integer costCenterIndex;
}
