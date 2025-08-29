package be.jonasboon.book_keeping_tool.mapper;

import be.jonasboon.book_keeping_tool.DTO.AddCostCenterDTO;
import be.jonasboon.book_keeping_tool.DTO.CostCenterDTO;
import be.jonasboon.book_keeping_tool.persistence.entity.CostCenter;
import lombok.experimental.UtilityClass;

import static be.jonasboon.book_keeping_tool.constants.CostCenterConstants.COST_CENTER_TITLE;
import static be.jonasboon.book_keeping_tool.constants.CostCenterConstants.COST_OR_INCOME;

@UtilityClass
public class CostCenterMapper {

    public static be.jonasboon.book_keeping_tool.model.CostCenter from(CostCenterDTO dto){
        return be.jonasboon.book_keeping_tool.model.CostCenter.builder()
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

    public static CostCenter toEntity(CostCenterDTO dto){
        return CostCenter.builder()
                .withCostCenter(dto.getCostCenter())
                .withIsCost(dto.getIsCost())
                .build();
    }

    public static CostCenter toEntity(AddCostCenterDTO dto){
        return CostCenter.builder()
                .withCostCenter(dto.costCenter())
                .withIsCost(dto.isCost())
                .build();
    }

    public static CostCenterDTO fromEntity(CostCenter entity){
        return CostCenterDTO.builder()
                .withCostCenter(entity.getCostCenter())
                .withIsCost(entity.getIsCost())
                .withTotalAmount(entity.getTotalAmount())
                .build();
    }

}
