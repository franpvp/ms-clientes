package com.example.ms_clientes.application.controller;

import com.example.ms_clientes.application.dto.ActualizarTipoUsuarioRequest;
import com.example.ms_clientes.application.dto.CrearTipoUsuarioRequest;
import com.example.ms_clientes.application.dto.TipoUsuarioResponse;
import com.example.ms_clientes.application.service.TipoUsuarioService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/roles")
@CrossOrigin
@RequiredArgsConstructor
public class TipoUsuarioController {

    private final TipoUsuarioService tipoUsuarioService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TipoUsuarioResponse crear(@RequestBody CrearTipoUsuarioRequest request) {
        return tipoUsuarioService.crear(request);
    }

    @GetMapping("/{id}")
    public TipoUsuarioResponse obtenerPorId(@PathVariable Long id) {
        return tipoUsuarioService.obtenerPorId(id);
    }

    @GetMapping
    public List<TipoUsuarioResponse> listarTodos() {
        return tipoUsuarioService.listarTodos();
    }

    @GetMapping("/buscar")
    public TipoUsuarioResponse obtenerPorNombre(@RequestParam String nombreTipo) {
        return tipoUsuarioService.obtenerPorNombre(nombreTipo);
    }

    @PutMapping("/{id}")
    public TipoUsuarioResponse actualizar(
            @PathVariable Long id,
            @RequestBody ActualizarTipoUsuarioRequest request
    ) {
        return tipoUsuarioService.actualizar(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void eliminar(@PathVariable Long id) {
        tipoUsuarioService.eliminar(id);
    }
}
