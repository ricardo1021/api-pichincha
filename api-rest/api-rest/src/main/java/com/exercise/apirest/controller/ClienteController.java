package com.exercise.apirest.controller;

import com.exercise.apirest.dto.ClienteDTO;
import com.exercise.apirest.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @GetMapping
    public ResponseEntity<List<ClienteDTO>> getAllClientes() {
        return new ResponseEntity<>(clienteService.findAll(), HttpStatus.OK);
    }

    @GetMapping("{clienteId}")
    public ResponseEntity<ClienteDTO> obtenerClientePorId(@PathVariable Long clienteId) {
        return new ResponseEntity<>(clienteService.obtenerClientePorId(clienteId), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ClienteDTO> crearCliente(@RequestBody ClienteDTO clienteDTO) {
        return new ResponseEntity<>(clienteService.save(clienteDTO), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClienteDTO> actualizarCliente(@PathVariable Long id, @RequestBody ClienteDTO clienteDTO) {
        return new ResponseEntity<>(clienteService.actualizarClientePorId(id, clienteDTO), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity eliminarCliente(@PathVariable Long id) {
        clienteService.eliminarCliente(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
