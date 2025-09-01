package be.jonasboon.book_keeping_tool.domain.transactions.service;

import be.jonasboon.book_keeping_tool.domain.transactions.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@AllArgsConstructor
@ConditionalOnProperty(value = "app.development.enable-clearing-transactions", havingValue = "false")
public class TransactionLoaderDevelopment implements TransactionLoader {

    private final TransactionRepository transactionRepository;

    @EventListener
    @Override
    public void clear(ContextRefreshedEvent event) {
        log.debug("Skipping clearing of transactions on startup in development mode.");
    }

}
