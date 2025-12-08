package com.example.ms_clientes.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrearTipoUsuarioRequest {

    private String nombreTipo;
    private String descripcion;
}
