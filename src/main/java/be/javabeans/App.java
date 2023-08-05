package be.javabeans;

import be.javabeans.controller.TransactionController;
import be.javabeans.utils.ArrayUtils;
import lombok.extern.slf4j.Slf4j;

import static be.javabeans.utils.CommandLineUtils.getCommandLineInput;

@Slf4j
public class App
{
    public static void main( String[] args )
    {
        TransactionController transactionController = TransactionController.getInstance();
        String fileName = ArrayUtils.getValueFromArray(0, args);
        transactionController.processTransactionsToBookkeeping(fileName);
    }
}