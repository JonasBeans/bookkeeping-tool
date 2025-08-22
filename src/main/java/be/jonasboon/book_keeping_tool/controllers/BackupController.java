package be.jonasboon.book_keeping_tool.controllers;

import be.jonasboon.book_keeping_tool.backup.cost_center.mapper.CostCenterMapper;
import be.jonasboon.book_keeping_tool.backup.service.BackupService;
import be.jonasboon.book_keeping_tool.backup.transaction.mapper.TransactionMapper;
import be.jonasboon.book_keeping_tool.persistence.repository.CostCenterRepository;
import be.jonasboon.book_keeping_tool.persistence.repository.TransactionRepository;
import be.jonasboon.book_keeping_tool.restore.cost_center.CSVCostCenterMapper;
import be.jonasboon.book_keeping_tool.restore.cost_center.CostCenterEntityMapper;
import be.jonasboon.book_keeping_tool.restore.transaction.TransactionCSVMapper;
import be.jonasboon.book_keeping_tool.restore.transaction.TransactionEntityMapper;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
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
    private BackupService backupService;

    @PostMapping("/request")
    public ResponseEntity<String> createBackup() {
        backupService.make("transactions_backup.bkt", TransactionMapper.INSTANCE, transactionRepository);
        backupService.make("cost_centers_backup.bkt", CostCenterMapper.INSTANCE, costCenterRepository);
        return ResponseEntity.ok("Backup successfully requested");
    }

    @PutMapping("/restore")
    public ResponseEntity<String> restoreFromBackup() {
        backupService.restore("transactions_backup.bkt", new TransactionCSVMapper(), new TransactionEntityMapper(), transactionRepository);
        backupService.restore("cost_centers_backup.bkt", new CSVCostCenterMapper(), new CostCenterEntityMapper(), costCenterRepository);
        return ResponseEntity.ok("Backup successfully restored");
    }

    @GetMapping("/download")
    public void downloadBackup(HttpServletResponse response) {
        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=backup.zip");
        backupService.downloadBackup(response, "transactions_backup.bkt", "cost_centers_backup.bkt");
    }

    @PostMapping(consumes = "multipart/form-data", path = "/upload")
    public ResponseEntity<String> uploadBackup(@RequestParam("file") MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            backupService.uploadBackup(inputStream, "transactions_backup.bkt", "cost_centers_backup.bkt");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return ResponseEntity.ok().body("Successfully uploaded backup file.");
    }
}
