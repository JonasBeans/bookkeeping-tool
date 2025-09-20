package be.jonasboon.book_keeping_tool.domain.transactions.controller;

import be.jonasboon.book_keeping_tool.domain.transactions.DTO.TransactionDTO;
import be.jonasboon.book_keeping_tool.domain.transactions.service.TransactionService;
import lombok.AllArgsConstructor;
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
    public List<TransactionDTO> uploadTransactionsCSV(@RequestParam("file") MultipartFile file) {
        return transactionService.processTransactionUpload(file);
    }

    @PutMapping(value = "/assigned")
    public List<TransactionDTO> assignTransactions(@RequestBody List<TransactionDTO> transactions) {
        return transactionService.loadAssigned(transactions);
    }

}
