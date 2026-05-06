package be.jonasboon.book_keeping_tool.domain.transactions.service;

import be.jonasboon.book_keeping_tool.domain.cost_centers.service.CostCenterService;
import be.jonasboon.book_keeping_tool.domain.transactions.DTO.TransactionDTO;
import be.jonasboon.book_keeping_tool.domain.transactions.classification.CostCenterPredictionService;
import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;
import be.jonasboon.book_keeping_tool.domain.transactions.mapper.TransactionMapper;
import be.jonasboon.book_keeping_tool.domain.transactions.processor.TransactionFileStrategy;
import be.jonasboon.book_keeping_tool.domain.transactions.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anySet;
import static org.mockito.Mockito.*;

class TransactionServiceTest {

    @Test
    void uploadSkipsExistingAndDuplicateTransactionsBeforeSavingAndPredicting() {
        TransactionMapper transactionMapper = mock(TransactionMapper.class);
        TransactionRepository transactionRepository = mock(TransactionRepository.class);
        CostCenterService costCenterService = mock(CostCenterService.class);
        TransactionFileStrategy transactionFileStrategy = mock(TransactionFileStrategy.class);
        CostCenterPredictionService costCenterPredictionService = mock(CostCenterPredictionService.class);
        EntityManager entityManager = mock(EntityManager.class);
        MultipartFile file = mock(MultipartFile.class);

        Transaction existing = transaction("Existing", "Existing party", "Old transaction");
        existing.refreshTransactionHash();
        Transaction newTransaction = transaction("New", "New party", "New transaction");
        Transaction duplicateOfNewTransaction = transaction("Different description", "New party", "Different message");

        when(transactionFileStrategy.process(file, "xlsx"))
                .thenReturn(List.of(existing, newTransaction, duplicateOfNewTransaction));
        when(transactionRepository.findExistingTransactionHashes(anySet()))
                .thenReturn(Set.of(existing.getTransactionHash()));
        when(transactionRepository.saveAll(any())).thenAnswer(invocation -> invocation.getArgument(0));
        when(transactionRepository.findAll()).thenReturn(List.of());
        when(transactionMapper.from(any(Transaction.class))).thenReturn(TransactionDTO.builder().build());

        TransactionService transactionService = new TransactionService(
                transactionMapper,
                transactionRepository,
                costCenterService,
                transactionFileStrategy,
                costCenterPredictionService,
                entityManager
        );

        transactionService.processTransactionUpload(file);

        ArgumentCaptor<List<Transaction>> predictionCaptor = ArgumentCaptor.forClass(List.class);
        verify(costCenterPredictionService).prefillCostCenters(predictionCaptor.capture());
        assertThat(predictionCaptor.getValue())
                .singleElement()
                .extracting(Transaction::getDescription)
                .isEqualTo("New");

        ArgumentCaptor<List<Transaction>> saveCaptor = ArgumentCaptor.forClass(List.class);
        verify(transactionRepository).saveAll(saveCaptor.capture());
        assertThat(saveCaptor.getValue())
                .singleElement()
                .extracting(Transaction::getDescription)
                .isEqualTo("New");
    }

    private Transaction transaction(String description, String nameOtherParty, String message) {
        return Transaction.builder()
                .withBookDate(LocalDate.of(2026, 1, 5))
                .withTransactionDate(LocalDate.of(2026, 1, 4))
                .withAmount(new BigDecimal("42.50"))
                .withDescription(description)
                .withNameOtherParty(nameOtherParty)
                .withMessage(message)
                .build();
    }
}
