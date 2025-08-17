package be.jonasboon.book_keeping_tool.controllers;

import be.jonasboon.book_keeping_tool.model.TransactionDTO;
import be.jonasboon.book_keeping_tool.service.transaction.TransactionService;
import be.jonasboon.book_keeping_tool.service.transaction.TransactionValidator;
import be.jonasboon.book_keeping_tool.utils.FileReaderUtil;
import com.opencsv.CSVReader;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequestMapping("transactions")
@AllArgsConstructor
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @GetMapping("/all")
    public List<TransactionDTO> getAllTransactions() {
        return transactionService.getAllTransactions();
    }

    @PostMapping(value = "/upload/transaction-file")
    public List<TransactionDTO> uploadTransactions(@RequestParam("file") MultipartFile file) {
        CSVReader reader = FileReaderUtil.consume(file);
        return transactionService.process(reader);
    }

    @PutMapping(value = "/assigned")
    public List<TransactionDTO> assignTransactions(@RequestBody List<TransactionDTO> transactions) {
        return transactionService.loadAssigned(transactions);
    }

    @PutMapping(value = "/save-to-file")
    public ResponseEntity<String> saveTransactionToFile() {
        try {
            transactionService.saveToFile();
        } catch (TransactionValidator.ValidationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
        return ResponseEntity.ok().build();
    }
}
