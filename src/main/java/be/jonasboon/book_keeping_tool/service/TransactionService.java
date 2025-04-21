package be.jonasboon.book_keeping_tool.service;

import be.jonasboon.book_keeping_tool.persistence.repository.TransactionRepository;
import be.jonasboon.book_keeping_tool.utils.FileReaderUtil;
import be.jonasboon.book_keeping_tool.utils.mapper.CSVObject;
import be.jonasboon.book_keeping_tool.utils.mapper.TransactionCSVMapper;
import com.opencsv.CSVReader;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public final class TransactionService {
    private final CostCenterService costCenterService;
    private final TransactionRepository transactionRepository;

    private TransactionService(CostCenterService costCenterService, TransactionRepository transactionRepository) {
        this.costCenterService = costCenterService;
        this.transactionRepository = transactionRepository;
    }

    public List<CSVObject> process(CSVReader csvReader) {
        return FileReaderUtil.convert( new TransactionCSVMapper(), csvReader);
    }

    public void save(List<CSVObject> transactions) {
        throw new RuntimeException("Not yet implemented"); //todo save transactions
    }

}
