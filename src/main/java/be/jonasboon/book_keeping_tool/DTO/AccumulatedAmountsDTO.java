package be.jonasboon.book_keeping_tool.DTO;

import java.math.BigDecimal;

public record AccumulatedAmountsDTO(
        BigDecimal totalIncome,
        BigDecimal totalCost
) { }
