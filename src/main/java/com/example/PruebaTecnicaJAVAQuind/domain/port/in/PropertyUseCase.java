package com.example.PruebaTecnicaJAVAQuind.domain.port.in;

import com.example.PruebaTecnicaJAVAQuind.domain.model.Property;

import java.util.List;
import java.util.Optional;

public interface PropertyUseCase {
    Property createProperty(Property property);
    Optional<Property> getProperty(String name);
    List<Property> getAllProperties();
    Optional<List<Property>> getAvailablePropertiesBetweenPriceRange(float minPrice, float maxPrice);
    Optional<Property> updateProperty(Property updateProperty);
}