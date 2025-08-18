package be.jonasboon.book_keeping_tool.backup.common;


import be.jonasboon.book_keeping_tool.utils.FileWriterUtil;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.Collection;

@Component
public class BackupExecutor {

    public <I, O extends BackupModel> void execute(File filePath, RestoreMapper<I, O> mapper, Collection<I> models) {
        String[] objectToBackup = models.stream()
                .map(mapper::of)
                .map(BackupModel::toBackupString)
                .toArray(String[]::new);
        FileWriterUtil.writeToFile(filePath.getPath(), objectToBackup);
    }

}
