package com.example.geologicaltest.service;

import com.example.geologicaltest.model.Section;
import com.example.geologicaltest.repository.SectionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service for handling operations related to Sections.
 * Provides methods to save sections and retrieve sections by code.
 *
 * author Ibtehaj
 */
@Service
public class SectionService {
    @Autowired
    private SectionRepository sectionRepository;

    /**
     * Saves a section to the repository.
     *
     * @param section the section to be saved
     * @return the saved Section
     */
    public Section saveSection(Section section) {
        return sectionRepository.save(section);
    }

    /**
     * Retrieves all sections from the repository.
     *
     * @return a list of all Sections
     */
    public List<Section> getSections() {
        return sectionRepository.findAll();
    }

    /**
     * Retrieves sections by geological class code.
     *
     * @param code the geological class code
     * @return a list of Sections with the specified geological class code
     */
    public List<Section> getSectionsByCode(String code) {
        return sectionRepository.findByGeologicalClassesCode(code);
    }
}
