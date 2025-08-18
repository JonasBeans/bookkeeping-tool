package be.jonasboon.book_keeping_tool.backup.cost_center.mapper;

import be.jonasboon.book_keeping_tool.backup.common.RestoreMapper;
import be.jonasboon.book_keeping_tool.backup.cost_center.model.CostCenterBackupModel;
import be.jonasboon.book_keeping_tool.persistence.entity.CostCenterEntity;

public class CostCenterMapper implements RestoreMapper<CostCenterEntity, CostCenterBackupModel> {

    public static final CostCenterMapper INSTANCE = new CostCenterMapper();

    @Override
    public CostCenterBackupModel of(CostCenterEntity costCenter) {
        return CostCenterBackupModel.builder()
                .id(costCenter.getId())
                .costCenter(costCenter.getCostCenter())
                .isCost(costCenter.getIsCost())
                .totalAmount(costCenter.getTotalAmount())
                .build();
    }
}
