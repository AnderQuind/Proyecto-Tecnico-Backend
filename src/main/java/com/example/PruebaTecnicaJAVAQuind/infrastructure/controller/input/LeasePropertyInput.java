package com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.input;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class LeasePropertyInput {
    private String idClient;
    private String propertyName;

    public LeasePropertyInput(String propertyName, String idClient) {
        this.idClient = idClient;
        this.propertyName = propertyName;
    }
}
