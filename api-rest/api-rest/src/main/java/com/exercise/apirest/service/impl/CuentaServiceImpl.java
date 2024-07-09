package com.exercise.apirest.service.impl;

import com.exercise.apirest.dto.CuentaDTO;
import com.exercise.apirest.exception.ResourceNotFoundException;
import com.exercise.apirest.model.Cuenta;
import com.exercise.apirest.repository.CuentaRepository;
import com.exercise.apirest.service.CuentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CuentaServiceImpl implements CuentaService {


    private final CuentaRepository cuentaRepository;

    @Override
    public List<CuentaDTO> obtenerCuentas() {
        List<Cuenta> cuentaList = cuentaRepository.findAll();

        if (cuentaList.isEmpty()) {
            throw new ResourceNotFoundException("No se encuentran cuentas registradas", 460);
        }

        return cuentaList.stream().map(this::mapToDTO).collect(Collectors.toList());
    }

    @Override
    public Optional<CuentaDTO> obtenerCuentaPorNumero(Long numeroCuenta) {
        Cuenta cuenta = obtenerCuentaId(numeroCuenta);
        return Optional.ofNullable(mapToDTO(cuenta));
    }

    private Cuenta obtenerCuentaId(Long numeroCuenta) {
        return cuentaRepository.findById(numeroCuenta)
            .orElseThrow(()-> new ResourceNotFoundException("No se encuentra la cuenta solicitada.", 461));
    }

    @Override
    public CuentaDTO crearCuenta(CuentaDTO cuentaDTO) {
        Cuenta cuenta = mapToEntity(cuentaDTO);
        cuenta = cuentaRepository.save(cuenta);
        return mapToDTO(cuenta);
    }

    @Override
    public CuentaDTO actualizarCuenta(Long numeroCuenta, CuentaDTO cuentaActualizada) {
        Cuenta cuentaExistente = obtenerCuentaId(numeroCuenta);

        cuentaExistente.setTipoCuenta(cuentaActualizada.getTipoCuenta());
        cuentaExistente.setSaldoInicial(cuentaActualizada.getSaldoInicial());
        cuentaExistente.setEstado(cuentaActualizada.getEstado());
        cuentaExistente = cuentaRepository.save(cuentaExistente);

        return mapToDTO(cuentaExistente);
    }

    @Override
    public void eliminarCuenta(Long numeroCuenta) {
        Cuenta cuenta = obtenerCuentaId(numeroCuenta);
        cuentaRepository.delete(cuenta);
    }

    private CuentaDTO mapToDTO(Cuenta cuenta) {
        CuentaDTO cuentaDTO = new CuentaDTO();
        cuentaDTO.setId(cuenta.getId());
        cuentaDTO.setNumeroCuenta(cuenta.getNumeroCuenta());
        cuentaDTO.setTipoCuenta(cuenta.getTipoCuenta());
        cuentaDTO.setSaldoInicial(cuenta.getSaldoInicial());
        cuentaDTO.setEstado(cuenta.getEstado());
        return cuentaDTO;
    }

    private Cuenta mapToEntity(CuentaDTO cuentaDTO) {
        Cuenta cuenta = new Cuenta();
        cuenta.setNumeroCuenta(cuentaDTO.getNumeroCuenta());
        cuenta.setTipoCuenta(cuentaDTO.getTipoCuenta());
        cuenta.setSaldoInicial(cuentaDTO.getSaldoInicial());
        cuenta.setEstado(cuentaDTO.getEstado());
        return cuenta;
    }
}
