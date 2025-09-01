package be.jonasboon.book_keeping_tool.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "app.development")
public record DevelopmentProperties (
        /**
         * Enable or disable the clearing of all transactions on application startup.
         */
        boolean cleanTransactions
) { }