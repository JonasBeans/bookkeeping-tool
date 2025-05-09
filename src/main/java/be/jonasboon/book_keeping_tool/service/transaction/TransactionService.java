package be.jonasboon.book_keeping_tool.service.transaction;

import be.jonasboon.book_keeping_tool.balance_sheet.BalanceSheetUpdater;
import be.jonasboon.book_keeping_tool.balance_sheet.BalanceSheetUpdatingService;
import be.jonasboon.book_keeping_tool.mapper.TransactionMapper;
import be.jonasboon.book_keeping_tool.model.Transaction;
import be.jonasboon.book_keeping_tool.persistence.entity.TransactionEntity;
import be.jonasboon.book_keeping_tool.persistence.repository.TransactionRepository;
import be.jonasboon.book_keeping_tool.utils.FileReaderUtil;
import be.jonasboon.book_keeping_tool.utils.mapper.CSVObject;
import be.jonasboon.book_keeping_tool.utils.mapper.TransactionCSVMapper;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static be.jonasboon.book_keeping_tool.service.transaction.TransactionValidator.validateAllHaveCostCenters;

@Slf4j
@Service
public final class TransactionService {
    private final TransactionRepository transactionRepository;
    private final BalanceSheetUpdater balanceSheetUpdatingService;

    private TransactionService(TransactionRepository transactionRepository, BalanceSheetUpdatingService balanceSheetUpdatingService) {
        this.transactionRepository = transactionRepository;
        this.balanceSheetUpdatingService = balanceSheetUpdatingService;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(TransactionMapper::from)
                .collect(Collectors.toList());
    }

    public List<Transaction> process(CSVReader csvReader) {
        transactionRepository.deleteAll();
        FileReaderUtil
                .convert(new TransactionCSVMapper(), csvReader)
                .forEach(this::save);
        return transactionRepository.findAll().stream().map(TransactionMapper::from).toList();
    }

    public void save(CSVObject transactions) {
        transactionRepository.save(
                TransactionMapper.from(transactions)
        );
    }

    public List<Transaction> loadAssigned(List<Transaction> transactions) {
        List<TransactionEntity> mappedTransactions = transactions.stream().map(TransactionMapper::from).toList();
        transactionRepository.saveAll(mappedTransactions);
        return transactionRepository.findAll().stream().map(TransactionMapper::from).toList();
    }

    public void saveToFile() throws TransactionValidator.ValidationException {
        List<TransactionEntity> transactions = transactionRepository.findAll();
        validateAllHaveCostCenters(transactions);
        transactions.stream().map(TransactionMapper::from)
                .forEach(balanceSheetUpdatingService::processTransactionToAccountingFile);
    }
}
