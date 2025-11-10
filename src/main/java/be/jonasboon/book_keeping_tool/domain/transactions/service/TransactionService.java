package be.jonasboon.book_keeping_tool.domain.transactions.service;

import be.jonasboon.book_keeping_tool.domain.cost_centers.service.CostCenterService;
import be.jonasboon.book_keeping_tool.domain.transactions.DTO.TransactionDTO;
import be.jonasboon.book_keeping_tool.domain.transactions.mapper.TransactionMapper;
import be.jonasboon.book_keeping_tool.domain.transactions.processor.TransactionFileStrategy;
import be.jonasboon.book_keeping_tool.domain.transactions.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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
    private final TransactionFileStrategy transactionFileStrategy;
    private final EntityManager entityManager;

    public List<TransactionDTO> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(transactionMapper::from)
                .collect(Collectors.toList());
    }

    public List<TransactionDTO> processTransactionUpload(MultipartFile file) {
        transactionRepository.deleteAll(); entityManager.flush();
        costCenterService.resetAllTotalAmounts();
        transactionRepository.saveAll(transactionFileStrategy.process(file, "xlsx"));
        return transactionRepository.findAll().stream().map(transactionMapper::from).toList();
    }

    public List<TransactionDTO> loadAssigned(List<TransactionDTO> transactions) {
        transactionRepository.saveAll(
                transactions.stream()
                        .filter(TransactionDTO::hasNoEmptyField)
                        .map(transactionMapper::from)
                        .toList()
        );
        entityManager.flush();
        costCenterService.updateTotalAmounts(transactions);
        return transactionRepository.findAll().stream().map(transactionMapper::from).toList();
    }

}
