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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@AllArgsConstructor
public class PropertyService implements PropertyUseCase {

    private static final Logger logger = LoggerFactory.getLogger(PropertyService.class);

    private final PropertyUseCase propertyUseCase;
    private final ClientUseCase clientUseCase;
    private final ClientService clientService;

    public Property toDomainModel(PropertyInput propertyInput) {
        logger.info("Transformando PropertyInput a modelo de dominio: {}", propertyInput);

        Property property = propertyUseCase.getProperty(propertyInput.getName()).orElse(null);
        LocalDateTime creationDate = LocalDateTime.now();
        Boolean available = true;
        Boolean deleted = false;

        if (property != null) {
            creationDate = property.getCreationDate();
            available = property.getAvailable();
            deleted = property.getDeleted();
        }

        Property domainProperty = new Property(propertyInput.getName(), propertyInput.getUbication(), available, propertyInput.getAssociatedImageURL(),
                propertyInput.getPrice(), creationDate, deleted);
        logger.info("Modelo de dominio creado: {}", domainProperty);
        return domainProperty;
    }

    @Override
    public Property createProperty(Property property) {
        logger.info("Intentando crear una propiedad con nombre: {}", property.getName());

        if (property.getName() == null ||
                property.getDeleted() == null ||
                property.getAvailable() == null ||
                property.getCreationDate() == null ||
                property.getPrice() == 0 ||
                property.getAssociatedImageURL() == null ||
                property.getUbication() == null) {
            logger.error("Error al crear propiedad: algún campo es nulo");
            throw new CustomException(HttpStatus.FORBIDDEN, "Ningun campo tiene que estar nulo");
        }
        if (property.getPrice() <= 0) {
            logger.error("Error al crear propiedad: el precio no puede ser negativo");
            throw new CustomException(HttpStatus.FORBIDDEN, "El precio no puede ser negativo");
        }
        if (propertyUseCase.getProperty(property.getName()).isPresent()) {
            logger.error("Error al crear propiedad: ya existe una propiedad con el nombre indicado");
            throw new CustomException(HttpStatus.FORBIDDEN, "Ya existe una propiedad con el nombre indicado");
        }
        if (!(property.getUbication().toLowerCase().contains("medellín") ||
                property.getUbication().toLowerCase().contains("medellin") ||
                property.getUbication().toLowerCase().contains("bogota") ||
                property.getUbication().toLowerCase().contains("bogotá") ||
                property.getUbication().toLowerCase().contains("cali") ||
                property.getUbication().toLowerCase().contains("cartagena"))) {
            logger.error("Error al crear propiedad: la ubicación debe ser en Medellín, Bogotá, Cali o Cartagena");
            throw new CustomException(HttpStatus.FORBIDDEN, "la propiedad tiene que estar ubicada en Medellín, Bogotá, Cali o Cartagena");
        }

        if ((property.getUbication().toLowerCase().contains("bogota") ||
                property.getUbication().toLowerCase().contains("bogotá") ||
                property.getUbication().toLowerCase().contains("cali")) && property.getPrice() < 2000000) {
            logger.error("Error al crear propiedad: el precio debe ser mayor a 2 millones para propiedades en Bogotá o Cali");
            throw new CustomException(HttpStatus.FORBIDDEN, "Si la propiedad se encuentra en Bogotá o Cali esta debe tener un costo de alquiler de más de 2 millones de pesos");
        }

        Property createdProperty = propertyUseCase.createProperty(property);
        logger.info("Propiedad creada exitosamente con nombre: {}", createdProperty.getName());
        return createdProperty;
    }

    @Override
    public Optional<Property> getProperty(String name) {
        logger.info("Buscando propiedad con nombre: {}", name);
        Optional<Property> property = propertyUseCase.getProperty(name);
        if (property.isPresent()) {
            logger.info("Propiedad encontrada: {}", property.get());
        } else {
            logger.warn("No se encontró propiedad con nombre: {}", name);
        }
        return property;
    }

    @Override
    public List<Property> getAllProperties() {
        logger.info("Obteniendo todas las propiedades");
        List<Property> properties = propertyUseCase.getAllProperties();
        logger.info("Número de propiedades obtenidas: {}", properties.size());
        return properties;
    }

