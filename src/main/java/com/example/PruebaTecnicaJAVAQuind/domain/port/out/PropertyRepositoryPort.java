package com.example.PruebaTecnicaJAVAQuind.domain.port.out;

import com.example.PruebaTecnicaJAVAQuind.domain.model.Property;

import java.util.List;
import java.util.Optional;

public interface PropertyRepositoryPort {
    Property save(Property property);
    Optional<Property> findByName(String name);
    List<Property> findAll();

    List<Property> findAllByIdClient(String idClient);

    Optional<Property> update(Property property);
    boolean deleteById(String name);

    Optional<List<Property>> findByAvailableTrueAndPriceBetweenRange(float minPrice, float maxPrice);
}
