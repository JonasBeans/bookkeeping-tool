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
    public List<TransactionDTO> getAllTransactions(@RequestParam(value = "bookYear", required = false) Integer bookYear,
                                                   @RequestParam(value = "bookMonth", required = false) Integer bookMonth) {
        return transactionService.getTransactionsForBookPeriod(bookYear, bookMonth);
    }

    @GetMapping("/bookyears")
    public List<Integer> getAvailableBookYears() {
        return transactionService.getAvailableBookYears();
    }

    @GetMapping("/bookmonths")
    public List<Integer> getAvailableBookMonths(@RequestParam(value = "bookYear", required = false) Integer bookYear) {
        return transactionService.getAvailableBookMonths(bookYear);
    }

    @DeleteMapping("/bookyears/{bookYear}")
    public void deleteTransactionsForBookYear(@PathVariable Integer bookYear) {
        transactionService.deleteTransactionsForBookYear(bookYear);
    }

    @PostMapping(value = "/upload/transaction-file")
    public List<TransactionDTO> uploadTransactionsCSV(@RequestParam("file") MultipartFile file) {
        return transactionService.processTransactionUpload(file);
    }

    @PutMapping(value = "/assigned")
    public List<TransactionDTO> assignTransactions(@RequestBody List<TransactionDTO> transactions,
                                                   @RequestParam(value = "bookYear", required = false) Integer bookYear,
                                                   @RequestParam(value = "bookMonth", required = false) Integer bookMonth) {
        return transactionService.loadAssigned(transactions, bookYear, bookMonth);
    }

}
