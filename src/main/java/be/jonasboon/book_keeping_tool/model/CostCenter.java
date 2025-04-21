package be.jonasboon.book_keeping_tool.model;

import lombok.*;

@Getter
@Setter
@Builder(setterPrefix = "with")
@EqualsAndHashCode
@AllArgsConstructor
public class CostCenter {
        String costCenter;
        Boolean isCost;
        Integer index;
}



