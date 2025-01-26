package com.samuel.itau.desafio.service;

import com.samuel.itau.desafio.model.Statistics;
import com.samuel.itau.desafio.model.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionServiceTest {
    private TransactionService transactionService;

    @BeforeEach
    void setUp() {
        transactionService = new TransactionService();
    }

    @Test
    void shouldAddValidTransaction() {
        Transaction transaction = new Transaction();
        transaction.setValor(BigDecimal.valueOf(100));
        transaction.setDataHora(OffsetDateTime.now());

        boolean result = transactionService.addTransaction(transaction);

        assertTrue(result);
    }

    @Test
    void shouldRejectFutureTransaction() {
        Transaction transaction = new Transaction();
        transaction.setValor(BigDecimal.valueOf(100));
        transaction.setDataHora(OffsetDateTime.now().plusMinutes(1));

        boolean result = transactionService.addTransaction(transaction);

        assertFalse(result);
    }

    @Test
    void shouldDeleteAllTransactions() {
        Transaction transaction = new Transaction();
        transaction.setValor(BigDecimal.valueOf(100));
        transaction.setDataHora(OffsetDateTime.now());
        transactionService.addTransaction(transaction);

        transactionService.deleteAllTransactions();

        Statistics statistics = transactionService.getStatistics();
        assertEquals(0, statistics.getCount());
        assertEquals(BigDecimal.ZERO, statistics.getSum());
    }

    @Test
    void shouldCalculateStatisticsForValidTransactions() {
        Transaction transaction1 = new Transaction();
        transaction1.setValor(BigDecimal.valueOf(100));
        transaction1.setDataHora(OffsetDateTime.now());

        Transaction transaction2 = new Transaction();
        transaction2.setValor(BigDecimal.valueOf(200));
        transaction2.setDataHora(OffsetDateTime.now());

        transactionService.addTransaction(transaction1);
        transactionService.addTransaction(transaction2);

        Statistics statistics = transactionService.getStatistics();

        assertEquals(2, statistics.getCount());
        assertEquals(BigDecimal.valueOf(300), statistics.getSum());
        assertEquals(BigDecimal.valueOf(150), statistics.getAvg());
        assertEquals(BigDecimal.valueOf(200), statistics.getMax());
        assertEquals(BigDecimal.valueOf(100), statistics.getMin());
    }

    @Test
    void shouldExcludeOldTransactionsFromStatistics() {
        Transaction oldTransaction = new Transaction();
        oldTransaction.setValor(BigDecimal.valueOf(100));
        oldTransaction.setDataHora(OffsetDateTime.now().minusMinutes(2));

        Transaction newTransaction = new Transaction();
        newTransaction.setValor(BigDecimal.valueOf(200));
        newTransaction.setDataHora(OffsetDateTime.now());

        transactionService.addTransaction(oldTransaction);
        transactionService.addTransaction(newTransaction);

        Statistics statistics = transactionService.getStatistics();

        assertEquals(1, statistics.getCount());
        assertEquals(BigDecimal.valueOf(200), statistics.getSum());
        assertEquals(BigDecimal.valueOf(200), statistics.getAvg());
        assertEquals(BigDecimal.valueOf(200), statistics.getMax());
        assertEquals(BigDecimal.valueOf(200), statistics.getMin());
    }

    @Test
    void shouldReturnZeroStatisticsWhenNoTransactions() {
        Statistics statistics = transactionService.getStatistics();

        assertEquals(0, statistics.getCount());
        assertEquals(BigDecimal.ZERO, statistics.getSum());
        assertEquals(BigDecimal.ZERO, statistics.getAvg());
        assertEquals(BigDecimal.ZERO, statistics.getMax());
        assertEquals(BigDecimal.ZERO, statistics.getMin());
    }
}