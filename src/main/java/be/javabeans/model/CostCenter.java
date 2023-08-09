package be.javabeans.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder(setterPrefix = "with")
@AllArgsConstructor
public class CostCenter {
        String costCenter;
        Boolean isCost;
        Integer index;
}
