package com.example.Prueba.Tecnica.JAVA.controller;

import com.example.PruebaTecnicaJAVAQuind.aplication.service.PropertyService;
import com.example.PruebaTecnicaJAVAQuind.domain.model.Property;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.PropertyController;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.input.LeasePropertyInput;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.input.PropertyInput;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PropertyControllerTest {

    @Mock
    private PropertyService propertyService;

    @InjectMocks
    private PropertyController propertyController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(propertyController).build();
        objectMapper = new ObjectMapper();
    }

    // Arrange
    String expectedName = "Test_Property";
    String expectedUbication = "Test Ubication";
    String expectedAssociatedImageURL = "https://example.com/image.jpg";
    float expectedPrice = 100.0f;

    PropertyInput propertyInput = new PropertyInput(
            expectedName,
            expectedUbication,
            expectedAssociatedImageURL,
            expectedPrice
    );

    Property property = new Property(
            expectedName,
            expectedUbication,
            true,
            expectedAssociatedImageURL,
            expectedPrice,
            LocalDateTime.now(),
            false
    );

    @Test
    public void testCreateProperty() throws Exception {

        when(propertyService.toDomainModel(any(PropertyInput.class))).thenReturn(property);
        when(propertyService.createProperty(any(Property.class))).thenReturn(property);

        mockMvc.perform(post("/api/property/create")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(propertyInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value("propiedad creada con éxito"));
    }

    @Test
    public void testGetAvailableProperties() throws Exception {
        when(propertyService.getAvailableProperties()).thenReturn(Optional.of(List.of(property)));

        mockMvc.perform(get("/api/property/available"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").isArray());
    }

    @Test
    public void testGetAvailablePropertiesBetweenPriceRange() throws Exception {
        when(propertyService.getAvailablePropertiesBetweenPriceRange(eq(1000f), eq(2000f)))
                .thenReturn(Optional.of(Collections.singletonList(new Property())));

        mockMvc.perform(get("/api/property/available/price-range")
                        .param("minPrice", "1000")
                        .param("maxPrice", "2000"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.response").isArray());
    }

    @Test
    public void testUpdateProperty() throws Exception {
        when(propertyService.toDomainModel(any(PropertyInput.class))).thenReturn(property);
        when(propertyService.updateProperty(any(Property.class))).thenReturn(Optional.of(property));

        mockMvc.perform(put("/api/property")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(propertyInput)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Test_Property"));
    }

    @Test
    public void testDeletePropertyByName() throws Exception {
        when(propertyService.deleteProperty(eq("Test_Property"))).thenReturn(Optional.of(property));

        mockMvc.perform(delete("/api/property/Test_Property"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value("Propiedad eliminada con éxito"));
    }

    @Test
    public void testLeaseProperty() throws Exception {
        LeasePropertyInput leasePropertyInput = new LeasePropertyInput(); // set necessary fields

        mockMvc.perform(put("/api/property/lease")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(leasePropertyInput)))
                .andExpect(status().isOk())
                .andExpect(content().string("El cliente con id " + leasePropertyInput.getIdClient() +
                        " ha alquilado la propiedad " + leasePropertyInput.getPropertyName() + " con éxito"));
    }

    @Test
    public void testGetPropertyByName() throws Exception {
        when(propertyService.getProperty(eq("Test_Property"))).thenReturn(Optional.of(property));

        mockMvc.perform(get("/api/property/Test_Property"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.answer").value("Propiedad encontrada con éxito"));
    }
}

