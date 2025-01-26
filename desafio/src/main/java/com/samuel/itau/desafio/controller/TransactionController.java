package com.samuel.itau.desafio.controller;

import com.samuel.itau.desafio.model.Statistics;
import com.samuel.itau.desafio.model.Transaction;
import com.samuel.itau.desafio.service.TransactionService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@io.swagger.v3.oas.annotations.tags.Tag(name = "Transações", description = "API para gerenciamento de transações financeiras")
public class TransactionController {
    private static final Logger logger = LoggerFactory.getLogger(TransactionController.class);
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Criar nova transação", description = "Adiciona uma nova transação financeira ao sistema")
    @io.swagger.v3.oas.annotations.responses.ApiResponses(value = {
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Transação criada com sucesso"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Dados da transação inválidos"),
        @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "Transação com data futura ou antiga (mais que o limite de tempo configurado)")
    })
    @PostMapping("/transacao")
    public ResponseEntity<Void> addTransaction(@Valid @RequestBody Transaction transaction) {
        logger.info("Received new transaction request");
        boolean success = transactionService.addTransaction(transaction);
        HttpStatus status = success ? HttpStatus.CREATED : HttpStatus.UNPROCESSABLE_ENTITY;
        logger.info("Transaction processing completed with status: {}", status);
        return new ResponseEntity<>(status);
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Excluir todas as transações", description = "Remove todas as transações armazenadas no sistema")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Todas as transações foram excluídas com sucesso")
    @DeleteMapping("/transacao")
    public ResponseEntity<Void> deleteTransactions() {
        logger.info("Received request to delete all transactions");
        transactionService.deleteAllTransactions();
        logger.info("All transactions deleted successfully");
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @io.swagger.v3.oas.annotations.Operation(summary = "Obter estatísticas", description = "Retorna estatísticas das transações dentro da janela de tempo configurada")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Estatísticas calculadas com sucesso")
    @GetMapping("/estatistica")
    public ResponseEntity<Statistics> getStatistics() {
        logger.info("Received request for statistics");
        Statistics statistics = transactionService.getStatistics();
        logger.info("Statistics retrieved successfully");
        return new ResponseEntity<>(statistics, HttpStatus.OK);
    }
}