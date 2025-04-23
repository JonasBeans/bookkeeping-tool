package be.jonasboon.book_keeping_tool.service;

import be.jonasboon.book_keeping_tool.mapper.TransactionMapper;
import be.jonasboon.book_keeping_tool.model.Transaction;
import be.jonasboon.book_keeping_tool.persistence.repository.TransactionRepository;
import be.jonasboon.book_keeping_tool.utils.FileReaderUtil;
import be.jonasboon.book_keeping_tool.utils.mapper.CSVObject;
import be.jonasboon.book_keeping_tool.utils.mapper.TransactionCSVMapper;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.actuate.metrics.data.DefaultRepositoryTagsProvider;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public final class TransactionService {
    private final CostCenterService costCenterService;
    private final TransactionRepository transactionRepository;
    private final DefaultRepositoryTagsProvider repositoryTagsProvider;

    private TransactionService(CostCenterService costCenterService, TransactionRepository transactionRepository, DefaultRepositoryTagsProvider repositoryTagsProvider) {
        this.costCenterService = costCenterService;
        this.transactionRepository = transactionRepository;
        this.repositoryTagsProvider = repositoryTagsProvider;
    }

    public List<Transaction> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(TransactionMapper::from)
                .collect(Collectors.toList());
    }

    public String process(CSVReader csvReader) {
        FileReaderUtil
                .convert( new TransactionCSVMapper(), csvReader)
                .forEach(this::save);
        return "File uploaded!";
    }

    public void save(CSVObject transactions) {
        transactionRepository.save(
                TransactionMapper.from(transactions)
        );
    }

}
