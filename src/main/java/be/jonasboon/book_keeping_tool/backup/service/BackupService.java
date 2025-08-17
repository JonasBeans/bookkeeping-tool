package be.jonasboon.book_keeping_tool.backup.service;

import be.jonasboon.book_keeping_tool.backup.common.BackupExecutor;
import be.jonasboon.book_keeping_tool.backup.common.BackupModel;
import be.jonasboon.book_keeping_tool.backup.common.BackupModelMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Path;

@Slf4j
@Service
@AllArgsConstructor
public class BackupService {

    private final BackupExecutor backupExecutor;

    @Async
    public <I, O extends BackupModel> void make(String fileName, BackupModelMapper<I,O> mapper, MongoRepository<I, ?> repository) {
        log.info("Received backup request for {}", fileName);
        File tempDirectory = FileUtils.getTempDirectory();
        Path filePath = tempDirectory.toPath().resolve(fileName);
        log.info("Making backup file on {}", filePath);
        backupExecutor.executeBackup(filePath.toFile(), mapper , repository.findAll());
        log.info("Successfully made backup {}", filePath);
    }
}
