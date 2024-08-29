package com.example.PruebaTecnicaJAVAQuind.infrastructure.mapper;

import com.example.PruebaTecnicaJAVAQuind.aplication.service.ClientService;
import com.example.PruebaTecnicaJAVAQuind.aplication.service.PropertyService;
import com.example.PruebaTecnicaJAVAQuind.domain.model.Property;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.entity.ClientEntity;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.entity.PropertyEntity;
import lombok.Data;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Data
@Component
public class PropertyMapper {

    private final ClientService clientService;
    private final PropertyService propertyService;
    private final ClientMapper clientMapper;

    @Autowired
    public PropertyMapper(ClientService clientService, PropertyService propertyService, ClientMapper clientMapper) {
        this.clientService = clientService;
        this.propertyService = propertyService;
        this.clientMapper = clientMapper;
    }

    public PropertyEntity fromDomainModel(Property property){
        return new PropertyEntity(property.getName(), property.getUbication(), property.getAvailable(),
                property.getAssociatedImageURL(), property.getPrice(), property.getCreationDate(), property.getDeleted());
    }

    public Property toDomainModel(PropertyEntity propertyEntity){

        return new Property(propertyEntity.getName(), propertyEntity.getUbication(), propertyEntity.getAvailable(),
                propertyEntity.getAssociatedImageURL(), propertyEntity.getPrice(), propertyEntity.getCreationDate(), propertyEntity.getDeleted());
    }
}
