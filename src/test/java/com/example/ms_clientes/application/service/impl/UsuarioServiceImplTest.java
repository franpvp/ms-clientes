package com.example.ms_clientes.application.service.impl;

import com.example.ms_clientes.application.dto.ActualizarUsuarioRequest;
import com.example.ms_clientes.application.dto.CrearUsuarioRequest;
import com.example.ms_clientes.application.dto.UsuarioResponse;
import com.example.ms_clientes.application.entity.TipoUsuarioEntity;
import com.example.ms_clientes.application.entity.UsuarioEntity;
import com.example.ms_clientes.application.repository.TipoUsuarioRepository;
import com.example.ms_clientes.application.repository.UsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceImplTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @InjectMocks
    private UsuarioServiceImpl usuarioServiceImpl;

    @Test
    void crearTest() {
        // Arrange
        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setIdTipoUsuario(1L);
        request.setUsername("fran");
        request.setEmail("fran@example.com");
        request.setActivo(true);

        TipoUsuarioEntity tipo = new TipoUsuarioEntity();
        tipo.setId(1L);
        tipo.setNombre("CLIENTE");

        when(tipoUsuarioRepository.findById(1L)).thenReturn(Optional.of(tipo));
        when(usuarioRepository.save(any(UsuarioEntity.class))).thenAnswer(inv -> {
            UsuarioEntity e = inv.getArgument(0);
            e.setId(10L);
            return e;
        });

        // Act
        UsuarioResponse response = usuarioServiceImpl.crear(request);

        // Assert
        ArgumentCaptor<UsuarioEntity> captor = ArgumentCaptor.forClass(UsuarioEntity.class);
        verify(tipoUsuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(captor.capture());

        UsuarioEntity saved = captor.getValue();
        assertThat(saved.getTipoUsuario()).isEqualTo(tipo);
        assertThat(saved.getUsername()).isEqualTo("fran");
        assertThat(saved.getEmail()).isEqualTo("fran@example.com");
        assertThat(saved.getActivo()).isTrue();

        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getUsername()).isEqualTo("fran");
        assertThat(response.getEmail()).isEqualTo("fran@example.com");
    }

    @Test
    void crearTipoUsuarioNoEncontradoTest() {
        // Arrange
        CrearUsuarioRequest request = new CrearUsuarioRequest();
        request.setIdTipoUsuario(99L);

        when(tipoUsuarioRepository.findById(99L)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class,
                () -> usuarioServiceImpl.crear(request));

        verify(usuarioRepository, never()).save(any(UsuarioEntity.class));
    }

    @Test
    void obtenerPorIdTest() {
        // Arrange
        Long id = 10L;
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(id);
        entity.setUsername("fran");
        entity.setEmail("fran@example.com");
        entity.setActivo(true);
        entity.setFechaCreacion(LocalDateTime.now());

        when(usuarioRepository.findById(id)).thenReturn(Optional.of(entity));

        // Act
        UsuarioResponse response = usuarioServiceImpl.obtenerPorId(id);

        // Assert
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getUsername()).isEqualTo("fran");
        assertThat(response.getEmail()).isEqualTo("fran@example.com");
        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    void obtenerPorIdNoEncontradoTest() {
        // Arrange
        Long id = 999L;
        when(usuarioRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class,
                () -> usuarioServiceImpl.obtenerPorId(id));

        verify(usuarioRepository, times(1)).findById(id);
    }

    @Test
    void obtenerPorEmailTest() {
        // Arrange
        String email = "fran@example.com";
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(10L);
        entity.setEmail(email);

        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.of(entity));

        // Act
        UsuarioResponse response = usuarioServiceImpl.obtenerPorEmail(email);

        // Assert
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getEmail()).isEqualTo(email);
        verify(usuarioRepository, times(1)).findByEmail(email);
    }

    @Test
    void obtenerPorEmailNoEncontradoTest() {
        // Arrange
        String email = "no-existe@example.com";
        when(usuarioRepository.findByEmail(email)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class,
                () -> usuarioServiceImpl.obtenerPorEmail(email));

        verify(usuarioRepository, times(1)).findByEmail(email);
    }

    @Test
    void obtenerPorUsernameTest() {
        // Arrange
        String username = "fran";
        UsuarioEntity entity = new UsuarioEntity();
        entity.setId(10L);
        entity.setUsername(username);

        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.of(entity));

        // Act
        UsuarioResponse response = usuarioServiceImpl.obtenerPorUsername(username);

        // Assert
        assertThat(response.getId()).isEqualTo(10L);
        assertThat(response.getUsername()).isEqualTo(username);
        verify(usuarioRepository, times(1)).findByUsername(username);
    }

    @Test
    void obtenerPorUsernameNoEncontradoTest() {
        // Arrange
        String username = "no-existe";
        when(usuarioRepository.findByUsername(username)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class,
                () -> usuarioServiceImpl.obtenerPorUsername(username));

        verify(usuarioRepository, times(1)).findByUsername(username);
    }

    @Test
    void listarTodosTest() {
        // Arrange
        UsuarioEntity u1 = new UsuarioEntity();
        u1.setId(1L);
        u1.setUsername("u1");

        UsuarioEntity u2 = new UsuarioEntity();
        u2.setId(2L);
        u2.setUsername("u2");

        when(usuarioRepository.findAll()).thenReturn(List.of(u1, u2));

        // Act
        List<UsuarioResponse> result = usuarioServiceImpl.listarTodos();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(UsuarioResponse::getUsername)
                .containsExactlyInAnyOrder("u1", "u2");
    }


    @Test
    void eliminarTest() {
        // Arrange
        Long id = 10L;
        when(usuarioRepository.existsById(id)).thenReturn(true);

        // Act
        usuarioServiceImpl.eliminar(id);

        // Assert
        verify(usuarioRepository, times(1)).existsById(id);
        verify(usuarioRepository, times(1)).deleteById(id);
    }

    @Test
    void eliminarNoEncontradoTest() {
        // Arrange
        Long id = 999L;
        when(usuarioRepository.existsById(id)).thenReturn(false);

        // Act + Assert
        assertThrows(EntityNotFoundException.class,
                () -> usuarioServiceImpl.eliminar(id));

        verify(usuarioRepository, times(1)).existsById(id);
        verify(usuarioRepository, never()).deleteById(anyLong());
    }
}
