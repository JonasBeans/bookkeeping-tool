package be.jonasboon.book_keeping_tool.persistence.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDate;

@Document(collation = "transactions")
@Builder(setterPrefix = "with")
@EqualsAndHashCode
@Getter
@Setter
public class TransactionEntity {
    public LocalDate bookDate;
    private LocalDate transactionDate;
    private BigDecimal amount;
    private String nameOtherParty;
    private Integer costCenterIndex;
}
