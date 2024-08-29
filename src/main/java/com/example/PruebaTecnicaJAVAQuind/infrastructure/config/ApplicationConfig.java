package com.example.PruebaTecnicaJAVAQuind.infrastructure.config;

import com.example.PruebaTecnicaJAVAQuind.aplication.service.ClientService;
import com.example.PruebaTecnicaJAVAQuind.aplication.service.PropertyService;
import com.example.PruebaTecnicaJAVAQuind.aplication.usecase.ClientUseCaseImpl;
import com.example.PruebaTecnicaJAVAQuind.aplication.usecase.PropertyUseCaseImpl;
import com.example.PruebaTecnicaJAVAQuind.domain.port.in.ClientUseCase;
import com.example.PruebaTecnicaJAVAQuind.domain.port.out.ClientRepositoryPort;
import com.example.PruebaTecnicaJAVAQuind.domain.port.out.PropertyRepositoryPort;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.adapter.repository.ClientRepositoryAdapter;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.adapter.repository.PropertyRepositoryAdapter;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.mapper.ClientMapper;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.mapper.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

@Configuration("securityApplicationConfig")
public class ApplicationConfig {

    @Bean
    public ClientService clientService(ClientRepositoryPort clientRepositoryPort){
        return new ClientService(
                new ClientUseCaseImpl(clientRepositoryPort)
        );
    }

    @Bean
    public PropertyService propertyService(PropertyRepositoryPort propertyRepositoryPort,
                                           ClientRepositoryPort clientRepositoryPort,
                                           ClientUseCase clientUseCase){
        return new PropertyService(
                new PropertyUseCaseImpl(propertyRepositoryPort),
                new ClientUseCaseImpl(clientRepositoryPort),
                new ClientService(clientUseCase)
        );
    }

    @Bean
    public ClientRepositoryPort clientRepositoryPort(ClientRepositoryAdapter clientRepositoryAdapter){
        return clientRepositoryAdapter;
    }

    @Bean
    public PropertyRepositoryPort propertyRepositoryPort(PropertyRepositoryAdapter propertyRepositoryAdapter){
        return propertyRepositoryAdapter;
    }

    @Bean
    public PropertyMapper propertyMapper(ClientService clientService,
                                         @Lazy PropertyService propertyService,
                                         ClientMapper clientMapper){
        return new PropertyMapper(clientService, propertyService, clientMapper);
    }

}
