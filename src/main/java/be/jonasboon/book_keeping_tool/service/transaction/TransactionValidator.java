package be.jonasboon.book_keeping_tool.service.transaction;

import be.jonasboon.book_keeping_tool.persistence.entity.TransactionEntity;

import java.util.List;

public class TransactionValidator {

    public static void validateAllHaveCostCenters(List<TransactionEntity> transactions) throws ValidationException {
        if (transactions.stream().anyMatch(TransactionEntity::hasNoCostCenter)) {
            List<TransactionEntity> without = transactions.stream().filter(TransactionEntity::hasNoCostCenter).toList();
            throw ValidationException.hasNoCostCenter(without);
        }
    }

    public static class ValidationException extends Exception{

        private ValidationException(String message) {
            super(message);
        }

        public static ValidationException hasNoCostCenter(List<TransactionEntity> transactions) {
            StringBuilder errorBuilder = new StringBuilder();
            errorBuilder.append("There are no cost center in transactions list.");
            transactions.forEach(transaction -> errorBuilder.append(transaction).append("/n"));
            return new ValidationException(errorBuilder.toString());
        }
    }}