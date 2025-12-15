package com.example.ms_clientes.application.mapper;

import com.example.ms_clientes.application.dto.ClienteResponse;
import com.example.ms_clientes.application.dto.TipoUsuarioDTO;
import com.example.ms_clientes.application.dto.UsuarioDTO;
import com.example.ms_clientes.application.entity.ClienteEntity;
import com.example.ms_clientes.application.entity.UsuarioEntity;

public class ClienteMapper {

    private ClienteMapper() {}

    public static ClienteResponse toResponse(ClienteEntity entity) {
        if (entity == null) return null;

        return ClienteResponse.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .apellido(entity.getApellido())
                .telefono(entity.getTelefono())
                .direccion(entity.getDireccion())
                .email(entity.getEmail())
                .ciudad(entity.getCiudad())
                .fechaRegistro(entity.getFechaRegistro())
                .usuario(toUsuarioDto(entity.getUsuario()))
                .build();
    }

    private static UsuarioDTO toUsuarioDto(UsuarioEntity usuario) {
        if (usuario == null) return null;

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(usuario.getId());
        dto.setUsername(usuario.getUsername());
        dto.setEmail(usuario.getEmail());

        if (usuario.getTipoUsuario() != null) {
            TipoUsuarioDTO tipo = new TipoUsuarioDTO();
            tipo.setId(usuario.getTipoUsuario().getId());
            tipo.setNombreTipo(usuario.getTipoUsuario().getNombre());
            dto.setTipoUsuarioDTO(tipo);
        }

        return dto;
    }
}