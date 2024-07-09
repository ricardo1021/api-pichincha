package com.exercise.apirest.service;

import com.exercise.apirest.dto.CuentaDTO;
import com.exercise.apirest.model.Cuenta;

import java.util.List;
import java.util.Optional;

public interface CuentaService {

    List<CuentaDTO> obtenerCuentas();

    Optional<CuentaDTO> obtenerCuentaPorNumero(Long numeroCuenta);

    CuentaDTO crearCuenta(CuentaDTO cuenta);

    CuentaDTO actualizarCuenta(Long numeroCuenta, CuentaDTO cuentaActualizada);

    void eliminarCuenta(Long numeroCuenta);

}
