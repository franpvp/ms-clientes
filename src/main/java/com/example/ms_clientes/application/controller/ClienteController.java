package com.example.ms_clientes.application.controller;

import com.example.ms_clientes.application.dto.ActualizarClienteRequest;
import com.example.ms_clientes.application.dto.ClienteResponse;
import com.example.ms_clientes.application.dto.CrearClienteRequest;
import com.example.ms_clientes.application.dto.UsuarioResponse;
import com.example.ms_clientes.application.service.ClienteService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/clientes")
@CrossOrigin
@RequiredArgsConstructor
public class ClienteController {

    private final ClienteService clienteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ClienteResponse crear(@RequestBody CrearClienteRequest request) {
        return clienteService.crear(request);
    }

    @GetMapping("/{id}")
    public ClienteResponse obtenerPorId(@PathVariable Long id) {
        return clienteService.obtenerPorId(id);
    }

    @GetMapping("/email/{email}")
    public ClienteResponse obtenerPorEmail(@PathVariable String email) {
        return clienteService.obtenerPorEmail(email);
    }

    @GetMapping
    public List<ClienteResponse> listarTodos() {
        return clienteService.listarTodos();
    }

    @PutMapping("/{id}")
    public ClienteResponse actualizar(
            @PathVariable Long id,
            @RequestBody ActualizarClienteRequest request
    ) {
        return clienteService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        clienteService.eliminar(id);
    }

    @PostMapping("/sync")
    public ClienteResponse sincronizarClienteAutenticado() {
        return clienteService.sincronizarClienteAutenticado();
    }

}