package be.javabeans.service;

import be.javabeans.constants.FileConstansts;
import be.javabeans.mapper.CostCenterMapper;
import be.javabeans.model.CostCenter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public final class CostCenterService {
    private static CostCenterService instance;
    private final List<CostCenter> costCenters;

    private CostCenterService(){
        costCenters = readCostCenterFile().stream()
                .map(CostCenterMapper::fromFileToDTO)
                .map(CostCenterMapper::toModel)
                .collect(Collectors.toList());
    }

    public static CostCenterService getInstance() {
        if (instance == null) {
            instance = new CostCenterService();
        }
        return instance;
    }
    public List<CostCenter> getIncomeCostCenters(){
        return this.costCenters.stream().filter(costCenter -> !costCenter.getIsCost()).collect(Collectors.toList());
    }
    public List<CostCenter> getCostCostCenters(){
        return this.costCenters.stream().filter(CostCenter::getIsCost).collect(Collectors.toList());
    }
    public List<Integer> getCostCenterIndexes(){
        return this.costCenters.stream().map(CostCenter::getIndex).collect(Collectors.toList());
    }

    public List<CostCenter> getCostCenters(){
        return this.costCenters;
    }

    private List<String> readCostCenterFile(){
        List<String> costCenterLines = new ArrayList<>();
        File file = new File(FileConstansts.COST_CENTERS_LOCATION);

        String line = "";
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while((line = reader.readLine()) != null){
                costCenterLines.add(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return costCenterLines;
    }
}
