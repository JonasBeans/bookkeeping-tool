package be.jonasboon.book_keeping_tool.domain.transactions.classification;

public record CostCenterPrediction(
        int transactionIndex,
        String costCenter,
        double confidence,
        String reason
) {
}
