package be.javabeans.DTO;

import be.javabeans.utils.mapper.CSVObject;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@Builder(setterPrefix = "with")
public class TransactionDTO implements CSVObject {

    private LocalDate bookDate;
    private LocalDate transactionDate;
    private BigDecimal amount;
    private String nameOtherParty;

    @Override
    public String toString() {
        StringBuilder stringBuilder =  new StringBuilder();
        return stringBuilder
                .append("Bookdate: ").append(bookDate).append("\n")
                .append("Transation date: ").append(transactionDate).append("\n")
                .append("Amount: ").append(amount).append("\n")
                .append("Name of other party: ").append(nameOtherParty).append("\n")
                .toString();
    }
}
