package com.example.ms_clientes.application.repository;


import com.example.ms_clientes.application.entity.ClienteEntity;
import com.example.ms_clientes.application.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<ClienteEntity, Long> {

    Optional<ClienteEntity> findByUsuario(UsuarioEntity usuario);
}
