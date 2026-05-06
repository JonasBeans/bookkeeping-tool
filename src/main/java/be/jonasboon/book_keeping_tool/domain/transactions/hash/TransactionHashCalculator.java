package be.jonasboon.book_keeping_tool.domain.transactions.hash;

import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.util.HexFormat;

public final class TransactionHashCalculator {

    private static final String SEPARATOR = "\u001F";

    private TransactionHashCalculator() {
    }

    public static String calculate(Transaction transaction) {
        // Keep the dedupe key on fields available before description/message were added, so older assigned transactions
        // are still recognized when the same Excel file is uploaded again.
        String source = String.join(
                SEPARATOR,
                normalize(transaction.getBookDate()),
                normalize(transaction.getTransactionDate()),
                normalize(transaction.getAmount()),
                normalize(transaction.getNameOtherParty())
        );
        return md5(source);
    }

    private static String normalize(LocalDate date) {
        return date == null ? "" : date.toString();
    }

    private static String normalize(BigDecimal amount) {
        if (amount == null) {
            return "";
        }
        return amount.stripTrailingZeros().toPlainString();
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }

    private static String md5(String value) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            return HexFormat.of().formatHex(digest.digest(value.getBytes(StandardCharsets.UTF_8)));
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException("MD5 hashing is not available", e);
        }
    }
}
