package com.exercise.apirest.dto;

import lombok.Data;

import java.time.LocalDate;

@Data
public class MovimientoDTO {
    private Long id;
    private LocalDate fecha;
    private String tipoMovimiento;
    private double valor;
    private double saldo;
}
