package com.exercise.apirest.service;

import com.exercise.apirest.dto.ClienteDTO;

import java.util.List;

public interface ClienteService {

    List<ClienteDTO> findAll();

    ClienteDTO obtenerClientePorId(Long id);

    ClienteDTO save(ClienteDTO clienteDTO);

    void eliminarCliente(Long id);

    ClienteDTO actualizarClientePorId(Long clientId, ClienteDTO clienteDTO);
}
