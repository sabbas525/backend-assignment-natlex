package com.example.geologicaltest.repository;

import com.example.geologicaltest.model.Section;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * Repository interface for Section entities.
 * Provides basic CRUD operations and custom query to find sections by geological class code.
 *
 * author Ibtehaj
 */
public interface SectionRepository extends JpaRepository<Section, Long> {
    List<Section> findByGeologicalClassesCode(String code);
}
