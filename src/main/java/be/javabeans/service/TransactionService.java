package be.javabeans.service;

import be.javabeans.DTO.TransactionDTO;
import be.javabeans.mapper.TransactionMapper;
import be.javabeans.model.Transaction;
import be.javabeans.utils.FileReaderUtil;
import be.javabeans.utils.mapper.CSVObject;
import be.javabeans.utils.mapper.TransactionCSVMapper;
import be.javabeans.workbook.TransactionsWorkbookSheet;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

import static be.javabeans.constants.FileConstansts.ACCOUNTING_WORKBOOK_LOCATION;
import static be.javabeans.constants.TransactionConstants.SEPERATOR;
import static be.javabeans.utils.CommandLineUtils.getCommandLineInput;

@Slf4j
public final class TransactionService {
     private static TransactionService instance;
     private final TransactionsWorkbookSheet sheet;
     private Predicate<String> validateCostCenter = (value) -> CostCenterService.getInstance().getCostCenterIndexes().contains(Integer.valueOf(value));
    private TransactionService(){
        this.sheet = new TransactionsWorkbookSheet(ACCOUNTING_WORKBOOK_LOCATION);
    }

    public static TransactionService getInstance(){
        if(instance == null){
            instance = new TransactionService();
        }
        return instance;
    }

    public void processTransactionsToBookkeeping(String fileName){
        FileReaderUtil reader = new FileReaderUtil(fileName);
        TransactionCSVMapper transactionCSVMapper = new TransactionCSVMapper();

        List<CSVObject> transactionsFromFile = reader.convert(transactionCSVMapper);
        List<Transaction> transactions = appointCostCentersToTransactions(transactionsFromFile);
        transactions.forEach(this::tryToReadTransactionSheet);
    }

    private void tryToReadTransactionSheet(Transaction transaction) {
        try {
            sheet.processTransactionToAccountingFile(transaction);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Transaction> appointCostCentersToTransactions(List<CSVObject> transactonsFromFile){
        List<Transaction> transactions = new ArrayList<>();
        for (CSVObject transaction : transactonsFromFile) {
            System.out.printf("%s\n%s\n%s\n", SEPERATOR, transaction.toString(), SEPERATOR);
            System.out.print("On which cost center to put: ");
            String chosenCostCenter = getCommandLineInput(validateCostCenter);

            //todo add cost center confirmation
            System.out.printf("Transaction was put on cost center: %s\n", chosenCostCenter);

            transactions.add(
                    TransactionMapper.toTransactionWithCostCenter((TransactionDTO) transaction, chosenCostCenter)
            );
            System.out.println();
        }
        return transactions;
    }
}
