package com.example.Prueba.Tecnica.JAVA.repository;

import com.example.PruebaTecnicaJAVAQuind.domain.model.Client;
import com.example.PruebaTecnicaJAVAQuind.domain.model.Property;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.adapter.repository.PropertyRepositoryAdapter;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.entity.ClientEntity;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.entity.PropertyEntity;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.mapper.PropertyMapper;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.mapper.ClientMapper;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.repository.ClientRepository;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.repository.PropertyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class PropertyRepositoryAdapterTest {

    @Mock
    private PropertyRepository propertyRepository;

    @Mock
    private ClientRepository clientRepository;

    @Mock
    private PropertyMapper propertyMapper;

    @Mock
    private ClientMapper clientMapper;

    @InjectMocks
    private PropertyRepositoryAdapter propertyRepositoryAdapter;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSave() {
        Property property = new Property("propertyName");
        PropertyEntity propertyEntity = new PropertyEntity("propertyName");
        PropertyEntity savedPropertyEntity = new PropertyEntity("propertyName");

        when(propertyMapper.fromDomainModel(eq(property))).thenReturn(propertyEntity);
        when(propertyRepository.save(eq(propertyEntity))).thenReturn(savedPropertyEntity);
        when(propertyMapper.toDomainModel(eq(savedPropertyEntity))).thenReturn(property);

        Property result = propertyRepositoryAdapter.save(property);

        assertNotNull(result);
        assertEquals("propertyName", result.getName());
    }

    @Test
    public void testFindByName_Found() {
        String propertyName = "propertyName";
        PropertyEntity propertyEntity = new PropertyEntity(propertyName);
        Property property = new Property(propertyName);

        when(propertyRepository.findById(eq(propertyName))).thenReturn(Optional.of(propertyEntity));
        when(propertyMapper.toDomainModel(eq(propertyEntity))).thenReturn(property);

        Optional<Property> result = propertyRepositoryAdapter.findByName(propertyName);

        assertTrue(result.isPresent());
        assertEquals(propertyName, result.get().getName());
    }

    @Test
    public void testFindByName_NotFound() {
        String propertyName = "propertyName";

        when(propertyRepository.findById(eq(propertyName))).thenReturn(Optional.empty());

        Optional<Property> result = propertyRepositoryAdapter.findByName(propertyName);

        assertFalse(result.isPresent());
    }

    @Test
    public void testFindAll() {
        PropertyEntity propertyEntity1 = new PropertyEntity("propertyName1");
        PropertyEntity propertyEntity2 = new PropertyEntity("propertyName2");
        Property property1 = new Property("propertyName1");
        Property property2 = new Property("propertyName2");

        when(propertyRepository.findAll()).thenReturn(List.of(propertyEntity1, propertyEntity2));
        when(propertyMapper.toDomainModel(eq(propertyEntity1))).thenReturn(property1);
        when(propertyMapper.toDomainModel(eq(propertyEntity2))).thenReturn(property2);

        List<Property> result = propertyRepositoryAdapter.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("propertyName1", result.get(0).getName());
        assertEquals("propertyName2", result.get(1).getName());
    }

    @Test
    public void testFindAllByIdClient_Found() {
        String clientId = "clientId";
        Client client = new Client(clientId);
        client.setPropertiesName(List.of("propertyName1", "propertyName2"));
        PropertyEntity propertyEntity1 = new PropertyEntity("propertyName1");
        PropertyEntity propertyEntity2 = new PropertyEntity("propertyName2");
        Property property1 = new Property("propertyName1");
        Property property2 = new Property("propertyName2");

        ClientEntity clientEntity = new ClientEntity();
        clientEntity.setIdClient(client.getIdClient());
        clientEntity.setPropertiesName(client.getPropertiesName().toString());

        when(clientRepository.findById(eq(clientId))).thenReturn(Optional.of(clientEntity));
        when(propertyRepository.findAll()).thenReturn(List.of(propertyEntity1, propertyEntity2));
        when(propertyMapper.toDomainModel(eq(propertyEntity1))).thenReturn(property1);
        when(propertyMapper.toDomainModel(eq(propertyEntity2))).thenReturn(property2);

        List<Property> result = propertyRepositoryAdapter.findAllByIdClient(clientId);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("propertyName1", result.get(0).getName());
        assertEquals("propertyName2", result.get(1).getName());
    }

    @Test
    public void testFindAllByIdClient_NotFound() {
        String clientId = "clientId";

        when(clientRepository.findById(eq(clientId))).thenReturn(Optional.empty());

        List<Property> result = propertyRepositoryAdapter.findAllByIdClient(clientId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testUpdate_Found() {
        Property property = new Property("propertyName");
        PropertyEntity propertyEntity = new PropertyEntity("propertyName");

        when(propertyRepository.existsById(eq("propertyName"))).thenReturn(true);
        when(propertyMapper.fromDomainModel(eq(property))).thenReturn(propertyEntity);
        when(propertyRepository.save(eq(propertyEntity))).thenReturn(propertyEntity);
        when(propertyMapper.toDomainModel(eq(propertyEntity))).thenReturn(property);

        Optional<Property> result = propertyRepositoryAdapter.update(property);

        assertTrue(result.isPresent());
        assertEquals("propertyName", result.get().getName());
    }

    @Test
    public void testUpdate_NotFound() {
        Property property = new Property("propertyName");

        when(propertyRepository.existsById(eq("propertyName"))).thenReturn(false);

        Optional<Property> result = propertyRepositoryAdapter.update(property);

        assertFalse(result.isPresent());
    }

    @Test
    public void testDeleteById_Found() {
        String propertyName = "propertyName";

        when(propertyRepository.existsById(eq(propertyName))).thenReturn(true);

        boolean result = propertyRepositoryAdapter.deleteById(propertyName);

        assertTrue(result);
        verify(propertyRepository).deleteById(eq(propertyName));
    }

    @Test
    public void testDeleteById_NotFound() {
        String propertyName = "propertyName";

        when(propertyRepository.existsById(eq(propertyName))).thenReturn(false);

        boolean result = propertyRepositoryAdapter.deleteById(propertyName);

        assertFalse(result);
        verify(propertyRepository, never()).deleteById(any());
    }

    @Test
    public void testFindByAvailableTrueAndPriceBetweenRange_Found() {
        float minPrice = 1000;
        float maxPrice = 5000;
        PropertyEntity propertyEntity1 = new PropertyEntity("propertyName1");
        PropertyEntity propertyEntity2 = new PropertyEntity("propertyName2");
        Property property1 = new Property("propertyName1");
        Property property2 = new Property("propertyName2");

        when(propertyRepository.findByAvailableTrueAndPriceBetweenRange(eq(minPrice), eq(maxPrice)))
                .thenReturn(List.of(propertyEntity1, propertyEntity2));
        when(propertyMapper.toDomainModel(eq(propertyEntity1))).thenReturn(property1);
        when(propertyMapper.toDomainModel(eq(propertyEntity2))).thenReturn(property2);

        Optional<List<Property>> result = propertyRepositoryAdapter.findByAvailableTrueAndPriceBetweenRange(minPrice, maxPrice);

        assertTrue(result.isPresent());
        assertEquals(2, result.get().size());
        assertEquals("propertyName1", result.get().get(0).getName());
        assertEquals("propertyName2", result.get().get(1).getName());
    }

    @Test
    public void testFindByAvailableTrueAndPriceBetweenRange_NotFound() {
        float minPrice = 1000;
        float maxPrice = 5000;

        when(propertyRepository.findByAvailableTrueAndPriceBetweenRange(eq(minPrice), eq(maxPrice)))
                .thenReturn(List.of());

        Optional<List<Property>> result = propertyRepositoryAdapter.findByAvailableTrueAndPriceBetweenRange(minPrice, maxPrice);

        assertTrue(result.isPresent());
        assertTrue(result.get().isEmpty());
    }
}

