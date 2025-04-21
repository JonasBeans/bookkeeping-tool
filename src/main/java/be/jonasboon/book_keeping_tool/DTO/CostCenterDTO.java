package be.jonasboon.book_keeping_tool.DTO;

import lombok.*;

@Getter
@Setter
@Builder(setterPrefix = "with")
@EqualsAndHashCode
@AllArgsConstructor
public class CostCenterDTO {

    private String costCenter;
    private Boolean isCost;
    private Integer index;

    public CostCenterDTO(String costCenter, String isCost, String index) {
        this.costCenter = costCenter;
        this.isCost = Boolean.valueOf(isCost);
        this.index = Integer.valueOf(index);
    }
}
