package be.jonasboon.book_keeping_tool.model;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class AccumulatedAmounts {

    BigDecimal totalIncome = BigDecimal.ZERO;
    BigDecimal totalCost = BigDecimal.ZERO;

    public void addIncome(BigDecimal amount) {
        this.totalIncome = this.totalIncome.add(amount);
    }

    public void addCost(BigDecimal amount) {
        this.totalCost = this.totalCost.add(amount);
    }
}
