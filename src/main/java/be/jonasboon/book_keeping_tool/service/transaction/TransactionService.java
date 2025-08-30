package be.jonasboon.book_keeping_tool.service.transaction;

import be.jonasboon.book_keeping_tool.mapper.TransactionEntityMapper;
import be.jonasboon.book_keeping_tool.mapper.TransactionMapper;
import be.jonasboon.book_keeping_tool.model.TransactionDTO;
import be.jonasboon.book_keeping_tool.persistence.repository.TransactionRepository;
import be.jonasboon.book_keeping_tool.service.cost_center.CostCenterService;
import be.jonasboon.book_keeping_tool.utils.CSVFileReaderUtil;
import be.jonasboon.book_keeping_tool.utils.mapper.TransactionCSVMapper;
import com.opencsv.CSVReader;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class TransactionService {

    private final TransactionMapper transactionMapper;
    private final TransactionRepository transactionRepository;
    private final CostCenterService costCenterService;
    private final EntityManager entityManager;

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
        transactionRepository.saveAll(transactions.stream().map(transactionMapper::from).toList()); entityManager.flush();
        costCenterService.updateTotalAmounts(transactions);
        return transactionRepository.findAll().stream().map(transactionMapper::from).toList();
    }

}
