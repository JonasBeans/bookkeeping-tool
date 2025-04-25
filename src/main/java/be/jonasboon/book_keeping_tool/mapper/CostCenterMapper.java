package be.jonasboon.book_keeping_tool.mapper;

import be.jonasboon.book_keeping_tool.DTO.CostCenterDTO;
import be.jonasboon.book_keeping_tool.model.CostCenter;
import be.jonasboon.book_keeping_tool.persistence.entity.CostCenterEntity;
import lombok.experimental.UtilityClass;

import static be.jonasboon.book_keeping_tool.constants.CostCenterConstants.*;

@UtilityClass
public class CostCenterMapper {

    public static CostCenter from(CostCenterDTO dto){
        return CostCenter.builder()
                .withCostCenter(dto.getCostCenter())
                .withIsCost(dto.getIsCost())
                .withIndex(dto.getIndex())
                .build();
    }
    public static CostCenterDTO from(String costCenterLine){
        String[] readCostCenter = costCenterLine.split(",");
        return new CostCenterDTO(
                readCostCenter[COST_CENTER_TITLE],
                readCostCenter[COST_OR_INCOME],
                readCostCenter[INDEX]);
    }

    public static CostCenterEntity toEntity(CostCenterDTO dto){
        return CostCenterEntity.builder()
                .withCostCenter(dto.getCostCenter())
                .withIndex(dto.getIndex())
                .withIsCost(dto.getIsCost())
                .build();
    }

}
