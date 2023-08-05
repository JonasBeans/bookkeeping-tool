package be.javabeans.controller;

import be.javabeans.service.TransactionService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public final class TransactionController {

    private static TransactionController instance;
    private final TransactionService transactionService;

    private TransactionController(){
        this.transactionService = TransactionService.getInstance();
    }

    public static TransactionController getInstance(){
        if(instance == null){
            instance = new TransactionController();
        }
        return instance;
    }

    public void processTransactionsToBookkeeping(String fileName){
        transactionService.processTransactionsToBookkeeping(fileName);
    }
}
