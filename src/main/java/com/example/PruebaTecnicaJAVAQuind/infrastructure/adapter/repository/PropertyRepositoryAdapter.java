package com.example.PruebaTecnicaJAVAQuind.infrastructure.adapter.repository;

import com.example.PruebaTecnicaJAVAQuind.domain.model.Property;
import com.example.PruebaTecnicaJAVAQuind.domain.port.out.PropertyRepositoryPort;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.entity.PropertyEntity;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.mapper.PropertyMapper;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.repository.ClientRepository;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.repository.PropertyRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@AllArgsConstructor
public class PropertyRepositoryAdapter implements PropertyRepositoryPort {

    private final PropertyRepository propertyRepository;
    private final ClientRepository clientRepository;
    private final PropertyMapper propertyMapper;

    @Override
    public Property save(Property property) {
        PropertyEntity propertyEntity = propertyMapper.fromDomainModel(property);
        PropertyEntity savedPropertyEntity = propertyRepository.save(propertyEntity);
        return propertyMapper.toDomainModel(savedPropertyEntity);
    }

    @Override
    public Optional<Property> findByName(String name) {
        return propertyRepository.findById(name).map(propertyMapper::toDomainModel);
    }

    @Override
    public List<Property> findAll() {
        return propertyRepository.findAll().stream()
                .map(propertyMapper::toDomainModel)
                .collect(Collectors.toList());
    }

    @Override
    public List<Property> findAllByIdClient(String idClient) {
        return clientRepository.findById(idClient)
                .map(client -> propertyRepository.findAll().stream()
                        .filter(property -> client.getPropertiesName().contains(property.getName()))
                        .map(propertyMapper::toDomainModel)
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    @Override
    public Optional<Property> update(Property property) {
        if(propertyRepository.existsById(property.getName())) {
            PropertyEntity propertyEntity = propertyMapper.fromDomainModel(property);
            PropertyEntity updateTaskEntity = propertyRepository.save(propertyEntity);
            return Optional.of(propertyMapper.toDomainModel(updateTaskEntity));
        }

        return Optional.empty();
    }

    @Override
    public boolean deleteById(String name) {
        if(propertyRepository.existsById(name)) {
            propertyRepository.deleteById(name);
            return true;
        }
        return false;
    }

    @Override
    public Optional<List<Property>> findByAvailableTrueAndPriceBetweenRange(float minPrice, float maxPrice) {
        return Optional.of(propertyRepository.findByAvailableTrueAndPriceBetweenRange(minPrice, maxPrice).stream()
                .map(propertyMapper::toDomainModel).toList());
    }
}
