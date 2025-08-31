package be.jonasboon.book_keeping_tool.domain.synchronization.executors;

import be.jonasboon.book_keeping_tool.utils.FileReaderUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
@AllArgsConstructor
public class RestoreExecutor {

    private final ObjectMapper objectMapper;

    public <I> void executeJson(File file, JpaRepository<I, ?> repository, ParameterizedTypeReference<I> reference) {
        repository.deleteAll();
        repository.saveAll(
                FileReaderUtil.convertFileToType(
                        file.getPath(),
                        objectMapper,
                        reference
                )
        );
    }
}
