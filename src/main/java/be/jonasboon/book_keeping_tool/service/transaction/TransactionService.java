package be.jonasboon.book_keeping_tool.service.transaction;

import be.jonasboon.book_keeping_tool.mapper.TransactionEntityMapper;
import be.jonasboon.book_keeping_tool.mapper.TransactionMapper;
import be.jonasboon.book_keeping_tool.model.TransactionDTO;
import be.jonasboon.book_keeping_tool.persistence.entity.Transaction;
import be.jonasboon.book_keeping_tool.persistence.repository.TransactionRepository;
import be.jonasboon.book_keeping_tool.service.cost_center.CostCenterService;
import be.jonasboon.book_keeping_tool.utils.CSVFileReaderUtil;
import be.jonasboon.book_keeping_tool.utils.mapper.TransactionCSVMapper;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static be.jonasboon.book_keeping_tool.service.transaction.TransactionValidator.validateAllHaveCostCenters;

@Slf4j
@Service
public class TransactionService {

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final CostCenterService costCenterService;

    private TransactionService(TransactionMapper transactionMapper, TransactionRepository transactionRepository, CostCenterService costCenterService) {
        this.transactionMapper = transactionMapper;
        this.transactionRepository = transactionRepository;
        this.costCenterService = costCenterService;
    }

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::from)
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> process(CSVReader csvReader) {
        TransactionEntityMapper entityMapper = new TransactionEntityMapper();
        transactionRepository.deleteAll();
        transactionRepository.saveAll(
                CSVFileReaderUtil
                        .convert(new TransactionCSVMapper(), csvReader)
                        .stream()
                        .map(entityMapper::map)
                        .toList()
        );
        return transactionRepository.findAll().stream().map(transactionMapper::from).toList();
    }

    public List<TransactionDTO> loadAssigned(List<TransactionDTO> transactions) {
        transactionRepository.saveAll(transactions.stream().map(transactionMapper::from).toList());
        costCenterService.updateTotalAmounts(transactions);
        return transactionRepository.findAll().stream().map(transactionMapper::from).toList();
    }

    public void saveToFile() throws TransactionValidator.ValidationException {
        List<Transaction> transactions = transactionRepository.findAll();
        validateAllHaveCostCenters(transactions);
    }
}
