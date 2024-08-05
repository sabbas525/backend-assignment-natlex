package com.example.geologicaltest.repository;

import com.example.geologicaltest.model.GeologicalClass;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Repository interface for GeologicalClass entities.
 * Provides basic CRUD operations.
 *
 * author Ibtehaj
 */
public interface GeologicalClassRepository extends JpaRepository<GeologicalClass, Long> {
}
