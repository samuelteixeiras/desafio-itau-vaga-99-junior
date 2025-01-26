package com.samuel.itau.desafio.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.samuel.itau.desafio.model.Statistics;
import com.samuel.itau.desafio.model.Transaction;
import com.samuel.itau.desafio.service.TransactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TransactionController.class)
public class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TransactionService transactionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldCreateTransactionWhenValid() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setValor(BigDecimal.valueOf(100));
        transaction.setDataHora(OffsetDateTime.now());

        when(transactionService.addTransaction(any(Transaction.class))).thenReturn(true);

        mockMvc.perform(post("/transacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isCreated());
    }

    @Test
    void shouldRejectTransactionWhenInvalid() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setValor(BigDecimal.valueOf(100));
        transaction.setDataHora(OffsetDateTime.now().plusMinutes(1));

        when(transactionService.addTransaction(any(Transaction.class))).thenReturn(false);

        mockMvc.perform(post("/transacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isUnprocessableEntity());
    }

    @Test
    void shouldRejectTransactionWhenMissingRequiredFields() throws Exception {
        Transaction transaction = new Transaction();
        // Missing required fields

        mockMvc.perform(post("/transacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldRejectTransactionWhenNegativeValue() throws Exception {
        Transaction transaction = new Transaction();
        transaction.setValor(BigDecimal.valueOf(-100));
        transaction.setDataHora(OffsetDateTime.now());

        mockMvc.perform(post("/transacao")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(transaction)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldDeleteAllTransactions() throws Exception {
        mockMvc.perform(delete("/transacao"))
                .andExpect(status().isOk());
    }

    @Test
    void shouldGetStatistics() throws Exception {
        Statistics statistics = new Statistics();
        statistics.setCount(2);
        statistics.setSum(BigDecimal.valueOf(300));
        statistics.setAvg(BigDecimal.valueOf(150));
        statistics.setMax(BigDecimal.valueOf(200));
        statistics.setMin(BigDecimal.valueOf(100));

        when(transactionService.getStatistics()).thenReturn(statistics);

        mockMvc.perform(get("/estatistica"))
                .andExpect(status().isOk());
    }
}