package com.example.ms_clientes.application.service.impl;

import com.example.ms_clientes.application.dto.ActualizarTipoUsuarioRequest;
import com.example.ms_clientes.application.dto.CrearTipoUsuarioRequest;
import com.example.ms_clientes.application.dto.TipoUsuarioResponse;
import com.example.ms_clientes.application.entity.TipoUsuarioEntity;
import com.example.ms_clientes.application.mapper.TipoUsuarioMapper;
import com.example.ms_clientes.application.repository.TipoUsuarioRepository;
import com.example.ms_clientes.application.service.TipoUsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TipoUsuarioServiceImpl implements TipoUsuarioService {

    private final TipoUsuarioRepository tipoUsuarioRepository;

    @Override
    public TipoUsuarioResponse crear(CrearTipoUsuarioRequest request) {

        if (tipoUsuarioRepository.existsByNombreTipo(request.getNombreTipo())) {
            throw new IllegalArgumentException(
                    "Ya existe un tipo de usuario con nombre: " + request.getNombreTipo()
            );
        }

        TipoUsuarioEntity entity = TipoUsuarioEntity.builder()
                .nombreTipo(request.getNombreTipo())
                .descripcion(request.getDescripcion())
                .build();

        TipoUsuarioEntity saved = tipoUsuarioRepository.save(entity);
        return TipoUsuarioMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public TipoUsuarioResponse obtenerPorId(Long id) {
        TipoUsuarioEntity entity = tipoUsuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Tipo de usuario no encontrado con id=" + id));

        return TipoUsuarioMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public TipoUsuarioResponse obtenerPorNombre(String nombreTipo) {
        TipoUsuarioEntity entity = tipoUsuarioRepository.findByNombreTipo(nombreTipo)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Tipo de usuario no encontrado con nombre=" + nombreTipo));

        return TipoUsuarioMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public List<TipoUsuarioResponse> listarTodos() {
        return tipoUsuarioRepository.findAll()
                .stream()
                .map(TipoUsuarioMapper::toResponse)
                .toList();
    }

    @Override
    public TipoUsuarioResponse actualizar(Long id, ActualizarTipoUsuarioRequest request) {
        TipoUsuarioEntity entity = tipoUsuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Tipo de usuario no encontrado con id=" + id));

        tipoUsuarioRepository.findByNombreTipo(request.getNombreTipo())
                .filter(otro -> !otro.getId().equals(id))
                .ifPresent(otro -> {
                    throw new IllegalArgumentException(
                            "Ya existe otro tipo de usuario con nombre: " + request.getNombreTipo()
                    );
                });

        entity.setNombreTipo(request.getNombreTipo());
        entity.setDescripcion(request.getDescripcion());

        TipoUsuarioEntity updated = tipoUsuarioRepository.save(entity);
        return TipoUsuarioMapper.toResponse(updated);
    }

    @Override
    public void eliminar(Long id) {
        if (!tipoUsuarioRepository.existsById(id)) {
            throw new EntityNotFoundException(
                    "Tipo de usuario no encontrado con id=" + id);
        }
        tipoUsuarioRepository.deleteById(id);
    }
}
