package be.jonasboon.book_keeping_tool.domain.transactions.service;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

public interface TransactionLoader {
    @EventListener
    void clear(ContextRefreshedEvent event);
}
