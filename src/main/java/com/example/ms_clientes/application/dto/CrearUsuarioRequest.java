package com.example.ms_clientes.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearUsuarioRequest {

    private Long idTipoUsuario;
    private String username;
    private String email;
    private Boolean activo;
}
