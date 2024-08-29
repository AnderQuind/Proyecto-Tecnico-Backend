package com.example.PruebaTecnicaJAVAQuind.infrastructure.mapper;

import com.example.PruebaTecnicaJAVAQuind.domain.model.Client;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.entity.ClientEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class ClientMapper {

    public ClientEntity fromDomainModel(Client client){
        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setIdClient(client.getIdClient());
        clientEntity.setPropertiesName(client.getPropertiesName().toString());
        return clientEntity;
    }

    public Client toDomainModel(ClientEntity clientEntity){
        return new Client(clientEntity.getIdClient(), clientEntity.getPropertiesAsArray());
    }
}
