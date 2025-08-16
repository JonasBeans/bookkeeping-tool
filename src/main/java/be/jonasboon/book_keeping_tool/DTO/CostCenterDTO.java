package be.jonasboon.book_keeping_tool.DTO;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder(setterPrefix = "with")
@EqualsAndHashCode
@AllArgsConstructor
public class CostCenterDTO {

    private String costCenter;
    private Boolean isCost;
    private Integer index;
    private BigDecimal totalAmount;

    public CostCenterDTO(String costCenter, String isCost, String index) {
        this.costCenter = costCenter;
        this.isCost = Boolean.valueOf(isCost);
        this.index = Integer.valueOf(index);
    }

    public CostCenterDTO(String costCenter, String isCost, String index, BigDecimal totalAmount) {
        this.costCenter = costCenter;
        this.isCost = Boolean.valueOf(isCost);
        this.index = Integer.valueOf(index);
        this.totalAmount = totalAmount;
    }
}
