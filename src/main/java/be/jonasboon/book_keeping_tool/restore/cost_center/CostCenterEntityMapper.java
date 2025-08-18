package be.jonasboon.book_keeping_tool.restore.cost_center;

import be.jonasboon.book_keeping_tool.persistence.entity.CostCenterEntity;
import be.jonasboon.book_keeping_tool.restore.common.RestoreEntityMapper;
import be.jonasboon.book_keeping_tool.utils.mapper.CSVObject;

public class CostCenterEntityMapper implements RestoreEntityMapper<CostCenterEntity> {

    @Override
    public CostCenterEntity map(CSVObject restoreModel) {
        if (restoreModel instanceof CostCenterRestoreModel model) {
            return CostCenterEntity.builder()
                    .withId(model.id())
                    .withCostCenter(model.costCenter())
                    .withIsCost(model.isCost())
                    .withTotalAmount(model.totalAmount())
                    .build();
        }
        else throw new ClassCastException();
    }

}
