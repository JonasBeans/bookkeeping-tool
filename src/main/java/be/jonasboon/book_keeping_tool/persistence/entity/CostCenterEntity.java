package be.jonasboon.book_keeping_tool.persistence.entity;

import lombok.Builder;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "cost_centers")
@Builder(setterPrefix = "with")
public class CostCenterEntity {

    String costCenter;
    Boolean isCost;
    Integer index;
}
