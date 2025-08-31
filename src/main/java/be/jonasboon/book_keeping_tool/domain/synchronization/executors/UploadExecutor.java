package be.jonasboon.book_keeping_tool.domain.synchronization.executors;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Slf4j
public class UploadExecutor {

    public static void execute(InputStream inputStream, List<String> allowedFileNames) {
        try (
                ZipInputStream zis = new ZipInputStream(inputStream)) {
            ZipEntry entry;
            Path targetDir = FileUtils.getTempDirectory().toPath();
            // Ensure the target directory exists
            Files.createDirectories(targetDir);

            while ((entry = zis.getNextEntry()) != null) {
                if (!allowedFileNames.contains(entry.getName())) {
                    throw new RuntimeException("Invalid file name: " + entry.getName());
                }
                Path filePath = targetDir.resolve(entry.getName());
                if (entry.isDirectory()) {
                    continue;
                } else {
                    try (OutputStream os = Files.newOutputStream(filePath)) {
                        log.info("Saving file to {}", filePath);
                        zis.transferTo(os);
                    }
                }
                zis.closeEntry();
            }
            log.info("Successfully saved backup files");
        } catch (
                IOException e) {
            throw new RuntimeException(e);
        }
    }

}
