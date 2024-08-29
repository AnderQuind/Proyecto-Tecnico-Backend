package com.example.PruebaTecnicaJAVAQuind.aplication.service;

import com.example.PruebaTecnicaJAVAQuind.domain.model.Property;
import com.example.PruebaTecnicaJAVAQuind.domain.port.in.ClientUseCase;
import com.example.PruebaTecnicaJAVAQuind.domain.port.in.PropertyUseCase;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.error.CustomException;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.input.LeasePropertyInput;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.input.PropertyInput;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.*;

@AllArgsConstructor
public class PropertyService implements PropertyUseCase {

    private final PropertyUseCase propertyUseCase;
    private final ClientUseCase clientUseCase;
    private final ClientService clientService;

    public Property toDomainModel(PropertyInput propertyInput) {
        Property property = propertyUseCase.getProperty(propertyInput.getName()).orElse(null);
        LocalDateTime creationDate = LocalDateTime.now();
        Boolean available = true;
        Boolean deleted = false;

        if(property != null){
            creationDate = property.getCreationDate();
            available = property.getAvailable();
            deleted = property.getDeleted();
        }

        return new Property(propertyInput.getName(), propertyInput.getUbication(), available, propertyInput.getAssociatedImageURL(),
                propertyInput.getPrice(), creationDate, deleted);
    }

    @Override
    public Property createProperty(Property property) {
        return propertyUseCase.createProperty(property);
    }

    @Override
    public Optional<Property> getProperty(String name) {
        return propertyUseCase.getProperty(name);
    }

    @Override
    public List<Property> getAllProperties() {
        return propertyUseCase.getAllProperties();
    }

    @Override
    public Optional<Property> updateProperty(Property updateProperty) {
        return Optional.ofNullable(this.createProperty(updateProperty/*, true*/));
    }

    public Optional<Property> deleteProperty(String name) {
        Property property = propertyUseCase.getProperty(name).orElse(null);

        if(property != null){
            property.setDeleted(true);
        }

        return Optional.of(property);
    }

    public Optional<List<Property>> getAvailableProperties() {
        return Optional.of(propertyUseCase.getAllProperties().stream().filter(Property::getAvailable)
                .toList());
    }

    public void leaseProperty(LeasePropertyInput leasePropertyInput) {
        Property leasedProperty = this.getProperty(leasePropertyInput.getPropertyName()).orElse(null);

        if(leasedProperty != null){
            clientService.leasePropertyToClient(leasePropertyInput);
            leasedProperty.setAvailable(false);
            propertyUseCase.updateProperty(leasedProperty);
        } else{
            throw new CustomException(HttpStatus.NOT_FOUND, "No hay ninguna propiedad con el nombre indicado");
        }
    }

    public Optional<List<Property>> getAvailablePropertiesBetweenPriceRange(float minPrice, float maxPrice) {
        return propertyUseCase.getAvailablePropertiesBetweenPriceRange(minPrice, maxPrice);
    }
}
