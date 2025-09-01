package be.jonasboon.book_keeping_tool.domain.cost_centers.entity;

import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cost_centers", schema = "public")
public class CostCenter implements Serializable {

    @Id
    private String costCenter;
    private Boolean isCost;
    @Setter
    private BigDecimal totalAmount;

    @JsonIgnore @Version
    private Long version;

    @JsonIgnore @OneToMany(mappedBy = "costCenter", fetch = FetchType.LAZY)
    private List<Transaction> transactions;

    public BigDecimal getTotalAmount() {
        if (totalAmount == null) {
            totalAmount = BigDecimal.ZERO;
        }
        return totalAmount;
    }

    public void addToTotalAmount(BigDecimal amount) {
        if (amount == null) {
            return;
        }
        if (totalAmount == null) {
            totalAmount = BigDecimal.ZERO;
        }
        totalAmount = totalAmount.add(amount);
    }

}
