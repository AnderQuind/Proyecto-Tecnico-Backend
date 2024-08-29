package com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.input;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyInput {

    private String name;
    private String ubication;
    private String associatedImageURL;
    private float price;
}
