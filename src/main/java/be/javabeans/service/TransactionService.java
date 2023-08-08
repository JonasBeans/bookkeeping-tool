package be.javabeans.service;

import be.javabeans.DTO.TransactionDTO;
import be.javabeans.constants.FileConstansts;
import be.javabeans.mapper.TransactionMapper;
import be.javabeans.model.Transaction;
import be.javabeans.utils.FileReaderUtil;
import be.javabeans.utils.mapper.CSVObject;
import be.javabeans.utils.mapper.TransactionCSVMapper;
import be.javabeans.constants.TransactionConstants;
import be.javabeans.workbook.TransactionsWorkbookSheet;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static be.javabeans.constants.FileConstansts.ACCOUNTING_WORKBOOK_LOCATION;
import static be.javabeans.constants.TransactionConstants.ACCOUNTING_WORKSHEET;
import static be.javabeans.utils.CommandLineUtils.getCommandLineInput;
import static be.javabeans.constants.TransactionConstants.SEPERATOR;

@Slf4j
public final class TransactionService {
     private static TransactionService instance;
     private final TransactionsWorkbookSheet sheet;
     private Predicate<String> validateCostCenter = TransactionConstants.POSSIBLE_COST_CENTERS::contains;
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
        transactions.stream()
                .map(Transaction::getTransactionDate)
                .forEach(this::tryToReadTransactionSheet);
    }

    private void tryToReadTransactionSheet(LocalDate date) {
        try {
            sheet.appendDate(date);
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

            System.out.printf("Transaction was put on cost center: %s\n", chosenCostCenter);

            transactions.add(
                    TransactionMapper.toTransactionWithCostCenter((TransactionDTO) transaction, chosenCostCenter)
            );
            System.out.println();
        }
        return transactions;
    }
}
