package be.jonasboon.book_keeping_tool.backup.service;

import be.jonasboon.book_keeping_tool.backup.common.BackupExecutor;
import be.jonasboon.book_keeping_tool.backup.common.BackupModel;
import be.jonasboon.book_keeping_tool.backup.common.RestoreMapper;
import be.jonasboon.book_keeping_tool.restore.RestoreExecutor;
import be.jonasboon.book_keeping_tool.restore.common.RestoreEntityMapper;
import be.jonasboon.book_keeping_tool.utils.mapper.CSVFileMapper;
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
    private final RestoreExecutor restoreExecutor;

    @Async
    public <I, O extends BackupModel> void make(String fileName, RestoreMapper<I,O> mapper, MongoRepository<I, ?> repository) {
        log.info("Received backup request for {}", fileName);
        File tempDirectory = FileUtils.getTempDirectory();
        Path filePath = tempDirectory.toPath().resolve(fileName);
        log.info("Making backup file on {}", filePath);
        backupExecutor.execute(filePath.toFile(), mapper , repository.findAll());
        log.info("Successfully made backup {}", filePath);
    }

    public <I> void restore(String fileName, CSVFileMapper csvFileMapper, RestoreEntityMapper<I> mapper, MongoRepository<I, ?> repository) {
        log.info("Received restore request for {}", fileName);
        File tempDirectory = FileUtils.getTempDirectory();
        Path filePath = tempDirectory.toPath().resolve(fileName);
        log.info("Restoring from file on {}", filePath);
        restoreExecutor.execute(filePath.toFile(), csvFileMapper, mapper, repository);
        log.info("Successfully restored {}", filePath);
    }
}
