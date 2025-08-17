package be.jonasboon.book_keeping_tool.backup.cost_center.model;

import be.jonasboon.book_keeping_tool.backup.common.BackupModel;
import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record CostCenterBackupModel(
        String costCenter,
        Boolean isCost,
        BigDecimal totalAmount
) implements BackupModel {

    public String toBackupString() {
        return String.format("%s,%s,%s",
                costCenter,
                isCost,
                totalAmount
        );
    }
}
