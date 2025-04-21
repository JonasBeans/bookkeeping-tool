package be.jonasboon.book_keeping_tool.controllers;

import be.jonasboon.book_keeping_tool.service.TransactionService;
import be.jonasboon.book_keeping_tool.utils.FileReaderUtil;
import com.opencsv.CSVReader;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("transactions")
@AllArgsConstructor
@RestController
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping(value = "/upload/transaction-file")
    public String uploadTransactions(@RequestParam("file") MultipartFile file) {
        CSVReader reader = FileReaderUtil.supply(file);
        transactionService.process(reader);
        return "File uploaded!";
    }
}
