package com.exercise.apirest.service;

import com.exercise.apirest.dto.MovimientoDTO;

import java.util.List;
import java.util.Optional;

public interface MovimientoService {

    List<MovimientoDTO> obtenerMovimientos();

    Optional<MovimientoDTO> obtenerMovimientoPorId(Long id);

    MovimientoDTO save(MovimientoDTO movimientoDTO);

    void delete(Long id);
}
