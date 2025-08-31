package be.jonasboon.book_keeping_tool.domain.cost_centers.DTO;

import java.math.BigDecimal;

public record AccumulatedAmountsDTO(
        BigDecimal totalIncome,
        BigDecimal totalCost
) { }
