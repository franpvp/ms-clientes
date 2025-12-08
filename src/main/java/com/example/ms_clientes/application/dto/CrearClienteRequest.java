package com.example.ms_clientes.application.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CrearClienteRequest {
    private Long idUsuario;
    private String nombre;
    private String apellido;
    private String telefono;
    private String direccion;
    private String ciudad;
}