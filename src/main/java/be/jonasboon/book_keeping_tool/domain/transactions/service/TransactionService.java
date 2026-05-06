package be.jonasboon.book_keeping_tool.domain.transactions.service;

import be.jonasboon.book_keeping_tool.domain.cost_centers.service.CostCenterService;
import be.jonasboon.book_keeping_tool.domain.transactions.DTO.TransactionDTO;
import be.jonasboon.book_keeping_tool.domain.transactions.classification.CostCenterPredictionService;
import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;
import be.jonasboon.book_keeping_tool.domain.transactions.mapper.TransactionMapper;
import be.jonasboon.book_keeping_tool.domain.transactions.processor.TransactionFileStrategy;
import be.jonasboon.book_keeping_tool.domain.transactions.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static be.jonasboon.book_keeping_tool.utils.BookYearUtils.endExclusiveOf;
import static be.jonasboon.book_keeping_tool.utils.BookYearUtils.startOf;


@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class TransactionService {

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final CostCenterService costCenterService;
    private final TransactionFileStrategy transactionFileStrategy;
    private final CostCenterPredictionService costCenterPredictionService;
    private final EntityManager entityManager;

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::from)
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> getTransactionsForBookYear(Integer bookYear) {
        if (bookYear == null) {
            return getAllTransactions();
        }
        return transactionRepository.findByBookDateGreaterThanEqualAndBookDateLessThan(startOf(bookYear), endExclusiveOf(bookYear)).stream()
                .map(transactionMapper::from)
                .collect(Collectors.toList());
    }

    public List<Integer> getAvailableBookYears() {
        return transactionRepository.findAll().stream()
                .map(Transaction::getBookDate)
                .map(LocalDate::getYear)
                .distinct()
                .sorted(Comparator.reverseOrder())
                .toList();
    }

    public List<TransactionDTO> processTransactionUpload(MultipartFile file) {
        List<Transaction> transactions = filterNewTransactions(transactionFileStrategy.process(file, "xlsx"));
        costCenterPredictionService.prefillCostCenters(transactions);

        List<Transaction> savedTransactions = transactions.isEmpty()
                ? List.of()
                : transactionRepository.saveAll(transactions);
        entityManager.flush();
        costCenterService.updateTotalAmounts(getAllTransactions());
        return savedTransactions.stream().map(transactionMapper::from).toList();
    }

    public List<TransactionDTO> loadAssigned(List<TransactionDTO> transactions, Integer bookYear) {
        transactionRepository.saveAll(
                transactions.stream()
                        .filter(TransactionDTO::hasNoEmptyField)
                        .map(transactionMapper::from)
                        .toList()
        );
        entityManager.flush();
        costCenterService.updateTotalAmounts(getAllTransactions());
        return getTransactionsForBookYear(bookYear);
    }

    public void deleteTransactionsForBookYear(Integer bookYear) {
        transactionRepository.deleteByBookDateGreaterThanEqualAndBookDateLessThan(startOf(bookYear), endExclusiveOf(bookYear));
        entityManager.flush();
        costCenterService.updateTotalAmounts(getAllTransactions());
    }

    private List<Transaction> filterNewTransactions(List<Transaction> transactions) {
        Map<String, Transaction> uniqueUploadedTransactions = new LinkedHashMap<>();
        transactions.forEach(transaction -> {
            transaction.refreshTransactionHash();
            uniqueUploadedTransactions.putIfAbsent(transaction.getTransactionHash(), transaction);
        });

        if (uniqueUploadedTransactions.isEmpty()) {
            return List.of();
        }

        Set<String> existingTransactionHashes = transactionRepository.findExistingTransactionHashes(uniqueUploadedTransactions.keySet());
        List<Transaction> newTransactions = uniqueUploadedTransactions.entrySet().stream()
                .filter(entry -> !existingTransactionHashes.contains(entry.getKey()))
                .map(Map.Entry::getValue)
                .toList();
        int skippedTransactions = transactions.size() - newTransactions.size();
        if (skippedTransactions > 0) {
            log.info("Skipped {} duplicate transactions during upload", skippedTransactions);
        }
        return newTransactions;
    }

}
