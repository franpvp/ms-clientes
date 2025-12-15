package com.example.ms_clientes.application.service.impl;

import com.example.ms_clientes.application.dto.ActualizarClienteRequest;
import com.example.ms_clientes.application.dto.ClienteResponse;
import com.example.ms_clientes.application.dto.CrearClienteRequest;
import com.example.ms_clientes.application.entity.ClienteEntity;
import com.example.ms_clientes.application.entity.TipoUsuarioEntity;
import com.example.ms_clientes.application.entity.UsuarioEntity;
import com.example.ms_clientes.application.mapper.ClienteMapper;
import com.example.ms_clientes.application.repository.ClienteRepository;
import com.example.ms_clientes.application.repository.UsuarioRepository;
import com.example.ms_clientes.application.util.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClienteServiceImplTest {

    @Mock
    private ClienteRepository clienteRepository;

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private ClienteServiceImpl clienteServiceImpl;

    @Test
    void crearTest() {
        // Arrange
        CrearClienteRequest request = new CrearClienteRequest();
        request.setIdUsuario(1L);
        request.setNombre("Francisca");
        request.setApellido("Valdivia");
        request.setTelefono("123456789");
        request.setEmail("fran@example.com");
        request.setDireccion("Calle Falsa 123");
        request.setCiudad("Santiago");

        UsuarioEntity usuario = new UsuarioEntity();
        usuario.setId(1L);

        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(clienteRepository.save(any(ClienteEntity.class))).thenAnswer(invocation -> {
            ClienteEntity e = invocation.getArgument(0);
            e.setId(10L);
            return e;
        });

        // Act
        ClienteResponse response = clienteServiceImpl.crear(request);

        // Assert
        ArgumentCaptor<ClienteEntity> captor = ArgumentCaptor.forClass(ClienteEntity.class);
        verify(usuarioRepository, times(1)).findById(1L);
        verify(clienteRepository, times(1)).save(captor.capture());

        ClienteEntity guardado = captor.getValue();
        assertThat(guardado.getUsuario()).isEqualTo(usuario);
        assertThat(guardado.getNombre()).isEqualTo("Francisca");
        assertThat(guardado.getApellido()).isEqualTo("Valdivia");
        assertThat(guardado.getTelefono()).isEqualTo("123456789");
        assertThat(guardado.getEmail()).isEqualTo("fran@example.com");
        assertThat(guardado.getDireccion()).isEqualTo("Calle Falsa 123");
        assertThat(guardado.getCiudad()).isEqualTo("Santiago");

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getNombre()).isEqualTo("Francisca");
        assertThat(response.getApellido()).isEqualTo("Valdivia");
    }

    @Test
    void crearUsuarioNoEncontradoTest() {
        // Arrange
        CrearClienteRequest request = new CrearClienteRequest();
        request.setIdUsuario(99L);

        when(usuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class,
                () -> clienteServiceImpl.crear(request));

        verify(clienteRepository, never()).save(any(ClienteEntity.class));
    }

    @Test
    void obtenerPorIdTest() {
        // Arrange
        Long id = 10L;
        ClienteEntity entity = new ClienteEntity();
        entity.setId(id);
        entity.setNombre("Fran");
        entity.setApellido("Valdivia");
        entity.setEmail("fran@example.com");

        when(clienteRepository.findById(id)).thenReturn(Optional.of(entity));

        // Act
        ClienteResponse response = clienteServiceImpl.obtenerPorId(id);

        // Assert
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getNombre()).isEqualTo("Fran");
        assertThat(response.getApellido()).isEqualTo("Valdivia");
        assertThat(response.getEmail()).isEqualTo("fran@example.com");

        verify(clienteRepository, times(1)).findById(id);
    }

    @Test
    void obtenerPorIdNoEncontradoTest() {
        // Arrange
        Long id = 999L;
        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class,
                () -> clienteServiceImpl.obtenerPorId(id));

        verify(clienteRepository, times(1)).findById(id);
    }

    @Test
    void obtenerPorEmailTest() {
        // Arrange
        String email = "fran@example.com";
        ClienteEntity entity = new ClienteEntity();
        entity.setId(10L);
        entity.setNombre("Fran");
        entity.setEmail(email);

        when(clienteRepository.findByEmail(email)).thenReturn(Optional.of(entity));

        // Act
        ClienteResponse response = clienteServiceImpl.obtenerPorEmail(email);

        // Assert
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getEmail()).isEqualTo(email);
        verify(clienteRepository, times(1)).findByEmail(email);
    }

    @Test
    void obtenerPorEmailNoEncontradoTest() {
        // Arrange
        String email = "no-existe@example.com";
        when(clienteRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class,
                () -> clienteServiceImpl.obtenerPorEmail(email));

        verify(clienteRepository, times(1)).findByEmail(email);
    }

    @Test
    void listarTodosTest() {
        // Arrange
        ClienteEntity c1 = new ClienteEntity();
        c1.setId(1L);
        c1.setNombre("A");

        ClienteEntity c2 = new ClienteEntity();
        c2.setId(2L);
        c2.setNombre("B");

        when(clienteRepository.findAll()).thenReturn(List.of(c1, c2));

        // Act
        List<ClienteResponse> result = clienteServiceImpl.listarTodos();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(ClienteResponse::getNombre)
                .containsExactlyInAnyOrder("A", "B");
    }

    @Test
    void actualizarTest() {
        // Arrange
        Long id = 10L;

        ClienteEntity entity = new ClienteEntity();
        entity.setId(id);
        entity.setNombre("Old");
        entity.setApellido("Old");
        entity.setTelefono("000");
        entity.setDireccion("Old");
        entity.setCiudad("Old");

        ActualizarClienteRequest request = ActualizarClienteRequest.builder()
                .nombre("NuevoNombre")
                .apellido("NuevoApellido")
                .telefono("123456789")
                .direccion("Nueva Direccion")
                .ciudad("Nueva Ciudad")
                .build();

        when(clienteRepository.findById(id)).thenReturn(Optional.of(entity));
        when(clienteRepository.save(any(ClienteEntity.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ClienteResponse response = clienteServiceImpl.actualizar(id, request);

        // Assert
        ArgumentCaptor<ClienteEntity> captor = ArgumentCaptor.forClass(ClienteEntity.class);
        verify(clienteRepository, times(1)).save(captor.capture());

        ClienteEntity updated = captor.getValue();
        assertThat(updated.getNombre()).isEqualTo("NuevoNombre");
        assertThat(updated.getApellido()).isEqualTo("NuevoApellido");
        assertThat(updated.getTelefono()).isEqualTo("123456789");
        assertThat(updated.getDireccion()).isEqualTo("Nueva Direccion");
        assertThat(updated.getCiudad()).isEqualTo("Nueva Ciudad");

        assertThat(response.getNombre()).isEqualTo("NuevoNombre");
    }

    @Test
    void actualizarNoEncontradoTest() {
        // Arrange
        Long id = 999L;
        ActualizarClienteRequest request = new ActualizarClienteRequest();

        when(clienteRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class,
                () -> clienteServiceImpl.actualizar(id, request));

        verify(clienteRepository, times(1)).findById(id);
        verify(clienteRepository, never()).save(any(ClienteEntity.class));
    }

    @Test
    void eliminarTest() {
        // Arrange
        Long id = 10L;
        when(clienteRepository.existsById(id)).thenReturn(true);

        // Act
        clienteServiceImpl.eliminar(id);

        // Assert
        verify(clienteRepository, times(1)).existsById(id);
        verify(clienteRepository, times(1)).deleteById(id);
    }

    @Test
    void eliminarNoEncontradoTest() {
        // Arrange
        Long id = 999L;
        when(clienteRepository.existsById(id)).thenReturn(false);

        // Act + Assert
        assertThrows(EntityNotFoundException.class,
                () -> clienteServiceImpl.eliminar(id));

        verify(clienteRepository, times(1)).existsById(id);
        verify(clienteRepository, never()).deleteById(anyLong());
    }

    @Test
    void sincronizarClienteAutenticadoTest() {
        // Arrange
        String email = "token-user@example.com";
        String nombreCompleto = "Nombre Apellido";

        try (MockedStatic<SecurityUtil> mocked = mockStatic(SecurityUtil.class)) {
            mocked.when(SecurityUtil::obtenerEmailDelToken).thenReturn(email);
            mocked.when(SecurityUtil::obtenerNombreDelToken).thenReturn(nombreCompleto);

            UsuarioEntity usuario = new UsuarioEntity();
            usuario.setId(5L);
            usuario.setEmail(email);
            usuario.setUsername(nombreCompleto);
            usuario.setTipoUsuario(TipoUsuarioEntity.builder().id(1L).build());
            usuario.setActivo(true);
            usuario.setFechaCreacion(LocalDateTime.now());

            when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(usuario));

            ClienteEntity cliente = new ClienteEntity();
            cliente.setId(20L);
            cliente.setUsuario(usuario);
            cliente.setNombre("Nombre");
            cliente.setApellido("Apellido");
            cliente.setEmail(email);
            cliente.setFechaRegistro(LocalDateTime.now());

            when(clienteRepository.findByUsuario(usuario))
                    .thenReturn(Optional.of(cliente));

            // Act
            ClienteResponse response = clienteServiceImpl.sincronizarClienteAutenticado();

            // Assert
            assertThat(response.getId()).isEqualTo(20L);
            assertThat(response.getNombre()).isEqualTo("Nombre");
            assertThat(response.getApellido()).isEqualTo("Apellido");

            verify(usuarioRepository, times(1)).findByEmail(email);
            verify(clienteRepository, times(1)).findByUsuario(usuario);
        }
    }
}
