package be.jonasboon.book_keeping_tool.domain.transactions.classification;

import be.jonasboon.book_keeping_tool.domain.cost_centers.entity.CostCenter;
import be.jonasboon.book_keeping_tool.domain.cost_centers.repository.CostCenterRepository;
import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CostCenterPredictionServiceTest {

    @Test
    void appliesKnownCostCenterPredictionAboveThreshold() {
        CostCenter food = costCenter("Food");
        CostCenterRepository repository = mock(CostCenterRepository.class);
        when(repository.findAll()).thenReturn(List.of(food));

        CostCenterClassifier classifier = (transactions, costCenters) ->
                List.of(new CostCenterPrediction(0, "Food", 0.91, "supermarket transaction"));
        Transaction transaction = transaction();

        predictionService(repository, classifier).prefillCostCenters(List.of(transaction));

        assertThat(transaction.getCostCenter().getCostCenter()).isEqualTo("Food");
    }

    @Test
    void ignoresLowConfidenceAndUnknownCostCenterPredictions() {
        CostCenterRepository repository = mock(CostCenterRepository.class);
        when(repository.findAll()).thenReturn(List.of(costCenter("Food")));

        CostCenterClassifier classifier = (transactions, costCenters) -> List.of(
                new CostCenterPrediction(0, "Food", 0.50, "not confident enough"),
                new CostCenterPrediction(1, "Unknown", 0.95, "not configured")
        );
        Transaction lowConfidence = transaction();
        Transaction unknownCostCenter = transaction();

        predictionService(repository, classifier).prefillCostCenters(List.of(lowConfidence, unknownCostCenter));

        assertThat(lowConfidence.hasNoCostCenter()).isTrue();
        assertThat(unknownCostCenter.hasNoCostCenter()).isTrue();
    }

    @Test
    void continuesWhenClassifierFails() {
        CostCenterRepository repository = mock(CostCenterRepository.class);
        when(repository.findAll()).thenReturn(List.of(costCenter("Food")));

        CostCenterClassifier classifier = (transactions, costCenters) -> {
            throw new IllegalStateException("remote API failure");
        };
        Transaction transaction = transaction();

        assertThatNoException().isThrownBy(() ->
                predictionService(repository, classifier).prefillCostCenters(List.of(transaction))
        );
        assertThat(transaction.hasNoCostCenter()).isTrue();
    }

    private CostCenterPredictionService predictionService(
            CostCenterRepository repository,
            CostCenterClassifier classifier
    ) {
        AiCostCenterProperties properties = new AiCostCenterProperties();
        properties.setEnabled(true);
        properties.setApiKey("test-api-key");
        properties.setConfidenceThreshold(0.75);
        return new CostCenterPredictionService(properties, repository, classifier);
    }

    private CostCenter costCenter(String name) {
        return CostCenter.builder()
                .withCostCenter(name)
                .withIsCost(true)
                .build();
    }

    private Transaction transaction() {
        return Transaction.builder()
                .withAmount(BigDecimal.TEN)
                .withDescription("Card payment")
                .withNameOtherParty("Supermarket")
                .withMessage("Groceries")
                .build();
    }
}
