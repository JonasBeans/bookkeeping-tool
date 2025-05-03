package be.jonasboon.book_keeping_tool.controllers;

import be.jonasboon.book_keeping_tool.workbook.BalanceSheetSaver;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/balance-sheet")
@AllArgsConstructor
@RestController
public class BalanceSheetController {

    private final BalanceSheetSaver balanceSheetSaver;

    @PostMapping(value = "/upload")
    public ResponseEntity<String> uploadTransactions(@RequestParam("file") MultipartFile file) {
        try {
            balanceSheetSaver.saveBalanceSheet(file);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("File saving failed: " + e.getMessage());
        }
        return ResponseEntity.ok().body("Upload successful");
    }
}
