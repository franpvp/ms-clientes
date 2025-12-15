package com.example.ms_clientes.application.mapper;

import com.example.ms_clientes.application.dto.TipoUsuarioDTO;
import com.example.ms_clientes.application.dto.UsuarioDTO;
import com.example.ms_clientes.application.dto.UsuarioResponse;
import com.example.ms_clientes.application.entity.UsuarioEntity;

public class UsuarioMapper {

    private UsuarioMapper() {}

    public static UsuarioResponse toResponse(UsuarioEntity entity) {
        if (entity == null) return null;

        return UsuarioResponse.builder()
                .id(entity.getId())
                .idTipoUsuario(entity.getTipoUsuario() != null ? entity.getTipoUsuario().getId() : null)
                .username(entity.getUsername())
                .email(entity.getEmail())
                .activo(entity.getActivo())
                .fechaCreacion(entity.getFechaCreacion())
                .build();
    }

    public UsuarioDTO toUsuarioDto(UsuarioEntity entity) {
        if (entity == null) return null;

        UsuarioDTO dto = new UsuarioDTO();
        dto.setId(entity.getId());
        dto.setUsername(entity.getUsername());
        dto.setEmail(entity.getEmail());

        if (entity.getTipoUsuario() != null) {
            TipoUsuarioDTO tipo = new TipoUsuarioDTO();
            tipo.setId(entity.getTipoUsuario().getId());
            tipo.setNombreTipo(entity.getTipoUsuario().getNombre());
            dto.setTipoUsuarioDTO(tipo);
        }

        return dto;
    }
}
