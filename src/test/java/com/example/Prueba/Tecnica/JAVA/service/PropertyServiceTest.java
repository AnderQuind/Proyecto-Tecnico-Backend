package com.example.Prueba.Tecnica.JAVA.service;

import com.example.PruebaTecnicaJAVAQuind.aplication.service.ClientService;
import com.example.PruebaTecnicaJAVAQuind.aplication.service.PropertyService;
import com.example.PruebaTecnicaJAVAQuind.domain.model.Client;
import com.example.PruebaTecnicaJAVAQuind.domain.model.Property;
import com.example.PruebaTecnicaJAVAQuind.domain.port.in.ClientUseCase;
import com.example.PruebaTecnicaJAVAQuind.domain.port.in.PropertyUseCase;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.error.CustomException;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.input.LeasePropertyInput;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.input.PropertyInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;

import org.springframework.http.HttpStatus;


import java.util.Collections;

class PropertyServiceTest {

    @Mock
    private PropertyUseCase propertyUseCase;

    @Mock
    private ClientUseCase clientUseCase;

    @Mock
    private ClientService clientService;

    @InjectMocks
    private PropertyService propertyService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testToDomainModel() {
        PropertyInput propertyInput = new PropertyInput("name", "Medellín", "imageURL", 1500000);
        Property existingProperty = new Property("name", "Medellín", true, "imageURL", 1500000, LocalDateTime.now(), false);

        when(propertyUseCase.getProperty(eq("name"))).thenReturn(Optional.of(existingProperty));

        Property result = propertyService.toDomainModel(propertyInput);

        assertNotNull(result);
        assertEquals("name", result.getName());
        assertEquals("Medellín", result.getUbication());
    }

    @Test
    void testCreateProperty_Success() {
        Property property = new Property("name", "Medellín", true, "imageURL", 1500000, LocalDateTime.now(), false);

        when(propertyUseCase.getProperty(eq("name"))).thenReturn(Optional.empty());
        when(propertyUseCase.createProperty(any(Property.class))).thenReturn(property);

        Property result = propertyService.createProperty(property);

        assertNotNull(result);
        assertEquals("name", result.getName());
    }

    @Test
    void testCreateProperty_NullField() {
        Property property = new Property(null, "Medellín", true, "imageURL", 1500000, LocalDateTime.now(), false);

        CustomException exception = assertThrows(CustomException.class, () -> {
            propertyService.createProperty(property);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("Ningun campo tiene que estar nulo", exception.getMessage());
    }

    @Test
    void testCreateProperty_NegativePrice() {
        Property property = new Property("name", "Medellín", true, "imageURL", -1000, LocalDateTime.now(), false);

        CustomException exception = assertThrows(CustomException.class, () -> {
            propertyService.createProperty(property);
        });

        assertEquals(HttpStatus.FORBIDDEN, exception.getStatus());
        assertEquals("El precio no puede ser negativo", exception.getMessage());
    }

    @Test
    void testUpdateProperty_NotFound() {
        Property property = new Property("name", "Medellín", true, "imageURL", 1500000, LocalDateTime.now(), false);

        when(propertyUseCase.getProperty(eq("name"))).thenReturn(Optional.empty());

        CustomException exception = assertThrows(CustomException.class, () -> {
            propertyService.updateProperty(property);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Propiedad no encontrada", exception.getMessage());
    }

    @Test
    void testDeleteProperty_Success() {
        Property property = new Property("name", "Medellín", true, "imageURL", 1500000, LocalDateTime.now(), false);

        when(propertyUseCase.getProperty(eq("name"))).thenReturn(Optional.of(property));

        Optional<Property> result = propertyService.deleteProperty("name");

        assertTrue(result.isPresent());
        assertTrue(result.get().getDeleted());
    }

    @Test
    void testLeaseProperty_NotAvailable() {
        Property property = new Property("name", "Medellín", false, "imageURL", 1500000, LocalDateTime.now(), false);

        when(propertyUseCase.getProperty(eq("name"))).thenReturn(Optional.of(property));

        LeasePropertyInput leasePropertyInput = new LeasePropertyInput("name", "clientId");

        CustomException exception = assertThrows(CustomException.class, () -> {
            propertyService.leaseProperty(leasePropertyInput);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Esta propiedad no se encuentra disponible", exception.getMessage());
    }

    @Test
    void testGetAvailableProperties() {
        Property property = new Property("name", "Medellín", true, "imageURL", 1500000, LocalDateTime.now(), false);

        when(propertyUseCase.getAllProperties()).thenReturn(Collections.singletonList(property));

        Optional<List<Property>> result = propertyService.getAvailableProperties();

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
    }

    @Test
    void testGetAvailablePropertiesBetweenPriceRange() {
        Property property = new Property("name", "Medellín", true, "imageURL", 1500000, LocalDateTime.now(), false);

        when(propertyUseCase.getAvailablePropertiesBetweenPriceRange(1000000, 2000000))
                .thenReturn(Optional.of(Collections.singletonList(property)));

        Optional<List<Property>> result = propertyService.getAvailablePropertiesBetweenPriceRange(1000000, 2000000);

        assertTrue(result.isPresent());
        assertEquals(1, result.get().size());
    }
}
