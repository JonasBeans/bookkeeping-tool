package be.jonasboon.book_keeping_tool.mapper;

import be.jonasboon.book_keeping_tool.DTO.AddCostCenterDTO;
import be.jonasboon.book_keeping_tool.DTO.CostCenterDTO;
import be.jonasboon.book_keeping_tool.persistence.entity.CostCenter;
import lombok.experimental.UtilityClass;

@UtilityClass
public class CostCenterMapper {

    public static CostCenter of(AddCostCenterDTO dto){
        return CostCenter.builder()
                .withCostCenter(dto.costCenter())
                .withIsCost(dto.isCost())
                .build();
    }

    public static CostCenterDTO of(CostCenter entity){
        return CostCenterDTO.builder()
                .withCostCenter(entity.getCostCenter())
                .withIsCost(entity.getIsCost())
                .withTotalAmount(entity.getTotalAmount())
                .withVersion(entity.getVersion())
                .build();
    }

}
