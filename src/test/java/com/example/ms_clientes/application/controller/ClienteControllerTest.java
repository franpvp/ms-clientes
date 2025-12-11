package com.example.ms_clientes.application.controller;

import com.example.ms_clientes.application.dto.ActualizarClienteRequest;
import com.example.ms_clientes.application.dto.ClienteResponse;
import com.example.ms_clientes.application.dto.CrearClienteRequest;
import com.example.ms_clientes.application.service.ClienteService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteControllerTest {

    @Mock
    private ClienteService clienteService;

    @InjectMocks
    private ClienteController clienteController;

    @Test
    void crearTest() {
        // Arrange
        CrearClienteRequest request = new CrearClienteRequest();
        request.setNombre("Fran");

        ClienteResponse responseService = ClienteResponse.builder()
                .id(10L)
                .nombre("Fran")
                .apellido("Valdivia")
                .email("fran@example.com")
                .fechaRegistro(LocalDateTime.now())
                .build();

        when(clienteService.crear(request)).thenReturn(responseService);

        // Act
        ClienteResponse response = clienteController.crear(request);

        // Assert
        assertThat(response).isSameAs(responseService);
        verify(clienteService, times(1)).crear(request);
    }

    @Test
    void obtenerPorIdTest() {
        // Arrange
        Long id = 10L;
        ClienteResponse responseService = ClienteResponse.builder()
                .id(id)
                .nombre("Fran")
                .build();

        when(clienteService.obtenerPorId(id)).thenReturn(responseService);

        // Act
        ClienteResponse response = clienteController.obtenerPorId(id);

        // Assert
        assertThat(response.getId()).isEqualTo(id);
        verify(clienteService, times(1)).obtenerPorId(id);
    }

    @Test
    void obtenerPorEmailTest() {
        // Arrange
        String email = "fran@example.com";
        ClienteResponse responseService = ClienteResponse.builder()
                .id(10L)
                .email(email)
                .build();

        when(clienteService.obtenerPorEmail(email)).thenReturn(responseService);

        // Act
        ClienteResponse response = clienteController.obtenerPorEmail(email);

        // Assert
        assertThat(response.getEmail()).isEqualTo(email);
        verify(clienteService, times(1)).obtenerPorEmail(email);
    }

    @Test
    void listarTodosTest() {
        // Arrange
        ClienteResponse c1 = ClienteResponse.builder().id(1L).build();
        ClienteResponse c2 = ClienteResponse.builder().id(2L).build();

        when(clienteService.listarTodos()).thenReturn(List.of(c1, c2));

        // Act
        List<ClienteResponse> result = clienteController.listarTodos();

        // Assert
        assertThat(result).hasSize(2);
        verify(clienteService, times(1)).listarTodos();
    }

    @Test
    void actualizarTest() {
        // Arrange
        Long id = 10L;
        ActualizarClienteRequest request = ActualizarClienteRequest.builder()
                .nombre("NuevoNombre")
                .build();

        ClienteResponse actualizado = ClienteResponse.builder()
                .id(id)
                .nombre("NuevoNombre")
                .build();

        when(clienteService.actualizar(id, request)).thenReturn(actualizado);

        // Act
        ClienteResponse response = clienteController.actualizar(id, request);

        // Assert
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getNombre()).isEqualTo("NuevoNombre");
        verify(clienteService, times(1)).actualizar(id, request);
    }

    @Test
    void eliminarTest() {
        // Arrange
        Long id = 10L;

        // Act
        clienteController.eliminar(id);

        // Assert
        verify(clienteService, times(1)).eliminar(id);
    }

    @Test
    void sincronizarClienteAutenticadoTest() {
        // Arrange
        ClienteResponse responseService = ClienteResponse.builder()
                .id(20L)
                .email("token-user@example.com")
                .build();

        when(clienteService.sincronizarClienteAutenticado())
                .thenReturn(responseService);

        // Act
        ClienteResponse response = clienteController.sincronizarClienteAutenticado();

        // Assert
        assertThat(response).isSameAs(responseService);
        verify(clienteService, times(1)).sincronizarClienteAutenticado();
    }
}
