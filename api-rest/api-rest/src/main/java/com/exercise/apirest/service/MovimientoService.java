package com.exercise.apirest.service;

import com.exercise.apirest.dto.MovimientoDTO;

import java.util.List;

public interface MovimientoService {

    List<MovimientoDTO> obtenerMovimientos();

    MovimientoDTO findById(Long id);

    MovimientoDTO save(MovimientoDTO movimientoDTO);

    void delete(Long id);
}
