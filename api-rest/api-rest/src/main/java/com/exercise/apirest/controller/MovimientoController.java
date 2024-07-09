package com.exercise.apirest.controller;

import com.exercise.apirest.dto.CuentaDTO;
import com.exercise.apirest.dto.MovimientoDTO;
import com.exercise.apirest.service.MovimientoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/movimientos")
@RequiredArgsConstructor
public class MovimientoController {

    private final MovimientoService movimientoService;

    @GetMapping
    public ResponseEntity<List<MovimientoDTO>> obtenerListaMovimientos() {
        return new ResponseEntity<>(movimientoService.obtenerMovimientos(), HttpStatus.OK);
    }

    @GetMapping("/{numeroMovimiento}")
    public ResponseEntity<MovimientoDTO> obtenerCuentaPorNumero(@PathVariable Long numeroMovimiento) {
        Optional<MovimientoDTO> movimientoDTO = movimientoService.obtenerMovimientoPorId(numeroMovimiento);
        return movimientoDTO.map(ResponseEntity::ok)
            .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{numeroMovimiento}")
    public ResponseEntity<MovimientoDTO> registrarMovimiento(@PathVariable Long numeroMovimiento,
        @RequestBody MovimientoDTO movimientoDTO) {
        return ResponseEntity.ok(movimientoService.registrarMovimiento(numeroMovimiento, movimientoDTO));
    }

    @DeleteMapping("/{numeroMovimiento}")
    public ResponseEntity<Void> eliminarMovimiento(@PathVariable Long numeroMovimiento) {
        movimientoService.eliminarMovimiento(numeroMovimiento);
        return ResponseEntity.noContent().build();
    }

}
