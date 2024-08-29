package com.example.Prueba.Tecnica.JAVA.service;

import com.example.PruebaTecnicaJAVAQuind.aplication.service.ClientService;
import com.example.PruebaTecnicaJAVAQuind.domain.model.Client;
import com.example.PruebaTecnicaJAVAQuind.domain.port.in.ClientUseCase;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.input.ClientInput;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.input.LeasePropertyInput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

class ClientServiceTest {

    @Mock
    private ClientUseCase clientUseCase;

    @InjectMocks
    private ClientService clientService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testLeasePropertyToClient_NewClient() {
        LeasePropertyInput leasePropertyInput = new LeasePropertyInput("propertyName", "newClientId");

        when(clientUseCase.getClient(eq("newClientId"))).thenReturn(Optional.empty());

        clientService.leasePropertyToClient(leasePropertyInput);

        verify(clientUseCase).getClient(eq("newClientId"));
        Client newClient = new Client("newClientId");
        newClient.setPropertiesName(List.of("propertyName"));
    }

    @Test
    void testLeasePropertyToClient_ExistingClient() {
        LeasePropertyInput leasePropertyInput = new LeasePropertyInput("propertyName", "existingClientId");
        Client existingClient = new Client("existingClientId");
        existingClient.setPropertiesName(new ArrayList<>());

        when(clientUseCase.getClient(eq("existingClientId"))).thenReturn(Optional.of(existingClient));

        clientService.leasePropertyToClient(leasePropertyInput);

        assertEquals(1, existingClient.getPropertiesName().size());
        assertTrue(existingClient.getPropertiesName().contains("propertyName"));
    }

    @Test
    void testLeasePropertyToClient_ExistingClientWithProperties() {
        LeasePropertyInput leasePropertyInput = new LeasePropertyInput("newProperty", "existingClientId");
        List<String> properties = new ArrayList<>();
        properties.add("existingProperty");
        Client existingClient = new Client("existingClientId");
        existingClient.setPropertiesName(properties);

        when(clientUseCase.getClient(eq("existingClientId"))).thenReturn(Optional.of(existingClient));

        clientService.leasePropertyToClient(leasePropertyInput);

        assertEquals(2, existingClient.getPropertiesName().size());
        assertTrue(existingClient.getPropertiesName().contains("newProperty"));
    }

    @Test
    void testGetClient() {
        Client client = new Client("testClientId");

        when(clientUseCase.getClient(eq("testClientId"))).thenReturn(Optional.of(client));

        Optional<Client> result = clientService.getClient("testClientId");

        assertTrue(result.isPresent());
        assertEquals("testClientId", result.get().getIdClient());
    }
}
