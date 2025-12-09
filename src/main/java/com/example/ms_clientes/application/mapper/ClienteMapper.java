package com.example.ms_clientes.application.mapper;

import com.example.ms_clientes.application.dto.ClienteResponse;
import com.example.ms_clientes.application.entity.ClienteEntity;

public class ClienteMapper {

    private ClienteMapper() {}

    public static ClienteResponse toResponse(ClienteEntity entity) {
        if (entity == null) return null;

        return ClienteResponse.builder()
                .id(entity.getId())
                .idUsuario(
                        entity.getUsuario() != null
                                ? entity.getUsuario().getId()
                                : null
                )
                .nombre(entity.getNombre())
                .apellido(entity.getApellido())
                .telefono(entity.getTelefono())
                .email(entity.getEmail())
                .direccion(entity.getDireccion())
                .ciudad(entity.getCiudad())
                .fechaRegistro(entity.getFechaRegistro())
                .build();
    }
}