package be.jonasboon.book_keeping_tool.domain.transactions.classification;

import be.jonasboon.book_keeping_tool.domain.cost_centers.entity.CostCenter;
import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class AnthropicCostCenterClassifier implements CostCenterClassifier {

    private static final String ANTHROPIC_VERSION = "2023-06-01";

    private final AiCostCenterProperties properties;
    private final ObjectMapper objectMapper;
    private final RestClient restClient;

    public AnthropicCostCenterClassifier(
            AiCostCenterProperties properties,
            ObjectMapper objectMapper,
            RestClient.Builder restClientBuilder
    ) {
        this.properties = properties;
        this.objectMapper = objectMapper;

        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        requestFactory.setConnectTimeout(properties.getTimeout());
        requestFactory.setReadTimeout(properties.getTimeout());

        this.restClient = restClientBuilder
                .requestFactory(requestFactory)
                .baseUrl("https://api.anthropic.com")
                .build();
    }

    @Override
    public List<CostCenterPrediction> predict(List<Transaction> transactions, List<CostCenter> costCenters) {
        if (transactions.isEmpty() || costCenters.isEmpty()) {
            return List.of();
        }

        List<CostCenterPrediction> predictions = new ArrayList<>();
        int batchSize = Math.max(1, properties.getMaxBatchSize());
        for (int start = 0; start < transactions.size(); start += batchSize) {
            int end = Math.min(start + batchSize, transactions.size());
            predictions.addAll(predictBatch(transactions.subList(start, end), costCenters, start));
        }
        return predictions;
    }

    private List<CostCenterPrediction> predictBatch(List<Transaction> transactions, List<CostCenter> costCenters, int indexOffset) {
        JsonNode response = restClient.post()
                .uri("/v1/messages")
                .contentType(MediaType.APPLICATION_JSON)
                .header("x-api-key", properties.getApiKey())
                .header("anthropic-version", ANTHROPIC_VERSION)
                .body(createRequestBody(transactions, costCenters, indexOffset))
                .retrieve()
                .body(JsonNode.class);

        if (response == null) {
            return List.of();
        }
        return parsePredictions(response);
    }

    private Map<String, Object> createRequestBody(List<Transaction> transactions, List<CostCenter> costCenters, int indexOffset) {
        return Map.of(
                "model", properties.getModel(),
                "max_tokens", 2048,
                "temperature", 0,
                "system", """
                        You classify bookkeeping transactions into exactly one known cost center.
                        Return only a JSON array. Do not include Markdown or explanatory text.
                        Every object must have: transactionIndex (number), costCenter (string), confidence (number from 0 to 1), reason (string).
                        costCenter must exactly match one of the allowed cost center names.
                        If unsure, use "Skip" when available or the closest allowed center with low confidence.
                        """,
                "messages", List.of(Map.of(
                        "role", "user",
                        "content", createPrompt(transactions, costCenters, indexOffset)
                ))
        );
    }

    private String createPrompt(List<Transaction> transactions, List<CostCenter> costCenters, int indexOffset) {
        StringBuilder prompt = new StringBuilder();
        prompt.append("Allowed cost centers:\n");
        costCenters.forEach(costCenter -> prompt
                .append("- ")
                .append(costCenter.getCostCenter())
                .append(Boolean.TRUE.equals(costCenter.getIsCost()) ? " (cost)" : " (income)")
                .append('\n'));

        prompt.append("\nTransactions to classify:\n");
        for (int index = 0; index < transactions.size(); index++) {
            Transaction transaction = transactions.get(index);
            prompt.append("- transactionIndex: ").append(indexOffset + index).append('\n');
            prompt.append("  amount: ").append(transaction.getAmount()).append('\n');
            prompt.append("  description: ").append(nullToEmpty(transaction.getDescription())).append('\n');
            prompt.append("  nameOtherParty: ").append(nullToEmpty(transaction.getNameOtherParty())).append('\n');
            prompt.append("  message: ").append(nullToEmpty(transaction.getMessage())).append('\n');
        }
        return prompt.toString();
    }

    private List<CostCenterPrediction> parsePredictions(JsonNode response) {
        String content = response.path("content").path(0).path("text").asText();
        if (content.isBlank()) {
            return List.of();
        }

        try {
            JsonNode predictionsNode = objectMapper.readTree(stripJsonCodeFence(content));
            if (!predictionsNode.isArray()) {
                log.warn("Anthropic cost-center response was not a JSON array");
                return List.of();
            }

            List<CostCenterPrediction> predictions = new ArrayList<>();
            for (JsonNode predictionNode : predictionsNode) {
                predictions.add(new CostCenterPrediction(
                        predictionNode.path("transactionIndex").asInt(-1),
                        predictionNode.path("costCenter").asText(),
                        predictionNode.path("confidence").asDouble(0),
                        predictionNode.path("reason").asText()
                ));
            }
            return predictions;
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("Could not parse Anthropic cost-center response", e);
        }
    }

    private String stripJsonCodeFence(String content) {
        return content
                .replaceFirst("^```json\\s*", "")
                .replaceFirst("^```\\s*", "")
                .replaceFirst("\\s*```$", "")
                .trim();
    }

    private String nullToEmpty(String value) {
        return value == null ? "" : value;
    }
}
