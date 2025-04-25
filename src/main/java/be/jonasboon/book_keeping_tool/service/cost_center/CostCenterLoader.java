package be.jonasboon.book_keeping_tool.service.cost_center;

import be.jonasboon.book_keeping_tool.constants.FileConstansts;
import be.jonasboon.book_keeping_tool.mapper.CostCenterMapper;
import be.jonasboon.book_keeping_tool.persistence.entity.CostCenterEntity;
import be.jonasboon.book_keeping_tool.persistence.repository.CostCenterRepository;
import lombok.AllArgsConstructor;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
@AllArgsConstructor
public class CostCenterLoader {

    private final CostCenterRepository costCenterRepository;

    @EventListener
    private void loadCostCenters(ContextRefreshedEvent event) {
        costCenterRepository.deleteAll();
        List<CostCenterEntity> costCenters = readCostCenterFile().stream()
                .map(CostCenterMapper::from)
                .map(CostCenterMapper::toEntity)
                .toList();
        costCenterRepository.saveAll(costCenters);
    }

    private static List<String> readCostCenterFile(){
        List<String> costCenterLines = new ArrayList<>();

        String line = "";
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(getCostCenterFile()))) {
            while((line = reader.readLine()) != null){
                costCenterLines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return costCenterLines;
    }

    private static InputStream getCostCenterFile(){
        ClassLoader classLoader = CostCenterService.class.getClassLoader();
        return classLoader.getResourceAsStream(FileConstansts.COST_CENTERS_LOCATION);
    }
}
