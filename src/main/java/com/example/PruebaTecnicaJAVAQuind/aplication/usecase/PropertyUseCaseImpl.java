package com.example.PruebaTecnicaJAVAQuind.aplication.usecase;

import com.example.PruebaTecnicaJAVAQuind.domain.model.Property;
import com.example.PruebaTecnicaJAVAQuind.domain.port.in.PropertyUseCase;
import com.example.PruebaTecnicaJAVAQuind.domain.port.out.PropertyRepositoryPort;
import lombok.AllArgsConstructor;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class PropertyUseCaseImpl implements PropertyUseCase {

    private final PropertyRepositoryPort propertyRepositoryPort;

    @Override
    public Property createProperty(Property property) {
        return propertyRepositoryPort.save(property);
    }

    @Override
    public Optional<Property> getProperty(String name) {
        return propertyRepositoryPort.findByName(name);
    }

    @Override
    public List<Property> getAllProperties() {
        return propertyRepositoryPort.findAll();
    }

    @Override
    public Optional<List<Property>> getAvailablePropertiesBetweenPriceRange(float minPrice, float maxPrice) {
        return propertyRepositoryPort.findByAvailableTrueAndPriceBetweenRange(minPrice, maxPrice);
    }

    @Override
    public Optional<Property> updateProperty(Property updateProperty) {
        return propertyRepositoryPort.update(updateProperty);
    }
}
