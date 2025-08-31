package be.jonasboon.book_keeping_tool.domain.synchronization.executors;


import be.jonasboon.book_keeping_tool.utils.FileWriterUtil;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collection;

@Component
@AllArgsConstructor
public class BackupExecutor {

    private final Gson gson;

    public <I> void executeJson(File filePath, Collection<I> models) {
        String[] items = models.stream()
                .map(gson::toJson)
                .toArray(String[]::new);
        FileWriterUtil.writeToJsonFile(filePath.getPath(), items);
    }

}
