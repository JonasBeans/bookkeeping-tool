package be.jonasboon.book_keeping_tool.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder(setterPrefix = "with")
@Getter
@Setter
public class TransactionDTO {
    public Long id;
    public LocalDate bookDate;
    private LocalDate transactionDate;
    private BigDecimal amount;
    private String nameOtherParty;
    private String costCenterReference;

    @Override
    public String toString() {
        return String.format("ID: %s, amount: %s", this.id, this.amount);
    }
}
