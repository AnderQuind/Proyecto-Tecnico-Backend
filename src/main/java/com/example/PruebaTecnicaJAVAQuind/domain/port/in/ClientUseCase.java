package com.example.PruebaTecnicaJAVAQuind.domain.port.in;

import com.example.PruebaTecnicaJAVAQuind.domain.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientUseCase {
    Optional<Client> getClient(String id);
}
