package be.jonasboon.book_keeping_tool.model;

import lombok.*;

@Builder(setterPrefix = "with")
@AllArgsConstructor
@Getter
@Setter
public class CostCenter {
        private String costCenter;
        private Boolean isCost;
        private Integer index;
}



