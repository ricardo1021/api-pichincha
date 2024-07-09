package com.exercise.apirest.dto;

import lombok.Data;

@Data
public class CuentaDTO {
    private Long id;
    private String numeroCuenta;
    private String tipoCuenta;
    private double saldoInicial;
    private String estado;
}

