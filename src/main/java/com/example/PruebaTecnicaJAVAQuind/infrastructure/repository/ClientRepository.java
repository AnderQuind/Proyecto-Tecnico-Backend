package com.example.PruebaTecnicaJAVAQuind.infrastructure.repository;

import com.example.PruebaTecnicaJAVAQuind.infrastructure.entity.ClientEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ClientRepository extends JpaRepository<ClientEntity, String> {
}
