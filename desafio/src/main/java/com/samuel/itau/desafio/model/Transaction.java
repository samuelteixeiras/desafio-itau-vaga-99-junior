package com.samuel.itau.desafio.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
public class Transaction {
    @NotNull(message = "O valor é obrigatório")
    @PositiveOrZero(message = "O valor deve ser maior ou igual a zero")
    private BigDecimal valor;

    @NotNull(message = "A data/hora é obrigatória")
    private OffsetDateTime dataHora;
}