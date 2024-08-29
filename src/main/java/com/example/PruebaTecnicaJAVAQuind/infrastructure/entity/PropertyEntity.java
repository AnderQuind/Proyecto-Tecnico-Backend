package com.example.PruebaTecnicaJAVAQuind.infrastructure.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "property")
public class PropertyEntity {

    @Id
    private String name;
    private String ubication;
    private Boolean available;
    private String associatedImageURL;
    private float price;
    private LocalDateTime creationDate;
    private Boolean deleted;

    public PropertyEntity(String name) {
        this.name = name;
    }
}
