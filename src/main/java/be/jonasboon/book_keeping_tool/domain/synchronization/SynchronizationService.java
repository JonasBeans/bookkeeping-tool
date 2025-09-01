package be.jonasboon.book_keeping_tool.domain.synchronization;

import be.jonasboon.book_keeping_tool.domain.synchronization.executors.BackupExecutor;
import be.jonasboon.book_keeping_tool.domain.synchronization.executors.DownloadExecutor;
import be.jonasboon.book_keeping_tool.domain.synchronization.executors.UploadExecutor;
import be.jonasboon.book_keeping_tool.domain.synchronization.executors.restore.EntityHandler;
import be.jonasboon.book_keeping_tool.domain.synchronization.executors.restore.RestoreExecutor;
import jakarta.persistence.EntityManager;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@Service
@Transactional
@AllArgsConstructor
public class SynchronizationService {

    private final BackupExecutor backupExecutor;
    private final RestoreExecutor restoreExecutor;
    private final EntityManager entityManager;


    @Async
    public <I> void make(String fileName, JpaRepository<I, ?> repository) {
        log.info("Received backup request for {}", fileName);
        File tempDirectory = FileUtils.getTempDirectory();
        Path filePath = tempDirectory.toPath().resolve(fileName);
        log.info("Making backup file on {}", filePath);
        backupExecutor.execute(filePath.toFile(), repository.findAll());
        log.info("Successfully made backup {}", filePath);
    }

    @Async
    public <I> void restore(String fileName, JpaRepository<I, ?> repository, EntityHandler entityHandler, ParameterizedTypeReference<I> reference) {
        log.info("Received restore request for {}", fileName);
        File tempDirectory = FileUtils.getTempDirectory();
        Path filePath = tempDirectory.toPath().resolve(fileName);
        log.info("Restoring from file on {}", filePath);
        restoreExecutor.execute(filePath.toFile(), repository, entityHandler, reference);
        log.info("Successfully restored {}", filePath);
    }

    public void downloadBackup(HttpServletResponse response, List<String> fileNames) {
        log.info("Preparing files for downloading backup");
        log.info("Downloading backup files");
        DownloadExecutor.execute(response, fileNames);
        log.info("Successfully prepared files for downloading backup");
    }

    public void uploadBackup(InputStream inputStream, List<String> allowedFileNames) {
        log.info("Preparing files for uploading backup");
        UploadExecutor.execute(inputStream, allowedFileNames);
        log.info("Successfully saved uploaded backup files");
    }

    @Transactional
    protected void clearAllTables() {
        log.info("Clearing all tables before restoring backup");
        entityManager.createNativeQuery("SET session_replication_role = replica").executeUpdate();
        @SuppressWarnings("unchecked")
        List<String> tables = entityManager.createNativeQuery(
                """
                SELECT table_name 
                FROM information_schema.tables 
                WHERE 
                    table_schema = 'public' AND 
                    table_catalog = current_database() AND
                    table_name NOT IN ('databasechangelog', 'databasechangeloglock')
                """
        ).getResultList();
        if (!tables.isEmpty()) {
            String joinedTables = String.join(", ", tables.stream().map(t -> "\"" + t + "\"").toList());
            entityManager.createNativeQuery("TRUNCATE TABLE " + joinedTables + " CASCADE").executeUpdate();
        }
        entityManager.createNativeQuery("SET session_replication_role = origin").executeUpdate();
        log.info("All tables cleared");
    }
}
