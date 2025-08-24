package be.jonasboon.book_keeping_tool.mapper;

import be.jonasboon.book_keeping_tool.DTO.AddCostCenterDTO;
import be.jonasboon.book_keeping_tool.DTO.CostCenterDTO;
import be.jonasboon.book_keeping_tool.model.CostCenter;
import be.jonasboon.book_keeping_tool.persistence.entity.CostCenterEntity;
import lombok.experimental.UtilityClass;

import static be.jonasboon.book_keeping_tool.constants.CostCenterConstants.COST_CENTER_TITLE;
import static be.jonasboon.book_keeping_tool.constants.CostCenterConstants.COST_OR_INCOME;

@UtilityClass
public class CostCenterMapper {

    public static CostCenter from(CostCenterDTO dto){
        return CostCenter.builder()
                .withCostCenter(dto.getCostCenter())
                .withIsCost(dto.getIsCost())
                .withTotalAmount(dto.getTotalAmount())
                .build();
    }
    public static CostCenterDTO from(String costCenterLine){
        String[] readCostCenter = costCenterLine.split(",");
        return new CostCenterDTO(
                readCostCenter[COST_CENTER_TITLE],
                readCostCenter[COST_OR_INCOME]
        );
    }

    public static CostCenterEntity toEntity(CostCenterDTO dto){
        return CostCenterEntity.builder()
                .withCostCenter(dto.getCostCenter())
                .withIsCost(dto.getIsCost())
                .build();
    }

    public static CostCenterEntity toEntity(AddCostCenterDTO dto){
        return CostCenterEntity.builder()
                .withCostCenter(dto.costCenter())
                .withIsCost(dto.isCost())
                .build();
    }

    public static CostCenterDTO fromEntity(CostCenterEntity entity){
        return CostCenterDTO.builder()
                .withId(entity.getId())
                .withCostCenter(entity.getCostCenter())
                .withIsCost(entity.getIsCost())
                .withTotalAmount(entity.getTotalAmount())
                .build();
    }

}
