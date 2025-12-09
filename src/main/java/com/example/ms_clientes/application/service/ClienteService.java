package com.example.ms_clientes.application.service;

import com.example.ms_clientes.application.dto.ActualizarClienteRequest;
import com.example.ms_clientes.application.dto.ClienteResponse;
import com.example.ms_clientes.application.dto.CrearClienteRequest;

import java.util.List;

public interface ClienteService {

    ClienteResponse crear(CrearClienteRequest request);

    ClienteResponse obtenerPorId(Long idCliente);

    ClienteResponse obtenerPorEmail(String email);

    List<ClienteResponse> listarTodos();

    ClienteResponse actualizar(Long idCliente, ActualizarClienteRequest request);

    void eliminar(Long idCliente);

    ClienteResponse sincronizarClienteAutenticado();
}
