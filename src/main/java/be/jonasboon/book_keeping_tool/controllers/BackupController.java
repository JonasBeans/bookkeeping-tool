package be.jonasboon.book_keeping_tool.controllers;

import be.jonasboon.book_keeping_tool.backup.cost_center.mapper.CostCenterMapper;
import be.jonasboon.book_keeping_tool.backup.service.BackupService;
import be.jonasboon.book_keeping_tool.backup.transaction.mapper.TransactionMapper;
import be.jonasboon.book_keeping_tool.persistence.repository.CostCenterRepository;
import be.jonasboon.book_keeping_tool.persistence.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
