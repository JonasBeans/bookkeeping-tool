package be.jonasboon.book_keeping_tool.persistence.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;

@Document(collection = "cost_centers")
@Builder(setterPrefix = "with")
@Getter
public class CostCenterEntity {

    String costCenter;
    Boolean isCost;
    Integer index;
    @Setter
    BigDecimal totalAmount = BigDecimal.ZERO;

    public BigDecimal getTotalAmount() {
        if (totalAmount == null) {
            totalAmount = BigDecimal.ZERO;
        }
        return totalAmount;
    }
}
