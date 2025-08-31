package be.jonasboon.book_keeping_tool.domain.synchronization;

import be.jonasboon.book_keeping_tool.domain.balance_posts.entity.BalancePost;
import be.jonasboon.book_keeping_tool.domain.balance_posts.repository.BalanceRepository;
import be.jonasboon.book_keeping_tool.domain.cost_centers.entity.CostCenter;
import be.jonasboon.book_keeping_tool.domain.cost_centers.repository.CostCenterRepository;
import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;
import be.jonasboon.book_keeping_tool.domain.transactions.repository.TransactionRepository;
import be.jonasboon.book_keeping_tool.utils.ExceptionUtils;
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
@RequestMapping("synchronization")
public class SynchronizationController {

    private TransactionRepository transactionRepository;
    private CostCenterRepository costCenterRepository;
    private BalanceRepository balanceRepository;
    private SynchronizationService synchronizationService;

    @PostMapping("/backup")
    public ResponseEntity<String> createBackup() {
        synchronizationService.make("transactions_backup.bkt", transactionRepository);
        synchronizationService.make("cost_centers_backup.bkt", costCenterRepository);
        synchronizationService.make("balance_posts.bkt", balanceRepository);
        return ResponseEntity.ok("Backup successfully requested");
    }

    @PutMapping("/restore")
    public ResponseEntity<String> restoreFromBackup() {
        synchronizationService.restore("transactions_backup.bkt", transactionRepository, ParameterizedTypeReference.forType(Transaction.class));
        synchronizationService.restore("cost_centers_backup.bkt", costCenterRepository, ParameterizedTypeReference.forType(CostCenter.class));
        synchronizationService.restore("balance_posts.bkt", balanceRepository, ParameterizedTypeReference.forType(BalancePost.class));
        return ResponseEntity.ok("Backup successfully restored");
    }

    @GetMapping("/download")
    public void downloadBackup(HttpServletResponse response) {
        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=backup.zip");
        synchronizationService.downloadBackup(response, "transactions_backup.bkt", "cost_centers_backup.bkt", "balance_posts.bkt");
    }

    @PostMapping(consumes = "multipart/form-data", path = "/upload")
    public ResponseEntity<String> uploadBackup(@RequestParam("file") MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            synchronizationService.uploadBackup(inputStream, "transactions_backup.bkt", "cost_centers_backup.bkt", "balance_posts.bkt");
        } catch (IOException e) {
            return ExceptionUtils.sendExceptionResponse(e, 500);
        }
        return ResponseEntity.ok().body("Successfully uploaded backup file.");
    }
}
