package be.jonasboon.book_keeping_tool.domain.transactions.classification;

import be.jonasboon.book_keeping_tool.domain.cost_centers.entity.CostCenter;
import be.jonasboon.book_keeping_tool.domain.cost_centers.repository.CostCenterRepository;
import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestClientException;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class CostCenterPredictionService {

    private final AiCostCenterProperties properties;
    private final CostCenterRepository costCenterRepository;
    private final CostCenterClassifier costCenterClassifier;

    public void prefillCostCenters(List<Transaction> transactions) {
        if (!properties.isEnabled() || transactions.isEmpty()) {
            return;
        }
        if (!StringUtils.hasText(properties.getApiKey())) {
            log.warn("AI cost-center prediction is enabled but no Anthropic API key is configured");
            return;
        }

        List<CostCenter> costCenters = costCenterRepository.findAll();
        if (costCenters.isEmpty()) {
            return;
        }

        Map<String, CostCenter> costCenterByName = costCenters.stream()
                .collect(Collectors.toMap(CostCenter::getCostCenter, Function.identity()));

        try {
            costCenterClassifier.predict(transactions, costCenters).forEach(prediction ->
                    applyPrediction(prediction, transactions, costCenterByName)
            );
        } catch (RestClientException | IllegalStateException e) {
            log.warn("AI cost-center prediction failed; upload will continue without predictions: {}", e.getMessage());
            log.debug("AI cost-center prediction failure details", e);
        }
    }

    private void applyPrediction(
            CostCenterPrediction prediction,
            List<Transaction> transactions,
            Map<String, CostCenter> costCenterByName
    ) {
        if (prediction.transactionIndex() < 0 || prediction.transactionIndex() >= transactions.size()) {
            log.warn("Ignoring AI cost-center prediction with invalid transaction index: {}", prediction);
            return;
        }
        if (prediction.confidence() < properties.getConfidenceThreshold()) {
            return;
        }

        CostCenter costCenter = costCenterByName.get(prediction.costCenter());
        if (costCenter == null) {
            log.warn("Ignoring AI cost-center prediction for unknown cost center: {}", prediction.costCenter());
            return;
        }

        Transaction transaction = transactions.get(prediction.transactionIndex());
        if (transaction.hasNoCostCenter()) {
            transaction.setCostCenter(costCenter);
        }
    }
}
