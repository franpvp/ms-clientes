package com.example.ms_clientes.application.service;

import com.example.ms_clientes.application.dto.ActualizarTipoUsuarioRequest;
import com.example.ms_clientes.application.dto.CrearTipoUsuarioRequest;
import com.example.ms_clientes.application.dto.TipoUsuarioResponse;

import java.util.List;

public interface TipoUsuarioService {

    TipoUsuarioResponse crear(CrearTipoUsuarioRequest request);

    TipoUsuarioResponse obtenerPorId(Long id);

    TipoUsuarioResponse obtenerPorNombre(String nombreTipo);

    List<TipoUsuarioResponse> listarTodos();

    TipoUsuarioResponse actualizar(Long id, ActualizarTipoUsuarioRequest request);

    void eliminar(Long id);
}