    @Override
    public Optional<Property> updateProperty(Property updateProperty) {
        logger.info("Actualizando propiedad con nombre: {}", updateProperty.getName());

        Property currentProperty = this.getProperty(updateProperty.getName()).orElse(null);

        if (currentProperty == null) {
            logger.error("Propiedad no encontrada para actualizar: {}", updateProperty.getName());
            throw new CustomException(HttpStatus.NOT_FOUND, "Propiedad no encontrada");
        }

        if (!currentProperty.getAvailable()) {
            if (currentProperty.getPrice() != updateProperty.getPrice()) {
                logger.error("Error al actualizar propiedad: precio no puede cambiar si la propiedad está arrendada");
                throw new CustomException(HttpStatus.FORBIDDEN, "Si la propiedad está arrendada no se podrá cambiar su precio");
            }
            if (!currentProperty.getUbication().equals(updateProperty.getUbication())) {
                logger.error("Error al actualizar propiedad: ubicación no puede cambiar si la propiedad está arrendada");
                throw new CustomException(HttpStatus.FORBIDDEN, "Si la propiedad está arrendada no se podrá cambiar su ubicación");
            }
        }

        Property updatedProperty = this.createProperty(updateProperty);
        logger.info("Propiedad actualizada exitosamente con nombre: {}", updatedProperty.getName());
        return Optional.of(updatedProperty);
    }

    public Optional<Property> deleteProperty(String name) {
        logger.info("Intentando eliminar propiedad con nombre: {}", name);

        Property property = propertyUseCase.getProperty(name).orElse(null);

        if (property != null) {
            LocalDateTime creationDate = property.getCreationDate();
            LocalDateTime now = LocalDateTime.now();

            LocalDateTime oneMonthLater = creationDate.plusMonths(1);

            if (now.isBefore(oneMonthLater)) {
                property.setDeleted(true);
                logger.info("Propiedad marcada como eliminada: {}", property.getName());
            } else {
                logger.error("Error al eliminar propiedad: no se puede eliminar antes de 1 mes");
                throw new CustomException(HttpStatus.FORBIDDEN, "Para que una propiedad pueda ser eliminada debe haber sido guardada hace más de 1 mes");
            }
        } else {
            logger.warn("Propiedad no encontrada para eliminar: {}", name);
        }

        return Optional.ofNullable(property);
    }

    public Optional<List<Property>> getAvailableProperties() {
        logger.info("Obteniendo propiedades disponibles");
        List<Property> availableProperties = propertyUseCase.getAllProperties().stream()
                .filter(Property::getAvailable)
                .toList();
        logger.info("Número de propiedades disponibles: {}", availableProperties.size());
        return Optional.of(availableProperties);
    }

    public void leaseProperty(LeasePropertyInput leasePropertyInput) {
        logger.info("Intentando arrendar propiedad con nombre: {}", leasePropertyInput.getPropertyName());

        Property leasedProperty = this.getProperty(leasePropertyInput.getPropertyName()).orElse(null);

        if (leasedProperty != null) {
            if (!leasedProperty.getAvailable() || leasedProperty.getDeleted()) {
                logger.error("Error al arrendar propiedad: propiedad no disponible");
                throw new CustomException(HttpStatus.NOT_FOUND, "Esta propiedad no se encuentra disponible");
            }
            leasedProperty.setAvailable(false);
            propertyUseCase.updateProperty(leasedProperty);
            clientService.leasePropertyToClient(leasePropertyInput);
            logger.info("Propiedad arrendada exitosamente a cliente: {}", leasePropertyInput.getIdClient());
        } else {
            logger.error("Error al arrendar propiedad: no existe una propiedad con el nombre indicado");
            throw new CustomException(HttpStatus.NOT_FOUND, "No hay ninguna propiedad con el nombre indicado");
        }
    }

    public Optional<List<Property>> getAvailablePropertiesBetweenPriceRange(float minPrice, float maxPrice) {
        logger.info("Obteniendo propiedades disponibles entre el rango de precio {} y {}", minPrice, maxPrice);
        Optional<List<Property>> properties = propertyUseCase.getAvailablePropertiesBetweenPriceRange(minPrice, maxPrice);
        logger.info("Número de propiedades encontradas en el rango de precio: {}", properties.map(List::size).orElse(0));
        return properties;
    }
}
