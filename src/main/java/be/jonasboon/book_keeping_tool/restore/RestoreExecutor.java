package be.jonasboon.book_keeping_tool.restore;

import be.jonasboon.book_keeping_tool.restore.common.RestoreEntityMapper;
import be.jonasboon.book_keeping_tool.utils.CSVFileReaderUtil;
import be.jonasboon.book_keeping_tool.utils.mapper.CSVFileMapper;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class RestoreExecutor {

    public <I> void execute(File file, CSVFileMapper csvFileMapper, RestoreEntityMapper<I> mapper, MongoRepository<I, ?> repository) {
        repository.deleteAll();
        repository.saveAll(CSVFileReaderUtil.convert(csvFileMapper, CSVFileReaderUtil.consume(file), false)
                .stream()
                .map(mapper::map)
                .toList()
        );
    }
}
