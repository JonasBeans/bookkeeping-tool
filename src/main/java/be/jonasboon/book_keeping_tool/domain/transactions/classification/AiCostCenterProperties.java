package be.jonasboon.book_keeping_tool.domain.transactions.classification;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

@Getter
@Setter
@ConfigurationProperties(prefix = "bookkeeping.ai.cost-center")
public class AiCostCenterProperties {

    private boolean enabled = false;
    private String apiKey = "";
    private String model = "claude-3-5-haiku-latest";
    private double confidenceThreshold = 0.75;
    private int maxBatchSize = 25;
    private Duration timeout = Duration.ofSeconds(10);
}
