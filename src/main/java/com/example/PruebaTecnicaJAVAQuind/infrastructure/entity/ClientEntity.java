package com.example.PruebaTecnicaJAVAQuind.infrastructure.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

import java.io.IOException;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "client")
public class ClientEntity {

    @Id
    @Setter
    @Column(name = "id_client")
    private String idClient;

    @Column(name = "properties_name", columnDefinition = "JSON")
    private String propertiesName;  // Se almacena como JSON en MySQL

    @Transient
    private List<String> propertiesAsArray;  // Representación en Java

    public void setPropertiesName(String propertiesName) {
        this.propertiesName = propertiesName;
        convertJsonToList();
    }

    public void setPropertiesAsArray(List<String> propertiesAsArray) {
        this.propertiesAsArray = propertiesAsArray;
        convertListToJson();
    }

    private void convertJsonToList() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.propertiesAsArray = mapper.readValue(this.propertiesName, new TypeReference<List<String>>() {});
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void convertListToJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.propertiesName = mapper.writeValueAsString(this.propertiesName);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
