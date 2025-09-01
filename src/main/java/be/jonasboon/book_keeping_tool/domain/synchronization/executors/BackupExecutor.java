package be.jonasboon.book_keeping_tool.domain.synchronization.executors;


import be.jonasboon.book_keeping_tool.utils.FileWriterUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collection;

@Component
@AllArgsConstructor
public class BackupExecutor {

    private final ObjectMapper objectMapper;

    public <I> void execute(File filePath, Collection<I> models) {
        String[] items = models.stream()
                .map(model -> {
                    try {
                        return objectMapper.writeValueAsString(model);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .toArray(String[]::new);
        FileWriterUtil.writeToJsonFile(filePath.getPath(), items);
    }

}
