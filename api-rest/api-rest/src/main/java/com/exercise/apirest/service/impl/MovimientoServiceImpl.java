package com.exercise.apirest.service.impl;

import com.exercise.apirest.dto.MovimientoDTO;
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
        return movimientoRepository.findAll().stream()
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
            .orElseThrow(() -> new RuntimeException("Movimiento no encontrado con ID: " + id));
        return movimiento;
    }

    @Override
    @Transactional
    public MovimientoDTO save(MovimientoDTO movimientoDTO) {
        Cuenta cuenta = cuentaRepository.findById(movimientoDTO.getId())
            .orElseThrow(() -> new RuntimeException("Cuenta no encontrada con ID: " + movimientoDTO.getId()));

        BigDecimal valorMovimiento = BigDecimal.valueOf(movimientoDTO.getValor());
        if (movimientoDTO.getTipoMovimiento().equals("retiro")) {
            valorMovimiento = valorMovimiento.negate(); // Si es retiro, el valor es negativo
        }

        Movimiento movimiento = new Movimiento();
        movimiento.setFecha(LocalDate.now());
        movimiento.setTipoMovimiento(movimientoDTO.getTipoMovimiento());
        movimiento.setValor(valorMovimiento);
        //movimiento.setSaldo(cuenta.getSaldo().add(valorMovimiento)); // Actualizar el saldo de la cuenta

        movimiento = movimientoRepository.save(movimiento);

        // Actualizar el saldo de la cuenta
        //cuenta.setSaldo(movimiento.getSaldo());
        cuentaRepository.save(cuenta);

        return convertToDTO(movimiento);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        Movimiento movimiento = obtenerMovimientoId(id);
        movimientoRepository.delete(movimiento);
    }

    private MovimientoDTO convertToDTO(Movimiento movimiento) {
        MovimientoDTO movimientoDTO = new MovimientoDTO();
        movimientoDTO.setId(movimiento.getId());
        movimientoDTO.setFecha(movimiento.getFecha());
        movimientoDTO.setTipoMovimiento(movimiento.getTipoMovimiento());
        //movimientoDTO.setValor(movimiento.getValor());
        //movimientoDTO.setSaldo(movimiento.getSaldo());
        //movimientoDTO.setCuentaId(movimiento.getCuenta().getId()); // ID de la cuenta asociada

        return movimientoDTO;
    }
}
