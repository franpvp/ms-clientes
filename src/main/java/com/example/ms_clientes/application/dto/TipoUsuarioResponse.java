package com.example.ms_clientes.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TipoUsuarioResponse {

    private Long id;
    private String nombreTipo;
    private String descripcion;
}
