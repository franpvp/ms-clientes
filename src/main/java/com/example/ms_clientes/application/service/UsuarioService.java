package com.example.ms_clientes.application.service;

import com.example.ms_clientes.application.dto.ActualizarUsuarioRequest;
import com.example.ms_clientes.application.dto.CrearUsuarioRequest;
import com.example.ms_clientes.application.dto.UsuarioResponse;

import java.util.List;

public interface UsuarioService {

    UsuarioResponse crear(CrearUsuarioRequest request);

    UsuarioResponse obtenerPorId(Long id);

    UsuarioResponse obtenerPorEmail(String email);

    UsuarioResponse obtenerPorUsername(String username);

    List<UsuarioResponse> listarTodos();

    UsuarioResponse actualizar(Long id, ActualizarUsuarioRequest request);

    void eliminar(Long id);
}
