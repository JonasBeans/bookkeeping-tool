package be.jonasboon.book_keeping_tool.persistence.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "cost_centers")
@Builder(setterPrefix = "with")
@Getter
public class CostCenterEntity {

    @Id
    String id;
    String costCenter;
    Boolean isCost;
    @Setter
    BigDecimal totalAmount;

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

    public CostCenterEntity resetTotalAmount() {
        totalAmount = BigDecimal.ZERO;
        return this;
    }
}
