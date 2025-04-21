package be.jonasboon.book_keeping_tool.service;

import be.jonasboon.book_keeping_tool.constants.FileConstansts;
import be.jonasboon.book_keeping_tool.exceptions.CostCenterNotFound;
import be.jonasboon.book_keeping_tool.mapper.CostCenterMapper;
import be.jonasboon.book_keeping_tool.model.CostCenter;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CostCenterService {

    private static final List<CostCenter> costCenters;

    static {
        costCenters = readCostCenterFile().stream()
                .map(CostCenterMapper::fromFileToDTO)
                .map(CostCenterMapper::toModel)
                .collect(Collectors.toList());
    }

    public static List<CostCenter> getIncomeCostCenters(){
        return costCenters.stream().filter(costCenter -> !costCenter.getIsCost()).collect(Collectors.toList());
    }

    public static List<CostCenter> getCostCostCenters(){
        return costCenters.stream().filter(CostCenter::getIsCost).collect(Collectors.toList());
    }

    public static List<Integer> getCostCenterIndexes(){
        return costCenters.stream().map(CostCenter::getIndex).collect(Collectors.toList());
    }

    public String findCostByIndex(Integer index) {
        return costCenters.stream()
                .filter(costCenter -> costCenter.getIndex().equals(index))
                .map(CostCenter::getCostCenter)
                .findFirst().orElseThrow(CostCenterNotFound::new);
    }

    public List<CostCenter> getCostCenters(){
        return costCenters;
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
