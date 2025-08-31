package be.jonasboon.book_keeping_tool.domain.transactions.service;

import be.jonasboon.book_keeping_tool.domain.transactions.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class TransactionLoader {

    private final TransactionRepository transactionRepository;

    @EventListener
    public void clear(ContextRefreshedEvent event) {
        transactionRepository.deleteAll();
    }

}
