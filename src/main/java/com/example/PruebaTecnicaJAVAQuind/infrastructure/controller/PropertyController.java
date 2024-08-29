package com.example.PruebaTecnicaJAVAQuind.infrastructure.controller;

import com.example.PruebaTecnicaJAVAQuind.aplication.service.PropertyService;
import com.example.PruebaTecnicaJAVAQuind.domain.model.Property;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.input.LeasePropertyInput;
import com.example.PruebaTecnicaJAVAQuind.infrastructure.controller.input.PropertyInput;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
            return new ResponseEntity<>(createdProperty, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.FORBIDDEN);
        }
    }

    @GetMapping("/available")
    public ResponseEntity<List<Property>> getAvailableProperties() {
        return propertyService.getAvailableProperties()
                .map(availableProperties -> new ResponseEntity<>(availableProperties, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/available/price-range")
    public ResponseEntity<List<Property>> getAvailablePropertiesBetweenPriceRange(
            @RequestParam Float minPrice,
            @RequestParam Float maxPrice) {
        return propertyService.getAvailablePropertiesBetweenPriceRange(minPrice, maxPrice)
                .map(availableProperties -> new ResponseEntity<>(availableProperties, HttpStatus.OK))
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
                    .map(property -> new ResponseEntity<>((Object) property, HttpStatus.OK))
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
    public ResponseEntity<Property> getPropertyByName(@PathVariable String name) {
        return propertyService.getProperty(name)
                .map(property -> new ResponseEntity<>(property, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}


