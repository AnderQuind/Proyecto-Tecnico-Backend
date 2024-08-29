package com.example.PruebaTecnicaJAVAQuind.infrastructure.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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

    public ClientEntity(String idClient) {
        this.idClient = idClient;
    }

    public void setPropertiesName(String propertiesName) {
        this.propertiesName = propertiesName;
        convertJsonToList();
    }

    public void setPropertiesAsArray(List<String> propertiesAsArray) {
        this.propertiesAsArray = propertiesAsArray;
        convertListToJson();
    }

    public static List<String> stringToList(String listStr) {
        return Arrays.stream(listStr.substring(1, listStr.length() - 1).split(","))
                .map(String::trim)
                .toList();
    }

    private void convertJsonToList() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            this.propertiesAsArray = stringToList(this.propertiesName);//mapper.readValue(this.propertiesName, new TypeReference<List<String>>() {});
        } catch (Exception e) {
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
