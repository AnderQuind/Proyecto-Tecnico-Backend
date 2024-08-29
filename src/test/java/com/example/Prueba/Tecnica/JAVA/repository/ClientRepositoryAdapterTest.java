package com.example.Prueba.Tecnica.JAVA.repository;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import com.example.PruebaTecnicaJAVAQuind.domain.model.Client;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.adapter.repository.ClientRepositoryAdapter;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.entity.ClientEntity;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.mapper.ClientMapper;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.repository.ClientRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

class ClientRepositoryAdapterTest {

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private ClientRepositoryAdapter clientRepositoryAdapter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSave() {
        Client client = new Client("clientId");
        ClientEntity clientEntity = new ClientEntity("clientId");
        ClientEntity savedClientEntity = new ClientEntity("clientId");

        when(clientMapper.fromDomainModel(eq(client))).thenReturn(clientEntity);
        when(clientRepository.save(eq(clientEntity))).thenReturn(savedClientEntity);
        when(clientMapper.toDomainModel(eq(savedClientEntity))).thenReturn(client);

        Client result = clientRepositoryAdapter.save(client);

        assertNotNull(result);
        assertEquals("clientId", result.getIdClient());
    }

    @Test
    void testFindById_Found() {
        String clientId = "clientId";
        ClientEntity clientEntity = new ClientEntity(clientId);
        Client client = new Client(clientId);

        when(clientRepository.findById(eq(clientId))).thenReturn(Optional.of(clientEntity));
        when(clientMapper.toDomainModel(eq(clientEntity))).thenReturn(client);

        Optional<Client> result = clientRepositoryAdapter.findById(clientId);

        assertTrue(result.isPresent());
        assertEquals(clientId, result.get().getIdClient());
    }

    @Test
    void testFindById_NotFound() {
        String clientId = "clientId";

        when(clientRepository.findById(eq(clientId))).thenReturn(Optional.empty());

        Optional<Client> result = clientRepositoryAdapter.findById(clientId);

        assertFalse(result.isPresent());
    }

    @Test
    void testFindAll() {
        ClientEntity clientEntity1 = new ClientEntity("clientId1");
        ClientEntity clientEntity2 = new ClientEntity("clientId2");
        Client client1 = new Client("clientId1");
        Client client2 = new Client("clientId2");

        when(clientRepository.findAll()).thenReturn(List.of(clientEntity1, clientEntity2));
        when(clientMapper.toDomainModel(eq(clientEntity1))).thenReturn(client1);
        when(clientMapper.toDomainModel(eq(clientEntity2))).thenReturn(client2);

        List<Client> result = clientRepositoryAdapter.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("clientId1", result.get(0).getIdClient());
        assertEquals("clientId2", result.get(1).getIdClient());
    }

    @Test
    void testUpdate_Found() {
        Client client = new Client("clientId");
        ClientEntity clientEntity = new ClientEntity("clientId");

        when(clientRepository.existsById(eq("clientId"))).thenReturn(true);
        when(clientMapper.fromDomainModel(eq(client))).thenReturn(clientEntity);
        when(clientRepository.save(eq(clientEntity))).thenReturn(clientEntity);
        when(clientMapper.toDomainModel(eq(clientEntity))).thenReturn(client);

        Optional<Client> result = clientRepositoryAdapter.update(client);

        assertTrue(result.isPresent());
        assertEquals("clientId", result.get().getIdClient());
    }

    @Test
    void testUpdate_NotFound() {
        Client client = new Client("clientId");

        when(clientRepository.existsById(eq("clientId"))).thenReturn(false);

        Optional<Client> result = clientRepositoryAdapter.update(client);

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteById_Found() {
        String clientId = "clientId";

        when(clientRepository.existsById(eq(clientId))).thenReturn(true);

        boolean result = clientRepositoryAdapter.deleteById(clientId);

        assertTrue(result);
        verify(clientRepository, times(1)).deleteById(eq(clientId));
    }

    @Test
    void testDeleteById_NotFound() {
        String clientId = "clientId";

        when(clientRepository.existsById(eq(clientId))).thenReturn(false);

        boolean result = clientRepositoryAdapter.deleteById(clientId);

        assertFalse(result);
        verify(clientRepository, never()).deleteById(any());
    }
}

