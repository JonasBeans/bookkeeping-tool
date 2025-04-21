package be.jonasboon.book_keeping_tool.mapper;

import be.jonasboon.book_keeping_tool.DTO.CostCenterDTO;
import be.jonasboon.book_keeping_tool.model.CostCenter;
import lombok.experimental.UtilityClass;

import static be.jonasboon.book_keeping_tool.constants.CostCenterConstants.*;

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
