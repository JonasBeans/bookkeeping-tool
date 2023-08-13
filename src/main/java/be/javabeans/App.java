package be.javabeans;

import be.javabeans.constants.FileConstansts;
import be.javabeans.controller.TransactionController;
import be.javabeans.service.CostCenterService;
import be.javabeans.utils.ArrayUtils;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;

import static be.javabeans.constants.FileConstansts.*;
import static be.javabeans.utils.CommandLineUtils.getCommandLineInput;

@Slf4j
public class App
{
    public static void main( String[] args )
    {
        String fileName = ArrayUtils.getValueFromArray(0, args);
        ACCOUNTING_WORKBOOK_LOCATION = ArrayUtils.getValueFromArray(1, args);
        CostCenterService costCenter = CostCenterService.getInstance();
        TransactionController transactionController = TransactionController.getInstance();
        transactionController.processTransactionsToBookkeeping(fileName);
    }
}