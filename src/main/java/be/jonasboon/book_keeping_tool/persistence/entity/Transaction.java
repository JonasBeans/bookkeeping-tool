package be.jonasboon.book_keeping_tool.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Builder(setterPrefix = "with")
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
@Table(name = "transactions")
@Entity
@Getter
@Setter
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;
    public LocalDate bookDate;
    private LocalDate transactionDate;
    private BigDecimal amount;
    private String nameOtherParty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cost_center")
    private CostCenter costCenter;

    public boolean hasNoCostCenter() {
        return Objects.isNull(costCenter) ;
    }

    public CostCenter getCostCenter() {
        if (costCenter == null) {
            return new CostCenter();
        }
        return costCenter;
    }

}
