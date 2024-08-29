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
        if(property.getName() == null ||
                property.getDeleted() == null ||
                property.getAvailable() == null ||
                property.getCreationDate() == null ||
                property.getPrice() == 0 ||
                property.getAssociatedImageURL() == null ||
                property.getUbication() == null){
            throw new CustomException(HttpStatus.FORBIDDEN, "Ningun campo tiene que estar nulo");
        }
        if(property.getPrice() <= 0){
            throw new CustomException(HttpStatus.FORBIDDEN, "El precio no puede ser negativo");
        }
        if(propertyUseCase.getProperty(property.getName()).isPresent()){
            throw new CustomException(HttpStatus.FORBIDDEN, "Ya existe una propiedad con el nombre indicado");
        }
        if(!(property.getUbication().toLowerCase().contains("medellín") ||
                property.getUbication().toLowerCase().contains("medellin") ||
                property.getUbication().toLowerCase().contains("bogota") ||
                property.getUbication().toLowerCase().contains("bogotá") ||
                property.getUbication().toLowerCase().contains("cali") ||
                property.getUbication().toLowerCase().contains("cartagena") )) {
            throw new CustomException(HttpStatus.FORBIDDEN, "la propiedad tiene que estar ubicada en Medellín, Bogotá, Cali o Cartagena");
        }
        if((property.getUbication().toLowerCase().contains("bogota") ||
                property.getUbication().toLowerCase().contains("bogotá") ||
                property.getUbication().toLowerCase().contains("cali")) && property.getPrice()<2000000){
            throw new CustomException(HttpStatus.FORBIDDEN, "Si la propiedad se encuentra en Bogotá o Cali esta debe tener un costo de alquiler " +
                    "de más de 2 millones de pesos");
        }

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
        Property currentProperty = this.getProperty(updateProperty.getName()).orElse(null);

        if(currentProperty == null){
            throw new CustomException(HttpStatus.NOT_FOUND, "Propiedad no encontrada");
        }

        if(!currentProperty.getAvailable()){
            if(currentProperty.getPrice() != updateProperty.getPrice()){
                throw new CustomException(HttpStatus.FORBIDDEN, "Si la propiedad está arrendada no se podrá cambiar su precio");
            }
            if(currentProperty.getUbication() != updateProperty.getUbication()){
                throw new CustomException(HttpStatus.FORBIDDEN, "Si la propiedad está arrendada no se podrá cambiar su ubicación");
            }
        }

        return Optional.ofNullable(this.createProperty(updateProperty));
    }

    public Optional<Property> deleteProperty(String name) {
        Property property = propertyUseCase.getProperty(name).orElse(null);

        if(property != null){
            LocalDateTime creationDate = property.getCreationDate();
            LocalDateTime now = LocalDateTime.now();

            LocalDateTime oneMonthLater = creationDate.plusMonths(1);

            if(now.isBefore(oneMonthLater)){
                property.setDeleted(true);
            } else{
                throw new CustomException(HttpStatus.FORBIDDEN, "Para que una propiedad pueda ser eliminada estar debe haber" +
                        " sido guardada hace más de 1 mes");
            }
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
            if(!leasedProperty.getAvailable() || leasedProperty.getDeleted()){
                throw new CustomException(HttpStatus.NOT_FOUND, "Esta propiedad no se encuentra disponible");
            }
        } else{
            throw new CustomException(HttpStatus.NOT_FOUND, "No hay ninguna propiedad con el nombre indicado");
        }
    }

    public Optional<List<Property>> getAvailablePropertiesBetweenPriceRange(float minPrice, float maxPrice) {
        return propertyUseCase.getAvailablePropertiesBetweenPriceRange(minPrice, maxPrice);
    }
}
