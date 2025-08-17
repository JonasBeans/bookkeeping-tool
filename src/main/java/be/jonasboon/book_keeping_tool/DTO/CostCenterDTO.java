package be.jonasboon.book_keeping_tool.DTO;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder(setterPrefix = "with")
@EqualsAndHashCode
@AllArgsConstructor
public class CostCenterDTO {

    private String id;
    private String costCenter;
    private Boolean isCost;
    private BigDecimal totalAmount;

    public CostCenterDTO(String costCenter, String isCost) {
        this.costCenter = costCenter;
        this.isCost = Boolean.valueOf(isCost);
    }

    public CostCenterDTO(String costCenter, String isCost, BigDecimal totalAmount) {
        this.costCenter = costCenter;
        this.isCost = Boolean.valueOf(isCost);
        this.totalAmount = totalAmount;
    }
}
