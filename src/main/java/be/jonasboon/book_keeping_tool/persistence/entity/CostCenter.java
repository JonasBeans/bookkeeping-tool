package be.jonasboon.book_keeping_tool.persistence.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Builder(setterPrefix = "with")
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cost_centers", schema = "public")
public class CostCenter {

    @Id
    private String costCenter;
    private Boolean isCost;
    @Setter
    private BigDecimal totalAmount;
    @Version
    private Long version;

    @OneToMany(mappedBy = "costCenter", fetch = FetchType.LAZY)
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
