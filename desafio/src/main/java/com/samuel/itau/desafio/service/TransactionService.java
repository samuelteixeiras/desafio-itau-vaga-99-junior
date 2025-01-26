package com.samuel.itau.desafio.service;

import com.samuel.itau.desafio.model.Statistics;
import com.samuel.itau.desafio.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedQueue;

@Service
public class TransactionService {
    private static final Logger logger = LoggerFactory.getLogger(TransactionService.class);
    private final ConcurrentLinkedQueue<Transaction> transactions = new ConcurrentLinkedQueue<>();

    @Value("${app.transaction.statistics.window-seconds:60}")
    private int statisticsWindowSeconds;

    public boolean addTransaction(Transaction transaction) {
        logger.info("Processing new transaction with value: {}", transaction.getValor());
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime cutoffTime = now.minusSeconds(statisticsWindowSeconds);

        if (transaction.getDataHora().isAfter(now)) {
            logger.warn("Transaction rejected: future timestamp detected. Transaction time: {}", transaction.getDataHora());
            return false;
        }

        if (transaction.getDataHora().isBefore(cutoffTime)) {
            logger.warn("Transaction rejected: timestamp too old. Transaction time: {}", transaction.getDataHora());
            return false;
        }

        transactions.add(transaction);
        logger.info("Transaction successfully added");
        return true;
    }

    public void deleteAllTransactions() {
        logger.info("Deleting all transactions");
        transactions.clear();
        logger.info("All transactions have been deleted");
    }

    public Statistics getStatistics() {
        logger.debug("Calculating statistics for the last {} seconds", statisticsWindowSeconds);
        long startTime = System.nanoTime();
        OffsetDateTime now = OffsetDateTime.now();
        OffsetDateTime cutoffTime = now.minusSeconds(statisticsWindowSeconds);
        List<Transaction> validTransactions = new ArrayList<>();

        for (Transaction transaction : transactions) {
            if (!transaction.getDataHora().isAfter(now) && !transaction.getDataHora().isBefore(cutoffTime)) {
                validTransactions.add(transaction);
            }
        }

        Statistics statistics = new Statistics();
        if (validTransactions.isEmpty()) {
            logger.info("No valid transactions found in the last {} seconds", statisticsWindowSeconds);
            statistics.setCount(0);
            statistics.setSum(BigDecimal.ZERO);
            statistics.setAvg(BigDecimal.ZERO);
            statistics.setMax(BigDecimal.ZERO);
            statistics.setMin(BigDecimal.ZERO);
            return statistics;
        }

        BigDecimal sum = validTransactions.stream()
                .map(Transaction::getValor)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal max = validTransactions.stream()
                .map(Transaction::getValor)
                .max(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal min = validTransactions.stream()
                .map(Transaction::getValor)
                .min(BigDecimal::compareTo)
                .orElse(BigDecimal.ZERO);

        BigDecimal avg = sum.divide(BigDecimal.valueOf(validTransactions.size()), 2, RoundingMode.HALF_UP);

        statistics.setCount(validTransactions.size());
        statistics.setSum(sum);
        statistics.setAvg(avg);
        statistics.setMax(max);
        statistics.setMin(min);

        long endTime = System.nanoTime();
        double executionTimeMs = (endTime - startTime) / 1_000_000.0;
        
        logger.info("Statistics calculated: count={}, sum={}, avg={}, max={}, min={}, execution_time_ms={}", 
                    statistics.getCount(), statistics.getSum(), statistics.getAvg(),
                    statistics.getMax(), statistics.getMin(), executionTimeMs);

        return statistics;
    }
}