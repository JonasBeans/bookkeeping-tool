package be.jonasboon.book_keeping_tool.controllers;

import be.jonasboon.book_keeping_tool.backup.service.BackupService;
import be.jonasboon.book_keeping_tool.persistence.entity.BalancePostEntity;
import be.jonasboon.book_keeping_tool.persistence.entity.CostCenterEntity;
import be.jonasboon.book_keeping_tool.persistence.entity.TransactionEntity;
import be.jonasboon.book_keeping_tool.persistence.repository.BalanceRepository;
import be.jonasboon.book_keeping_tool.persistence.repository.CostCenterRepository;
import be.jonasboon.book_keeping_tool.persistence.repository.TransactionRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@AllArgsConstructor
@RequestMapping("backup")
public class BackupController {

    private TransactionRepository transactionRepository;
    private CostCenterRepository costCenterRepository;
    private BalanceRepository balanceRepository;
    private BackupService backupService;

    @PostMapping("/request")
    public ResponseEntity<String> createBackup() {
        backupService.make("transactions_backup.bkt", transactionRepository);
        backupService.make("cost_centers_backup.bkt", costCenterRepository);
        backupService.make("balance_posts.bkt", balanceRepository);
        return ResponseEntity.ok("Backup successfully requested");
    }

    @PutMapping("/restore")
    public ResponseEntity<String> restoreFromBackup() {
        backupService.restore("transactions_backup.bkt", transactionRepository, ParameterizedTypeReference.forType(TransactionEntity.class));
        backupService.restore("cost_centers_backup.bkt", costCenterRepository, ParameterizedTypeReference.forType(CostCenterEntity.class));
        backupService.restore("balance_posts.bkt", balanceRepository, ParameterizedTypeReference.forType(BalancePostEntity.class));
        return ResponseEntity.ok("Backup successfully restored");
    }

    @GetMapping("/download")
    public void downloadBackup(HttpServletResponse response) {
        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=backup.zip");
        backupService.downloadBackup(response, "transactions_backup.bkt", "cost_centers_backup.bkt", "balance_posts.bkt");
    }

    @PostMapping(consumes = "multipart/form-data", path = "/upload")
    public ResponseEntity<String> uploadBackup(@RequestParam("file") MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            backupService.uploadBackup(inputStream, "transactions_backup.bkt", "cost_centers_backup.bkt", "balance_posts.bkt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body("Successfully uploaded backup file.");
    }
}
