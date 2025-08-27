package be.jonasboon.book_keeping_tool.backup.common;


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

    public <I, O extends BackupModel> void execute(File filePath, RestoreMapper<I, O> mapper, Collection<I> models) {
        String[] objectToBackup = models.stream()
                .map(mapper::of)
                .map(BackupModel::toBackupString)
                .toArray(String[]::new);
        FileWriterUtil.writeToCSVFile(filePath.getPath(), objectToBackup);
    }

    public <I> void executeJson(File filePath, Collection<I> models) {
        String[] items = models.stream()
                .map(gson::toJson)
                .toArray(String[]::new);
        FileWriterUtil.writeToJsonFile(filePath.getPath(), items);
    }

}
