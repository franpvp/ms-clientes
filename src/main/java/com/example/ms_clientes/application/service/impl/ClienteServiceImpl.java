package com.example.ms_clientes.application.service.impl;

import com.example.ms_clientes.application.dto.ActualizarClienteRequest;
import com.example.ms_clientes.application.dto.ClienteResponse;
import com.example.ms_clientes.application.dto.ContactoRequestDto;
import com.example.ms_clientes.application.dto.CrearClienteRequest;
import com.example.ms_clientes.application.dto.UsuarioResponse;
import com.example.ms_clientes.application.entity.ClienteEntity;
import com.example.ms_clientes.application.entity.TipoUsuarioEntity;
import com.example.ms_clientes.application.entity.UsuarioEntity;
import com.example.ms_clientes.application.mapper.ClienteMapper;
import com.example.ms_clientes.application.mapper.UsuarioMapper;
import com.example.ms_clientes.application.repository.UsuarioRepository;
import com.example.ms_clientes.application.service.ClienteService;
import com.example.ms_clientes.application.repository.ClienteRepository;
import com.example.ms_clientes.application.util.SecurityUtil;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ClienteServiceImpl implements ClienteService {

    @Value("${app.contacto.destino}")
    private String destino;

    private final ClienteRepository clienteRepository;
    private final UsuarioRepository usuarioRepository;
    private final EmailService emailService;
    private final EmailTemplateService templateService;

    @Override
    public ClienteResponse crear(CrearClienteRequest request) {
        UsuarioEntity usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cliente no encontrado con id " + request.getIdUsuario()
                ));

        ClienteEntity entity = ClienteEntity.builder()
                .usuario(usuario)
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .telefono(request.getTelefono())
                .email(request.getEmail())
                .direccion(request.getDireccion())
                .ciudad(request.getCiudad())
                .fechaRegistro(LocalDateTime.now())
                .build();

        ClienteEntity saved = clienteRepository.save(entity);
        return ClienteMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public ClienteResponse obtenerPorId(Long idCliente) {
        ClienteEntity entity = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cliente no encontrado con id " + idCliente
                ));
        return ClienteMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public ClienteResponse obtenerPorEmail(String email) {
        ClienteEntity entity = clienteRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("Cliente no encontrado con email=" + email));

        return ClienteMapper.toResponse(entity);
    }

    @Override
    @Transactional
    public List<ClienteResponse> listarTodos() {
        return clienteRepository.findAll()
                .stream()
                .map(ClienteMapper::toResponse)
                .toList();
    }

    @Override
    public ClienteResponse actualizar(Long idCliente, ActualizarClienteRequest request) {
        ClienteEntity entity = clienteRepository.findById(idCliente)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Cliente no encontrado con id " + idCliente
                ));

        entity.setNombre(request.getNombre());
        entity.setApellido(request.getApellido());
        entity.setTelefono(request.getTelefono());
        entity.setDireccion(request.getDireccion());
        entity.setCiudad(request.getCiudad());

        ClienteEntity updated = clienteRepository.save(entity);
        return ClienteMapper.toResponse(updated);
    }

    @Override
    public void eliminar(Long idCliente) {
        if (!clienteRepository.existsById(idCliente)) {
            throw new EntityNotFoundException("Cliente no encontrado con id " + idCliente);
        }
        clienteRepository.deleteById(idCliente);
    }

    @Override
    public ClienteResponse sincronizarClienteAutenticado() {

        String email = SecurityUtil.obtenerEmailDelToken();
        String nombreCompleto = SecurityUtil.obtenerNombreDelToken();

        UsuarioEntity usuario = usuarioRepository.findByEmail(email)
                .orElseGet(() -> {
                    UsuarioEntity nuevo = new UsuarioEntity();
                    nuevo.setEmail(email);
                    nuevo.setUsername(nombreCompleto);
                    nuevo.setTipoUsuario(TipoUsuarioEntity.builder().id(1L).build());
                    nuevo.setFechaCreacion(LocalDateTime.now());
                    nuevo.setActivo(true);
                    return usuarioRepository.save(nuevo);
                });

        ClienteEntity cliente = clienteRepository.findByUsuario(usuario)
                .orElseGet(() -> {

                    // Separar nombre y apellido (Azure entrega "NOMBRE APELLIDO")
                    String[] partes = nombreCompleto.split(" ", 2);
                    String nombre = partes.length > 0 ? partes[0] : "Pendiente";
                    String apellido = partes.length > 1 ? partes[1] : "Pendiente";

                    ClienteEntity nuevoCliente = ClienteEntity.builder()
                            .usuario(usuario)
                            .nombre(nombre)
                            .apellido(apellido)
                            .email(email)
                            .fechaRegistro(LocalDateTime.now())
                            .build();

                    return clienteRepository.save(nuevoCliente);
                });

        return ClienteMapper.toResponse(cliente);
    }

    public void enviarMensaje(ContactoRequestDto req) {
        String subject = "Contacto TechFactory - " + (req.getNombre() == null ? "Usuario" : req.getNombre());
        String html = templateService.templateContacto(req.getNombre(), req.getEmail(), req.getMensaje());

        emailService.enviarCorreoHtml(destino, subject, html);
    }

}
