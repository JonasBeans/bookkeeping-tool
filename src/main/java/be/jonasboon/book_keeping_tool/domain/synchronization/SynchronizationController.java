package be.jonasboon.book_keeping_tool.domain.synchronization;

import be.jonasboon.book_keeping_tool.utils.ExceptionUtils;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

@RestController
@AllArgsConstructor
@RequestMapping("synchronization")
public class SynchronizationController {

    private SynchronizationFileProperties synchronizationFileProperties;
    private SynchronizationService synchronizationService;

    @PostMapping("/backup")
    public ResponseEntity<String> createBackup() {
        synchronizationFileProperties.PROPERTIES.forEach(property ->
                synchronizationService.make(property.fileName(), property.repository())
        );
        return ResponseEntity.ok("Backup successfully requested");
    }

    @PutMapping("/restore")
    public ResponseEntity<String> restoreFromBackup() {
        synchronizationService.clearAllTables();
        synchronizationFileProperties.PROPERTIES.forEach(property ->
                synchronizationService.restore(property.fileName(), property.repository(), property.entityHandler(), property.typeReference())
        );
        return ResponseEntity.ok("Backup successfully restored");
    }

    @GetMapping("/download")
    public void downloadBackup(HttpServletResponse response) {
        response.setHeader("Content-Type", "application/octet-stream");
        response.setHeader("Content-Disposition", "attachment; filename=backup.zip");
        synchronizationService.downloadBackup(response, synchronizationFileProperties.getAllFileNames());
    }

    @PostMapping(consumes = "multipart/form-data", path = "/upload")
    public ResponseEntity<String> uploadBackup(@RequestParam("file") MultipartFile file) {
        try (InputStream inputStream = file.getInputStream()) {
            synchronizationService.uploadBackup(inputStream, synchronizationFileProperties.getAllFileNames());
        } catch (IOException e) {
            return ExceptionUtils.sendExceptionResponse(e, 500);
        }
        return ResponseEntity.ok().body("Successfully uploaded backup file.");
    }
}
