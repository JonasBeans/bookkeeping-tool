package be.jonasboon.book_keeping_tool.restore.cost_center;

import be.jonasboon.book_keeping_tool.utils.mapper.CSVFileMapper;
import be.jonasboon.book_keeping_tool.utils.mapper.CSVObject;

import static be.jonasboon.book_keeping_tool.utils.mapper.TransactionCSVMapper.convertAmount;

public class CSVCostCenterMapper implements CSVFileMapper {

    private final Integer ID = 0; // Not used, but kept for consistency with other mappers
    private final Integer COST_CENTER = 1;
    private final Integer COST_INDICATOR = 2;
    private final Integer TOTAL_AMOUNT = 3;

    @Override
    public CSVObject mapToObject(String[] seperatedValues) {
        return CostCenterRestoreModel.builder()
                .id(seperatedValues[ID])
                .costCenter(seperatedValues[COST_CENTER])
                .isCost(Boolean.valueOf(seperatedValues[COST_INDICATOR]))
                .totalAmount(convertAmount(seperatedValues[TOTAL_AMOUNT]))
                .build();
    }

}
