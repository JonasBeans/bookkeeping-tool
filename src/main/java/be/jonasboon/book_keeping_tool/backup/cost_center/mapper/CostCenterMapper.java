package be.jonasboon.book_keeping_tool.backup.cost_center.mapper;

import be.jonasboon.book_keeping_tool.backup.common.RestoreMapper;
import be.jonasboon.book_keeping_tool.backup.cost_center.model.CostCenterBackupModel;
import be.jonasboon.book_keeping_tool.persistence.entity.CostCenter;

public class CostCenterMapper implements RestoreMapper<CostCenter, CostCenterBackupModel> {

    public static final CostCenterMapper INSTANCE = new CostCenterMapper();

    @Override
    public CostCenterBackupModel of(CostCenter costCenter) {
        return CostCenterBackupModel.builder()
                .costCenter(costCenter.getCostCenter())
                .isCost(costCenter.getIsCost())
                .totalAmount(costCenter.getTotalAmount())
                .build();
    }
}
