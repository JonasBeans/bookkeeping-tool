package be.jonasboon.book_keeping_tool.domain.transactions.entity;

import be.jonasboon.book_keeping_tool.domain.cost_centers.entity.CostCenter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Builder(setterPrefix = "with")
@AllArgsConstructor@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "transactions", schema = "public")
@Entity
@Getter@Setter
public class Transaction implements Serializable {

    @JsonIgnore
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDate bookDate;
    private LocalDate transactionDate;
    private BigDecimal amount;
    private String nameOtherParty;
    @Version @JsonIgnore
    private Long version;

    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "cost_center")
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
