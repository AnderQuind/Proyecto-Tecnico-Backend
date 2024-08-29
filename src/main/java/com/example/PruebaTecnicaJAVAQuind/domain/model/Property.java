package com.example.PruebaTecnicaJAVAQuind.domain.model;

import lombok.*;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Property {
    private String name;
    private String ubication;
    private Boolean available;
    private String associatedImageURL;
    private float price;
    private LocalDateTime creationDate;
    private Boolean deleted;

    public Property(String name) {
        this.name = name;
    }
}
