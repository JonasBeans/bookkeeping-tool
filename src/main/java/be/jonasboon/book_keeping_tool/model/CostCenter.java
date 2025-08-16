package be.jonasboon.book_keeping_tool.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Builder(setterPrefix = "with")
@AllArgsConstructor
@Getter
@Setter
public class CostCenter {
        private String costCenter;
        private Boolean isCost;
        private Integer index;
        private BigDecimal totalAmount;
}



