package be.javabeans.mapper;

import be.javabeans.DTO.CostCenterDTO;
import be.javabeans.model.CostCenter;
import lombok.experimental.UtilityClass;

import static be.javabeans.constants.CostCenterConstants.*;

@UtilityClass
public class CostCenterMapper {

    public static CostCenter toModel(CostCenterDTO dto){
        return CostCenter.builder()
                .withCostCenter(dto.getCostCenter())
                .withIsCost(dto.getIsCost())
                .withIndex(dto.getIndex())
                .build();
    }
    public static CostCenterDTO fromFileToDTO(String costCenterLine){
        String[] readCostCenter = costCenterLine.split(",");
        return new CostCenterDTO(
                readCostCenter[COST_CENTER_TITLE],
                readCostCenter[COST_OR_INCOME],
                readCostCenter[INDEX]);
    }

}
