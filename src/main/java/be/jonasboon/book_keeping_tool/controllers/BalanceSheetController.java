package be.jonasboon.book_keeping_tool.controllers;

import be.jonasboon.book_keeping_tool.balance_sheet.BalanceSheetSaver;
import be.jonasboon.book_keeping_tool.balance_sheet.BalanceSheetSender;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RequestMapping("/balance-sheet")
@AllArgsConstructor
@RestController
public class BalanceSheetController {

    private final BalanceSheetSaver balanceSheetSaver;
    private final BalanceSheetSender balanceSheetSender;

    @GetMapping("/download")
    public ResponseEntity<byte[]> downloadBalanceSheet() {
        try {
            // Load file from resources folder (e.g., src/main/resources/balance-sheet.pdf)
            byte[] bytes = balanceSheetSender.downloadBalanceSheet();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(
                    MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
            );
            headers.setContentDispositionFormData("attachment", balanceSheetSaver.getBalanceSheetFileName());

            return new ResponseEntity<>(bytes, headers, HttpStatus.OK);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

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
