package com.example.ms_clientes.application.repository;

import com.example.ms_clientes.application.entity.TipoUsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TipoUsuarioRepository extends JpaRepository<TipoUsuarioEntity, Long> {

    Optional<TipoUsuarioEntity> findByNombreTipo(String nombreTipo);

    boolean existsByNombreTipo(String nombreTipo);
}
