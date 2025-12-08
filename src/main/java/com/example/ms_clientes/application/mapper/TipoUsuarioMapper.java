package com.example.ms_clientes.application.mapper;

import com.example.ms_clientes.application.dto.TipoUsuarioResponse;
import com.example.ms_clientes.application.entity.TipoUsuarioEntity;

public class TipoUsuarioMapper {

    private TipoUsuarioMapper() {}

    public static TipoUsuarioResponse toResponse(TipoUsuarioEntity entity) {
        if (entity == null) return null;

        return TipoUsuarioResponse.builder()
                .id(entity.getId())
                .nombreTipo(entity.getNombreTipo())
                .descripcion(entity.getDescripcion())
                .build();
    }
}
