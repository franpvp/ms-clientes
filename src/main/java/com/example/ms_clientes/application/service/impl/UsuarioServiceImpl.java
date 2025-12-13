package com.example.ms_clientes.application.service.impl;

import com.example.ms_clientes.application.dto.ActualizarClienteRequest;
import com.example.ms_clientes.application.dto.ActualizarUsuarioRequest;
import com.example.ms_clientes.application.dto.CrearUsuarioRequest;
import com.example.ms_clientes.application.dto.UsuarioResponse;
import com.example.ms_clientes.application.entity.TipoUsuarioEntity;
import com.example.ms_clientes.application.entity.UsuarioEntity;
import com.example.ms_clientes.application.mapper.UsuarioMapper;
import com.example.ms_clientes.application.repository.TipoUsuarioRepository;
import com.example.ms_clientes.application.repository.UsuarioRepository;
import com.example.ms_clientes.application.service.UsuarioService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final TipoUsuarioRepository tipoUsuarioRepository;

    @Override
    public UsuarioResponse crear(CrearUsuarioRequest request) {

        TipoUsuarioEntity tipo = tipoUsuarioRepository.findById(request.getIdTipoUsuario())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Tipo de usuario no encontrado con id=" + request.getIdTipoUsuario()));

        UsuarioEntity entity = UsuarioEntity.builder()
                .tipoUsuario(tipo)
                .username(request.getUsername())
                .email(request.getEmail())
                .activo(request.getActivo())
                .fechaCreacion(LocalDateTime.now())
                .build();

        UsuarioEntity saved = usuarioRepository.save(entity);

        return UsuarioMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public UsuarioResponse obtenerPorId(Long id) {
        UsuarioEntity entity = usuarioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con id=" + id));

        return UsuarioMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public UsuarioResponse obtenerPorEmail(String email) {
        UsuarioEntity entity = usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con email=" + email));

        return UsuarioMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public UsuarioResponse obtenerPorUsername(String username) {
        UsuarioEntity entity = usuarioRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("Usuario no encontrado con username=" + username));

        return UsuarioMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public List<UsuarioResponse> listarTodos() {
        return usuarioRepository.findAll()
                .stream()
                .map(UsuarioMapper::toResponse)
                .toList();
    }

    @Override
    public void eliminar(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new EntityNotFoundException("Usuario no encontrado con id=" + id);
        }

        usuarioRepository.deleteById(id);
    }
}
