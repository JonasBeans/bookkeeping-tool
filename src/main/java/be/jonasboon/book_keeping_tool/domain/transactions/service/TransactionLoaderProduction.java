package be.jonasboon.book_keeping_tool.domain.transactions.service;

import be.jonasboon.book_keeping_tool.domain.transactions.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
@ConditionalOnProperty(value = "app.development.enable-clearing-transactions", havingValue = "true", matchIfMissing = true)
public class TransactionLoaderProduction implements TransactionLoader {

    private final TransactionRepository transactionRepository;

    @EventListener
    @Override
    public void clear(ContextRefreshedEvent event) {
        transactionRepository.deleteAll();
    }

}
