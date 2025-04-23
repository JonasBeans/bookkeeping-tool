package be.jonasboon.book_keeping_tool.DTO;

import be.jonasboon.book_keeping_tool.utils.mapper.CSVObject;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder(setterPrefix = "with")
@EqualsAndHashCode
public class TransactionDTO implements CSVObject {

    private LocalDate bookDate;
    private LocalDate transactionDate;
    private BigDecimal amount;
    private String nameOtherParty;

    @Override
    public String toString() {
        return String.format("""
                Bookdate: %s
                Transaction date: %s
                Amount: %s
                Name of other party: %s
                """, bookDate, transactionDate, amount, nameOtherParty);
    }
}
