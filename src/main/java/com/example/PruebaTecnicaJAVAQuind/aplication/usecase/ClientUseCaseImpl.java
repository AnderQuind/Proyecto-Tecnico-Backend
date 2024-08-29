package com.example.PruebaTecnicaJAVAQuind.aplication.usecase;

import com.example.PruebaTecnicaJAVAQuind.domain.model.Client;
import com.example.PruebaTecnicaJAVAQuind.domain.port.in.ClientUseCase;
import com.example.PruebaTecnicaJAVAQuind.domain.port.out.ClientRepositoryPort;
import lombok.AllArgsConstructor;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class ClientUseCaseImpl implements ClientUseCase {

    private final ClientRepositoryPort clientRepositoryPort;

    @Override
    public Optional<Client> getClient(String id) {
        return clientRepositoryPort.findById(id);
    }
}
