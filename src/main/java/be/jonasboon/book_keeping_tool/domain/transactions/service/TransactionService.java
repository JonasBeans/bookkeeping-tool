package be.jonasboon.book_keeping_tool.domain.transactions.service;

import be.jonasboon.book_keeping_tool.domain.cost_centers.service.CostCenterService;
import be.jonasboon.book_keeping_tool.domain.transactions.DTO.TransactionDTO;
import be.jonasboon.book_keeping_tool.domain.transactions.mapper.TransactionCSVMapper;
import be.jonasboon.book_keeping_tool.domain.transactions.mapper.TransactionMapper;
import be.jonasboon.book_keeping_tool.domain.transactions.repository.TransactionRepository;
import be.jonasboon.book_keeping_tool.utils.CSVFileReaderUtil;
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

    public List<TransactionDTO> processTransactionCSVUpload(CSVReader csvReader) {
        transactionRepository.deleteAll(); entityManager.flush();
        costCenterService.resetAllTotalAmounts();
        transactionRepository.saveAll(
                CSVFileReaderUtil
                        .convert(new TransactionCSVMapper(), csvReader)
                        .stream()
                        .map(transactionMapper::map)
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
