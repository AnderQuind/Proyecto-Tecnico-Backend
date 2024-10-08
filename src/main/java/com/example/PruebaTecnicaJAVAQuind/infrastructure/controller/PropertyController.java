package com.example.PruebaTecnicaJAVAQuind.infrastructure.controller;

import com.example.PruebaTecnicaJAVAQuind.aplication.service.PropertyService;
import com.example.PruebaTecnicaJAVAQuind.domain.model.Property;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.input.LeasePropertyInput;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.input.PropertyInput;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@AllArgsConstructor
@RequestMapping("/api/property")
public class PropertyController {

    private final PropertyService propertyService;

    @PostMapping("/create")
    public ResponseEntity<Object> createProperty(@RequestBody PropertyInput propertyInput) {
        try {
            Property property = propertyService.toDomainModel(propertyInput);
            Property createdProperty = propertyService.createProperty(property);
            return new ResponseEntity<>(Map.of("response", createdProperty, "answer", "propiedad creada con éxito"), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/available")
    public ResponseEntity<Map<String, Object>> getAvailableProperties() {
        return propertyService.getAvailableProperties()
                .map(availableProperties -> new ResponseEntity<>(Map.of("response", availableProperties, "answer", "propiedad creada con éxito"),
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/available/price-range")
    public ResponseEntity<Map<String, Object>> getAvailablePropertiesBetweenPriceRange(
            @RequestParam Float minPrice,
            @RequestParam Float maxPrice) {
        return propertyService.getAvailablePropertiesBetweenPriceRange(minPrice, maxPrice)
                .map(availableProperties -> new ResponseEntity<>(Map.of("response", availableProperties, "answer", "propiedades listadas con éxito"),
                        HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PutMapping
    public ResponseEntity<Object> updateProperty(@RequestBody PropertyInput propertyInput) {
        try {
            Property updateProperty = propertyService.toDomainModel(propertyInput);
            return propertyService.updateProperty(updateProperty)
                    .map(property -> new ResponseEntity<>((Object) property, HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @DeleteMapping("/{name}")
    public ResponseEntity<Object> deletePropertyByName(@PathVariable String name) {
        try {
            return propertyService.deleteProperty(name)
                    .map(property -> new ResponseEntity<>((Object) Map.of("answer", "Propiedad eliminada con éxito"),
                            HttpStatus.OK))
                    .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @PutMapping("/lease")
    public ResponseEntity<Object> leaseProperty(@RequestBody LeasePropertyInput leasePropertyInput) {
        try {
            propertyService.leaseProperty(leasePropertyInput);
            return new ResponseEntity<>("El cliente con id " + leasePropertyInput.getIdClient() +
                    " ha alquilado la propiedad " + leasePropertyInput.getPropertyName() + " con éxito", HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/{name}")
    public ResponseEntity<Map<String, Object>> getPropertyByName(@PathVariable String name) {
        return propertyService.getProperty(name)
                .map(property -> new ResponseEntity<>(Map.of("response", (Object) property, "answer", "Propiedad encontrada con éxito"), HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}


