package com.example.ms_clientes.application.controller;

import com.example.ms_clientes.application.dto.ActualizarUsuarioRequest;
import com.example.ms_clientes.application.dto.CrearUsuarioRequest;
import com.example.ms_clientes.application.dto.UsuarioResponse;
import com.example.ms_clientes.application.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioControllerTest {

    @Mock
    private UsuarioService usuarioService;

    @InjectMocks
    private UsuarioController usuarioController;

    @Test
    void crearTest() {
        // Arrange
        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setUsername("fran");

        UsuarioResponse responseService = UsuarioResponse.builder()
                .id(10L)
                .username("fran")
                .email("fran@example.com")
                .activo(true)
                .fechaCreacion(LocalDateTime.now())
                .build();

        when(usuarioService.crear(request)).thenReturn(responseService);

        // Act
        UsuarioResponse response = usuarioController.crear(request);

        // Assert
        assertThat(response).isSameAs(responseService);
        verify(usuarioService, times(1)).crear(request);
    }

    @Test
    void obtenerPorIdTest() {
        // Arrange
        Long id = 10L;
        UsuarioResponse responseService = UsuarioResponse.builder()
                .id(id)
                .username("fran")
                .build();

        when(usuarioService.obtenerPorId(id)).thenReturn(responseService);

        // Act
        UsuarioResponse response = usuarioController.obtenerPorId(id);

        // Assert
        assertThat(response.getId()).isEqualTo(id);
        verify(usuarioService, times(1)).obtenerPorId(id);
    }

    @Test
    void listarTodosTest() {
        // Arrange
        UsuarioResponse u1 = UsuarioResponse.builder().id(1L).build();
        UsuarioResponse u2 = UsuarioResponse.builder().id(2L).build();

        when(usuarioService.listarTodos()).thenReturn(List.of(u1, u2));

        // Act
        List<UsuarioResponse> result = usuarioController.listarTodos();

        // Assert
        assertThat(result).hasSize(2);
        verify(usuarioService, times(1)).listarTodos();
    }

    @Test
    void obtenerPorEmailTest() {
        // Arrange
        String email = "fran@example.com";
        UsuarioResponse responseService = UsuarioResponse.builder()
                .id(10L)
                .email(email)
                .build();

        when(usuarioService.obtenerPorEmail(email)).thenReturn(responseService);

        // Act
        UsuarioResponse response = usuarioController.obtenerPorEmail(email);

        // Assert
        assertThat(response.getEmail()).isEqualTo(email);
        verify(usuarioService, times(1)).obtenerPorEmail(email);
    }

    @Test
    void obtenerPorUsernameTest() {
        // Arrange
        String username = "fran";
        UsuarioResponse responseService = UsuarioResponse.builder()
                .id(10L)
                .username(username)
                .build();

        when(usuarioService.obtenerPorUsername(username)).thenReturn(responseService);

        // Act
        UsuarioResponse response = usuarioController.obtenerPorUsername(username);

        // Assert
        assertThat(response.getUsername()).isEqualTo(username);
        verify(usuarioService, times(1)).obtenerPorUsername(username);
    }

    @Test
    void actualizarTest() {
        // Arrange
        Long id = 10L;
        ActualizarUsuarioRequest request = ActualizarUsuarioRequest.builder()
                .username("nuevo")
                .email("nuevo@example.com")
                .activo(true)
                .build();

        UsuarioResponse actualizado = UsuarioResponse.builder()
                .id(id)
                .username("nuevo")
                .email("nuevo@example.com")
                .activo(true)
                .build();

        when(usuarioService.actualizar(id, request)).thenReturn(actualizado);

        // Act
        UsuarioResponse response = usuarioController.actualizar(id, request);

        // Assert
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getUsername()).isEqualTo("nuevo");
        verify(usuarioService, times(1)).actualizar(id, request);
    }

    @Test
    void eliminarTest() {
        // Arrange
        Long id = 10L;

        // Act
        usuarioController.eliminar(id);

        // Assert
        verify(usuarioService, times(1)).eliminar(id);
    }
}
