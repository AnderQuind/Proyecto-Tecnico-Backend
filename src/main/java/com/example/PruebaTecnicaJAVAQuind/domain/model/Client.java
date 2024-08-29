package com.example.PruebaTecnicaJAVAQuind.domain.model;

import lombok.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Client {
    private String idClient;
    private List<String> propertiesName;

    public Client(String idClient) {
        this.idClient = idClient;
    }
}
