package be.javabeans.service;

import be.javabeans.DTO.TransactionDTO;
import be.javabeans.mapper.TransactionMapper;
import be.javabeans.model.Transaction;
import be.javabeans.utils.CommandLineUtils;
import be.javabeans.utils.FileReaderUtil;
import be.javabeans.utils.mapper.CSVObject;
import be.javabeans.utils.mapper.TransactionCSVMapper;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;

import static be.javabeans.utils.CommandLineUtils.getCommandLineInput;
import static be.javabeans.utils.mapper.constants.TransactionConstants.SEPERATOR;

@Slf4j
public final class TransactionService {
     private static TransactionService instance;


    private TransactionService(){
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
        List<CSVObject> transactionsFromFile = reader.readFile(transactionCSVMapper);
        appointCostCentersToTransactions(transactionsFromFile);
    }

    private List<Transaction> appointCostCentersToTransactions(List<CSVObject> transactonsFromFile){
        List<Transaction> transactions = new ArrayList<>();
        for (CSVObject transaction : transactonsFromFile) {
            System.out.printf("%s\n%s\n%s\n", SEPERATOR, transaction.toString(), SEPERATOR);
            System.out.print("On which cost center to put: ");
            //todo validation
            String chosenCostCenter = getCommandLineInput();
            System.out.println(chosenCostCenter);
            transactions.add(
                    TransactionMapper.toTransactionWithCostCenter((TransactionDTO) transaction, chosenCostCenter)
            );
            System.out.println();
        }
        return transactions;
    }
}
