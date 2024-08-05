package com.example.geologicaltest.controller;

import com.example.geologicaltest.model.Section;
import com.example.geologicaltest.service.SectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Controller for handling CRUD operations on Sections.
 * Provides endpoints for creating and retrieving sections.
 * Implements filtering by geological class code.
 *
 * author Ibtehaj
 */
@RestController
@RequestMapping("/sections")
public class SectionController {
    @Autowired
    private SectionService sectionService;

    /**
     * Endpoint to create a new section.
     *
     * @param section the section to be created
     * @return the created Section
     */
    @PostMapping
    public Section createSection(@RequestBody Section section) {
        return sectionService.saveSection(section);
    }

    /**
     * Endpoint to retrieve all sections.
     *
     * @return a list of all Sections
     */
    @GetMapping
    public List<Section> getAllSections() {
        return sectionService.getSections();
    }

    /**
     * Endpoint to retrieve sections by geological class code.
     *
     * @param code the geological class code to filter by
     * @return a list of Sections with the specified geological class code
     */
    @GetMapping("/by-code")
    public List<Section> getSectionsByCode(@RequestParam String code) {
        return sectionService.getSectionsByCode(code);
    }

}
