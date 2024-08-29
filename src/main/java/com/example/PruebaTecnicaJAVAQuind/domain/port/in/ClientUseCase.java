package com.example.PruebaTecnicaJAVAQuind.domain.port.in;

import com.example.PruebaTecnicaJAVAQuind.domain.model.Client;

import java.util.List;
import java.util.Optional;

public interface ClientUseCase {/*
    Client createClient(Client client);
    List<Client> getAllClients();
    Optional<Client> updateClient(Client updateClient);
    boolean deleteClient(Long id);*/
    Optional<Client> getClient(String id);
}
