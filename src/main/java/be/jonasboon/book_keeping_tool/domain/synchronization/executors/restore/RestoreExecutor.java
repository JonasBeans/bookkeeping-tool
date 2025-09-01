package be.jonasboon.book_keeping_tool.domain.synchronization.executors.restore;

import be.jonasboon.book_keeping_tool.domain.cost_centers.repository.CostCenterRepository;
import be.jonasboon.book_keeping_tool.utils.FileReaderUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.io.File;
import java.util.List;

@Component
@AllArgsConstructor
public class RestoreExecutor {

    private final ObjectMapper objectMapper;
    private final CostCenterRepository costCenterRepository;

    public <I> void execute(
            File file,
            JpaRepository<I, ?> repository,
            EntityHandler entityHandler,
            ParameterizedTypeReference<I> reference
    ) {
        repository.deleteAll();
        List<I> entities = FileReaderUtil.convertFileToType(
                file.getPath(),
                objectMapper,
                reference
        ).stream().map(entityHandler::handle).toList();
        repository.saveAll(entities);
    }


}
