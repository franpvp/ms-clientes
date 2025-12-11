package com.example.ms_clientes.application.controller;

import com.example.ms_clientes.application.dto.ActualizarTipoUsuarioRequest;
import com.example.ms_clientes.application.dto.CrearTipoUsuarioRequest;
import com.example.ms_clientes.application.dto.TipoUsuarioResponse;
import com.example.ms_clientes.application.service.TipoUsuarioService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoUsuarioControllerTest {

    @Mock
    private TipoUsuarioService tipoUsuarioService;

    @InjectMocks
    private TipoUsuarioController tipoUsuarioController;

    @Test
    void crearTest() {
        // Arrange
        CrearTipoUsuarioRequest request = CrearTipoUsuarioRequest.builder()
                .nombreTipo("CLIENTE")
                .descripcion("Cliente final")
                .build();

        TipoUsuarioResponse responseService = TipoUsuarioResponse.builder()
                .id(1L)
                .nombreTipo("CLIENTE")
                .descripcion("Cliente final")
                .build();

        when(tipoUsuarioService.crear(request)).thenReturn(responseService);

        // Act
        TipoUsuarioResponse response = tipoUsuarioController.crear(request);

        // Assert
        assertThat(response).isSameAs(responseService);
        verify(tipoUsuarioService, times(1)).crear(request);
    }

    @Test
    void obtenerPorIdTest() {
        // Arrange
        Long id = 1L;
        TipoUsuarioResponse responseService = TipoUsuarioResponse.builder()
                .id(id)
                .nombreTipo("CLIENTE")
                .build();

        when(tipoUsuarioService.obtenerPorId(id)).thenReturn(responseService);

        // Act
        TipoUsuarioResponse response = tipoUsuarioController.obtenerPorId(id);

        // Assert
        assertThat(response.getId()).isEqualTo(id);
        verify(tipoUsuarioService, times(1)).obtenerPorId(id);
    }

    @Test
    void listarTodosTest() {
        // Arrange
        TipoUsuarioResponse t1 = TipoUsuarioResponse.builder().id(1L).build();
        TipoUsuarioResponse t2 = TipoUsuarioResponse.builder().id(2L).build();

        when(tipoUsuarioService.listarTodos()).thenReturn(List.of(t1, t2));

        // Act
        List<TipoUsuarioResponse> result = tipoUsuarioController.listarTodos();

        // Assert
        assertThat(result).hasSize(2);
        verify(tipoUsuarioService, times(1)).listarTodos();
    }

    @Test
    void obtenerPorNombreTest() {
        // Arrange
        String nombreTipo = "CLIENTE";
        TipoUsuarioResponse responseService = TipoUsuarioResponse.builder()
                .id(1L)
                .nombreTipo(nombreTipo)
                .build();

        when(tipoUsuarioService.obtenerPorNombre(nombreTipo))
                .thenReturn(responseService);

        // Act
        TipoUsuarioResponse response = tipoUsuarioController.obtenerPorNombre(nombreTipo);

        // Assert
        assertThat(response.getNombreTipo()).isEqualTo(nombreTipo);
        verify(tipoUsuarioService, times(1)).obtenerPorNombre(nombreTipo);
    }

    @Test
    void actualizarTest() {
        // Arrange
        Long id = 1L;
        ActualizarTipoUsuarioRequest request = ActualizarTipoUsuarioRequest.builder()
                .nombreTipo("NEW")
                .descripcion("New desc")
                .build();

        TipoUsuarioResponse actualizado = TipoUsuarioResponse.builder()
                .id(id)
                .nombreTipo("NEW")
                .descripcion("New desc")
                .build();

        when(tipoUsuarioService.actualizar(id, request)).thenReturn(actualizado);

        // Act
        TipoUsuarioResponse response = tipoUsuarioController.actualizar(id, request);

        // Assert
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getNombreTipo()).isEqualTo("NEW");
        verify(tipoUsuarioService, times(1)).actualizar(id, request);
    }

    @Test
    void eliminarTest() {
        // Arrange
        Long id = 1L;

        // Act
        tipoUsuarioController.eliminar(id);

        // Assert
        verify(tipoUsuarioService, times(1)).eliminar(id);
    }
}
