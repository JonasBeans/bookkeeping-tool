package be.jonasboon.book_keeping_tool.domain.transactions.hash;

import be.jonasboon.book_keeping_tool.domain.transactions.entity.Transaction;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

class TransactionHashCalculatorTest {

    @Test
    void calculatesSameHashForEquivalentAmountsAndTrimmedText() {
        Transaction first = transaction("10.00", " Grocery Store ");
        Transaction second = transaction("10", "Grocery Store");

        assertThat(TransactionHashCalculator.calculate(first))
                .isEqualTo(TransactionHashCalculator.calculate(second));
    }

    @Test
    void changesHashWhenStableImportedTransactionFieldsChange() {
        Transaction first = transaction("10.00", "Grocery Store");
        Transaction second = transaction("10.00", "Book Store");

        assertThat(TransactionHashCalculator.calculate(first))
                .isNotEqualTo(TransactionHashCalculator.calculate(second));
    }

    private Transaction transaction(String amount, String nameOtherParty) {
        return Transaction.builder()
                .withBookDate(LocalDate.of(2026, 1, 5))
                .withTransactionDate(LocalDate.of(2026, 1, 4))
                .withAmount(new BigDecimal(amount))
                .withDescription("Card payment")
                .withNameOtherParty(nameOtherParty)
                .withMessage("Payment reference")
                .build();
    }
}
