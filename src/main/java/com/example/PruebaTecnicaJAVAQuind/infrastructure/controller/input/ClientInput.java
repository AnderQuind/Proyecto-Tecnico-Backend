package com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.input;

import com.example.PruebaTecnicaJAVAQuind.domain.model.Property;
import lombok.*;

import java.util.Date;
import java.util.List;

@Data
public class ClientInput{
    private String idClient;
    private List<Property> properties;
}
