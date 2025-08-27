package be.jonasboon.book_keeping_tool.restore;

import be.jonasboon.book_keeping_tool.restore.common.RestoreEntityMapper;
import be.jonasboon.book_keeping_tool.utils.CSVFileReaderUtil;
import be.jonasboon.book_keeping_tool.utils.FileReaderUtil;
import be.jonasboon.book_keeping_tool.utils.mapper.CSVFileMapper;
import com.google.gson.Gson;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@AllArgsConstructor
public class RestoreExecutor {

    private final Gson gson;

    public <I> void execute(File file, CSVFileMapper csvFileMapper, RestoreEntityMapper<I> mapper, MongoRepository<I, ?> repository) {
        repository.deleteAll();
        repository.saveAll(CSVFileReaderUtil.convert(csvFileMapper, CSVFileReaderUtil.consume(file), false)
                .stream()
                .map(mapper::map)
                .toList()
        );
    }

    public <I> void executeJson(File file, MongoRepository<I, ?> repository, ParameterizedTypeReference<I> reference) {
        repository.deleteAll();
        repository.saveAll(
                FileReaderUtil.convertFileToType(
                        file.getPath(),
                        gson,
                        reference
                )
        );
    }
}

