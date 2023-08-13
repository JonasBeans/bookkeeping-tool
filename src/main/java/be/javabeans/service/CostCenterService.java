package be.javabeans.service;

import be.javabeans.constants.FileConstansts;
import be.javabeans.exceptions.CostCenterNotFound;
import be.javabeans.mapper.CostCenterMapper;
import be.javabeans.model.CostCenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

    //From here varies methods to get desired cost centers
    public List<CostCenter> getIncomeCostCenters(){
        return this.costCenters.stream().filter(costCenter -> !costCenter.getIsCost()).collect(Collectors.toList());
    }
    public List<CostCenter> getCostCostCenters(){
        return this.costCenters.stream().filter(CostCenter::getIsCost).collect(Collectors.toList());
    }
    public List<Integer> getCostCenterIndexes(){
        return this.costCenters.stream().map(CostCenter::getIndex).collect(Collectors.toList());
    }

    public String findCostByIndex(Integer index) {
        return this.costCenters.stream()
                .filter(costCenter -> costCenter.getIndex().equals(index))
                .map(CostCenter::getCostCenter)
                .findFirst().orElseThrow(CostCenterNotFound::new);
    }

    public List<CostCenter> getCostCenters(){
        return this.costCenters;
    }

    //From here code for loading cost centers from the cost center file.
    private List<String> readCostCenterFile(){
        List<String> costCenterLines = new ArrayList<>();

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
