package be.javabeans.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
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
