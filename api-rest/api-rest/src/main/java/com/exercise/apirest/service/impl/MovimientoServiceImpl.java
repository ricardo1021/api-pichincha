package com.exercise.apirest.service.impl;

import com.exercise.apirest.dto.MovimientoDTO;
import com.exercise.apirest.exception.ResourceNotFoundException;
import com.exercise.apirest.model.Cuenta;
import com.exercise.apirest.model.Movimiento;
import com.exercise.apirest.repository.CuentaRepository;
import com.exercise.apirest.repository.MovimientoRepository;
import com.exercise.apirest.service.MovimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MovimientoServiceImpl implements MovimientoService {

    private final MovimientoRepository movimientoRepository;
    private final CuentaRepository cuentaRepository;

    @Autowired
    public MovimientoServiceImpl(MovimientoRepository movimientoRepository, CuentaRepository cuentaRepository) {
        this.movimientoRepository = movimientoRepository;
        this.cuentaRepository = cuentaRepository;
    }

    @Override
    public List<MovimientoDTO> obtenerMovimientos() {

        List<Movimiento> movimientoList = movimientoRepository.findAll();
        if (movimientoList.isEmpty()) {
            throw new ResourceNotFoundException("No se encuentran movimientos registrados", 460);
        }

        return movimientoList.stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }

    @Override
    public Optional<MovimientoDTO> obtenerMovimientoPorId(Long id) {
        Movimiento movimiento = obtenerMovimientoId(id);
        return Optional.of(convertToDTO(movimiento));
    }

    private Movimiento obtenerMovimientoId(Long id) {
        Movimiento movimiento = movimientoRepository.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Movimiento no encontrado con ID: " + id, 461));
        return movimiento;
    }

    @Override
    @Transactional
    public MovimientoDTO registrarMovimiento(Long numeroMovimiento, MovimientoDTO movimientoDTO) {
        Cuenta cuenta = cuentaRepository.findByNumeroCuenta(movimientoDTO.getNumeroCuenta())
            .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + movimientoDTO.getNumeroCuenta()));

        BigDecimal nuevoSaldo;
        if ("DEBITO".equals(movimientoDTO.getTipoMovimiento())) {
            nuevoSaldo = cuenta.getSaldoInicial().subtract(movimientoDTO.getValor());
        } else if ("CREDITO".equals(movimientoDTO.getTipoMovimiento())) {
            nuevoSaldo = cuenta.getSaldoInicial().add(movimientoDTO.getValor());
        } else {
            throw new IllegalArgumentException("Tipo de movimiento no válido: " + movimientoDTO.getTipoMovimiento());
        }

        if (nuevoSaldo.compareTo(BigDecimal.ZERO) < 0) {
            throw new RuntimeException("Saldo insuficiente para realizar este movimiento.");
        }

        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDate.now());
        movimiento.setTipoMovimiento(movimientoDTO.getTipoMovimiento());
        movimiento.setValor(movimientoDTO.getValor());
        movimiento.setSaldo(cuenta.getSaldoInicial().add(movimientoDTO.getValor())); // Actualizar el saldo de la cuenta

        movimiento = movimientoRepository.save(movimiento);

        // Actualizar el saldo de la cuenta
        cuenta.setSaldoInicial(movimiento.getSaldo());
        cuentaRepository.save(cuenta);

        return convertToDTO(movimiento);
    }

    @Override
    @Transactional
    public void eliminarMovimiento(Long id) {
        Movimiento movimiento = obtenerMovimientoId(id);
        movimientoRepository.delete(movimiento);
    }

    private MovimientoDTO convertToDTO(Movimiento movimiento) {
        MovimientoDTO movimientoDTO = new MovimientoDTO();
        movimientoDTO.setId(movimiento.getId());
        movimientoDTO.setFecha(movimiento.getFecha());
        movimientoDTO.setTipoMovimiento(movimiento.getTipoMovimiento());
        movimientoDTO.setValor(movimiento.getValor());
        movimientoDTO.setSaldo(movimiento.getSaldo());
        //movimientoDTO.setNumeroCuenta(movimiento.getCuenta().getId()); // ID de la cuenta asociada

        return movimientoDTO;
    }
}
