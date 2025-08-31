package be.jonasboon.book_keeping_tool.domain.cost_centers.DTO;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder(setterPrefix = "with")
@EqualsAndHashCode
@AllArgsConstructor
public class CostCenterDTO {

    private Long id;
    private String costCenter;
    private Boolean isCost;
    private BigDecimal totalAmount;
    private Long version;

    public CostCenterDTO(String costCenter, String isCost) {
        this.costCenter = costCenter;
        this.isCost = Boolean.valueOf(isCost);
    }

}
