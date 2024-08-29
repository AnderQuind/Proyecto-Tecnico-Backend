package com.example.PruebaTecnicaJAVAQuind.infrastructure.repository;

import com.example.PruebaTecnicaJAVAQuind.infrastructure.entity.PropertyEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PropertyRepository extends JpaRepository<PropertyEntity, String> {
    // Método para encontrar propiedades disponibles
    List<PropertyEntity> findByAvailableTrue();

    // Método para encontrar propiedades disponibles en un rango de precio usando una consulta JPQL
    @Query("SELECT p FROM PropertyEntity p WHERE p.available = true AND p.price BETWEEN :minPrice AND :maxPrice")
    List<PropertyEntity> findByAvailableTrueAndPriceBetweenRange(@Param("minPrice") float minPrice, @Param("maxPrice") float maxPrice);
}