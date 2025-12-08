package com.example.ms_clientes.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioResponse {

    private Long id;
    private Long idTipoUsuario;
    private String username;
    private String email;
    private Boolean activo;
    private LocalDateTime fechaCreacion;
}
