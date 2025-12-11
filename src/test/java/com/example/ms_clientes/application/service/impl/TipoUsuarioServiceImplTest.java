package com.example.ms_clientes.application.service.impl;

import com.example.ms_clientes.application.dto.ActualizarTipoUsuarioRequest;
import com.example.ms_clientes.application.dto.CrearTipoUsuarioRequest;
import com.example.ms_clientes.application.dto.TipoUsuarioResponse;
import com.example.ms_clientes.application.entity.TipoUsuarioEntity;
import com.example.ms_clientes.application.repository.TipoUsuarioRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TipoUsuarioServiceImplTest {

    @Mock
    private TipoUsuarioRepository tipoUsuarioRepository;

    @InjectMocks
    private TipoUsuarioServiceImpl tipoUsuarioServiceImpl;

    @Test
    void crearTest() {
        // Arrange
        CrearTipoUsuarioRequest request = CrearTipoUsuarioRequest.builder()
                .nombreTipo("CLIENTE")
                .descripcion("Cliente final")
                .build();

        when(tipoUsuarioRepository.existsByNombre("CLIENTE")).thenReturn(false);
        when(tipoUsuarioRepository.save(any(TipoUsuarioEntity.class)))
                .thenAnswer(inv -> {
                    TipoUsuarioEntity e = inv.getArgument(0);
                    e.setId(1L);
                    return e;
                });

        // Act
        TipoUsuarioResponse response = tipoUsuarioServiceImpl.crear(request);

        // Assert
        ArgumentCaptor<TipoUsuarioEntity> captor = ArgumentCaptor.forClass(TipoUsuarioEntity.class);
        verify(tipoUsuarioRepository, times(1)).existsByNombre("CLIENTE");
        verify(tipoUsuarioRepository, times(1)).save(captor.capture());

        TipoUsuarioEntity saved = captor.getValue();
        assertThat(saved.getNombre()).isEqualTo("CLIENTE");
        assertThat(saved.getDescripcion()).isEqualTo("Cliente final");

        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNombreTipo()).isEqualTo("CLIENTE");
    }

    @Test
    void crearNombreDuplicadoTest() {
        // Arrange
        CrearTipoUsuarioRequest request = CrearTipoUsuarioRequest.builder()
                .nombreTipo("ADMIN")
                .descripcion("Administrador")
                .build();

        when(tipoUsuarioRepository.existsByNombre("ADMIN")).thenReturn(true);

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> tipoUsuarioServiceImpl.crear(request));

        verify(tipoUsuarioRepository, times(1)).existsByNombre("ADMIN");
        verify(tipoUsuarioRepository, never()).save(any(TipoUsuarioEntity.class));
    }

    @Test
    void obtenerPorIdTest() {
        // Arrange
        Long id = 1L;
        TipoUsuarioEntity entity = new TipoUsuarioEntity();
        entity.setId(id);
        entity.setNombre("CLIENTE");
        entity.setDescripcion("Cliente final");

        when(tipoUsuarioRepository.findById(id)).thenReturn(Optional.of(entity));

        // Act
        TipoUsuarioResponse response = tipoUsuarioServiceImpl.obtenerPorId(id);

        // Assert
        assertThat(response.getId()).isEqualTo(id);
        assertThat(response.getNombreTipo()).isEqualTo("CLIENTE");
        verify(tipoUsuarioRepository, times(1)).findById(id);
    }

    @Test
    void obtenerPorIdNoEncontradoTest() {
        // Arrange
        Long id = 999L;
        when(tipoUsuarioRepository.findById(id)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class,
                () -> tipoUsuarioServiceImpl.obtenerPorId(id));

        verify(tipoUsuarioRepository, times(1)).findById(id);
    }

    @Test
    void obtenerPorNombreTest() {
        // Arrange
        String nombre = "CLIENTE";
        TipoUsuarioEntity entity = new TipoUsuarioEntity();
        entity.setId(1L);
        entity.setNombre(nombre);

        when(tipoUsuarioRepository.findByNombre(nombre)).thenReturn(Optional.of(entity));

        // Act
        TipoUsuarioResponse response = tipoUsuarioServiceImpl.obtenerPorNombre(nombre);

        // Assert
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getNombreTipo()).isEqualTo(nombre);
        verify(tipoUsuarioRepository, times(1)).findByNombre(nombre);
    }

    @Test
    void obtenerPorNombreNoEncontradoTest() {
        // Arrange
        String nombre = "NO_EXISTE";
        when(tipoUsuarioRepository.findByNombre(nombre)).thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(EntityNotFoundException.class,
                () -> tipoUsuarioServiceImpl.obtenerPorNombre(nombre));

        verify(tipoUsuarioRepository, times(1)).findByNombre(nombre);
    }

    @Test
    void listarTodosTest() {
        // Arrange
        TipoUsuarioEntity t1 = new TipoUsuarioEntity();
        t1.setId(1L);
        t1.setNombre("CLIENTE");

        TipoUsuarioEntity t2 = new TipoUsuarioEntity();
        t2.setId(2L);
        t2.setNombre("ADMIN");

        when(tipoUsuarioRepository.findAll()).thenReturn(List.of(t1, t2));

        // Act
        List<TipoUsuarioResponse> result = tipoUsuarioServiceImpl.listarTodos();

        // Assert
        assertThat(result).hasSize(2);
        assertThat(result).extracting(TipoUsuarioResponse::getNombreTipo)
                .containsExactlyInAnyOrder("CLIENTE", "ADMIN");
    }

    @Test
    void actualizarTest() {
        // Arrange
        Long id = 1L;

        TipoUsuarioEntity existing = new TipoUsuarioEntity();
        existing.setId(id);
        existing.setNombre("OLD");
        existing.setDescripcion("Old desc");

        ActualizarTipoUsuarioRequest request = ActualizarTipoUsuarioRequest.builder()
                .nombreTipo("NEW")
                .descripcion("New desc")
                .build();

        when(tipoUsuarioRepository.findById(id)).thenReturn(Optional.of(existing));
        when(tipoUsuarioRepository.findByNombre("NEW"))
                .thenReturn(Optional.empty());
        when(tipoUsuarioRepository.save(any(TipoUsuarioEntity.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // Act
        TipoUsuarioResponse response = tipoUsuarioServiceImpl.actualizar(id, request);

        // Assert
        ArgumentCaptor<TipoUsuarioEntity> captor = ArgumentCaptor.forClass(TipoUsuarioEntity.class);
        verify(tipoUsuarioRepository, times(1)).save(captor.capture());

        TipoUsuarioEntity updated = captor.getValue();
        assertThat(updated.getNombre()).isEqualTo("NEW");
        assertThat(updated.getDescripcion()).isEqualTo("New desc");

        assertThat(response.getNombreTipo()).isEqualTo("NEW");
    }

    @Test
    void actualizarNombreDuplicadoTest() {
        // Arrange
        Long id = 1L;

        TipoUsuarioEntity existing = new TipoUsuarioEntity();
        existing.setId(id);
        existing.setNombre("OLD");

        TipoUsuarioEntity other = new TipoUsuarioEntity();
        other.setId(2L);
        other.setNombre("NEW");

        ActualizarTipoUsuarioRequest request = ActualizarTipoUsuarioRequest.builder()
                .nombreTipo("NEW")
                .descripcion("New desc")
                .build();

        when(tipoUsuarioRepository.findById(id)).thenReturn(Optional.of(existing));
        when(tipoUsuarioRepository.findByNombre("NEW"))
                .thenReturn(Optional.of(other));

        // Act + Assert
        assertThrows(IllegalArgumentException.class,
                () -> tipoUsuarioServiceImpl.actualizar(id, request));

        verify(tipoUsuarioRepository, times(1)).findById(id);
        verify(tipoUsuarioRepository, times(1)).findByNombre("NEW");
        verify(tipoUsuarioRepository, never()).save(any(TipoUsuarioEntity.class));
    }

    @Test
    void eliminarTest() {
        // Arrange
        Long id = 1L;
        when(tipoUsuarioRepository.existsById(id)).thenReturn(true);

        // Act
        tipoUsuarioServiceImpl.eliminar(id);

        // Assert
        verify(tipoUsuarioRepository, times(1)).existsById(id);
        verify(tipoUsuarioRepository, times(1)).deleteById(id);
    }

    @Test
    void eliminarNoEncontradoTest() {
        // Arrange
        Long id = 999L;
        when(tipoUsuarioRepository.existsById(id)).thenReturn(false);

        // Act + Assert
        assertThrows(EntityNotFoundException.class,
                () -> tipoUsuarioServiceImpl.eliminar(id));

        verify(tipoUsuarioRepository, times(1)).existsById(id);
        verify(tipoUsuarioRepository, never()).deleteById(anyLong());
    }
}
