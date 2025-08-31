package be.jonasboon.book_keeping_tool.domain.synchronization.executors;

import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.yaml.snakeyaml.util.ArrayUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class DownloadExecutor {

    public static void execute(HttpServletResponse response, String... fileNames) {
        List<String> fileNamesList = ArrayUtils.toUnmodifiableList(fileNames);

        // Create ZIP output stream
        try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {
            fileNamesList.forEach(fileName -> {
                addFileToZip(fileName, zipOut);
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void addFileToZip(String fileName, ZipOutputStream zipOut) {
        Path tempDirectory = FileUtils.getTempDirectory().toPath();
        File file = tempDirectory.resolve(fileName).toFile();

        try (FileInputStream fis = new FileInputStream(file)) {
            zipOut.putNextEntry(new ZipEntry(file.getName()));
            log.info("Adding file {} to ZIP", file.getName());
            fis.transferTo(zipOut);
            zipOut.closeEntry();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
