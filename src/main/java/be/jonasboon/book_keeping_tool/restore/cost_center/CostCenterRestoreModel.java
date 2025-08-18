package be.jonasboon.book_keeping_tool.restore.cost_center;

import be.jonasboon.book_keeping_tool.utils.mapper.CSVObject;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CostCenterRestoreModel(
        String id,
        String costCenter,
        Boolean isCost,
        BigDecimal totalAmount
) implements CSVObject {
}
