package com.example.PruebaTecnicaJAVAQuind.infrastructure.adapter.repository;

import com.example.PruebaTecnicaJAVAQuind.domain.model.Client;
import com.example.PruebaTecnicaJAVAQuind.domain.port.out.ClientRepositoryPort;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.entity.ClientEntity;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.mapper.ClientMapper;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.repository.ClientRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class ClientRepositoryAdapter implements ClientRepositoryPort {

    private final ClientRepository clientRepository;
    private final ClientMapper clientMapper;

    @Override
    public Client save(Client client) {
        ClientEntity clientEntity = clientMapper.fromDomainModel(client);
        ClientEntity savedClientEntity = clientRepository.save(clientEntity);
        return clientMapper.toDomainModel(savedClientEntity);
    }

    @Override
    public Optional<Client> findById(String id) {
        return clientRepository.findById(id).map(clientMapper::toDomainModel);
    }

    @Override
    public List<Client> findAll() {
        return clientRepository.findAll().stream()
                .map(clientMapper::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<Client> update(Client client) {
        if(clientRepository.existsById(client.getIdClient())) {
            ClientEntity clientEntity = clientMapper.fromDomainModel(client);
            ClientEntity updateTaskEntity = clientRepository.save(clientEntity);
            return Optional.of(clientMapper.toDomainModel(updateTaskEntity));
        }

        return Optional.empty();
    }

    @Override
    public boolean deleteById(String id) {
        if(clientRepository.existsById(id)) {
            clientRepository.deleteById(id);
            return true;
        }

        return false;
    }
}
