package be.jonasboon.book_keeping_tool.domain.transactions.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder(setterPrefix = "with")
@Getter
@Setter
public class TransactionDTO {

    private Long id;
    private LocalDate bookDate;
    private LocalDate transactionDate;
    private BigDecimal amount;
    private String nameOtherParty;
    private String costCenterReference;
    private Long version;

    @Override
    public String toString() {
        return String.format("amount: %s", this.amount);
    }
}
