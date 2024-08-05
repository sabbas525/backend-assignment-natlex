package com.example.geologicaltest.service;

import com.example.geologicaltest.model.GeologicalClass;
import com.example.geologicaltest.model.JobStatus;
import com.example.geologicaltest.model.Section;
import com.example.geologicaltest.repository.SectionRepository;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Service for handling file import and export operations.
 * Uses Apache POI for parsing XLS files and manages job statuses.
 *
 * author Ibtehaj
 */
@Service
public class FileService {
    private final SectionRepository sectionRepository;
    private final ConcurrentHashMap<String, JobStatus> jobStatusMap = new ConcurrentHashMap<>();

    @Autowired
    public FileService(SectionRepository sectionRepository) {
        this.sectionRepository = sectionRepository;
    }

    /**
     * Asynchronous method to import data from an XLS file.
     *
     * @param file the file to be imported
     * @param jobId the ID of the job
     * @return a CompletableFuture indicating the result of the import operation
     */
    @Async
    public CompletableFuture<String> importFile(MultipartFile file, String jobId) {
        jobStatusMap.put(jobId, new JobStatus(jobId, "IN PROGRESS"));
        try (Workbook workbook = new XSSFWorkbook(file.getInputStream())) {
            List<Section> sections = parseWorkbook(workbook);
            sectionRepository.saveAll(sections);
            jobStatusMap.put(jobId, new JobStatus(jobId, "DONE"));
            return CompletableFuture.completedFuture("DONE");
        } catch (IOException e) {
            jobStatusMap.put(jobId, new JobStatus(jobId, "ERROR"));
            return CompletableFuture.completedFuture("ERROR");
        }
    }

    private List<Section> parseWorkbook(Workbook workbook) {
        Sheet sheet = workbook.getSheetAt(0);
        List<Section> sections = new ArrayList<>();
        for (Row row : sheet) {
            if (row.getRowNum() == 0) continue; // Skip header row
            Section section = new Section();
            section.setName(row.getCell(0).getStringCellValue());
            section.setGeologicalClasses(parseGeologicalClasses(row));
            sections.add(section);
        }
        return sections;
    }

    private List<GeologicalClass> parseGeologicalClasses(Row row) {
        List<GeologicalClass> geologicalClasses = new ArrayList<>();
        for (int i = 1; i < row.getLastCellNum(); i += 2) {
            GeologicalClass geoClass = new GeologicalClass();
            geoClass.setName(row.getCell(i).getStringCellValue());
            geoClass.setCode(row.getCell(i + 1).getStringCellValue());
            geologicalClasses.add(geoClass);
        }
        return geologicalClasses;
    }

    /**
     * Asynchronous method to export data to an XLS file.
     *
     * @param jobId the ID of the job
     * @return a CompletableFuture indicating the result of the export operation
     */
    @Async
    public CompletableFuture<String> exportFile(String jobId) {
        jobStatusMap.put(jobId, new JobStatus(jobId, "IN PROGRESS"));
        try (Workbook workbook = new XSSFWorkbook()) {
            List<Section> sections = sectionRepository.findAll();
            createSheet(workbook, sections);
            try (FileOutputStream fileOut = new FileOutputStream("sections.xlsx")) {
                workbook.write(fileOut);
            }
            jobStatusMap.put(jobId, new JobStatus(jobId, "DONE"));
            return CompletableFuture.completedFuture("DONE");
        } catch (IOException e) {
            jobStatusMap.put(jobId, new JobStatus(jobId, "ERROR"));
            return CompletableFuture.completedFuture("ERROR");
        }
    }

    private void createSheet(Workbook workbook, List<Section> sections) {
        Sheet sheet = workbook.createSheet("Sections");
        createHeaderRow(sheet);
        int rowNum = 1;
        for (Section section : sections) {
            createSectionRow(sheet, section, rowNum++);
        }
    }

    private void createHeaderRow(Sheet sheet) {
        Row headerRow = sheet.createRow(0);
        headerRow.createCell(0).setCellValue("Section name");
        headerRow.createCell(1).setCellValue("Class 1 name");
        headerRow.createCell(2).setCellValue("Class 1 code");
        headerRow.createCell(3).setCellValue("Class 2 name");
        headerRow.createCell(4).setCellValue("Class 2 code");
        headerRow.createCell(5).setCellValue("Class M name");
        headerRow.createCell(6).setCellValue("Class M code");
    }

    private void createSectionRow(Sheet sheet, Section section, int rowNum) {
        Row row = sheet.createRow(rowNum);
        row.createCell(0).setCellValue(section.getName());
        int cellNum = 1;
        for (GeologicalClass geoClass : section.getGeologicalClasses()) {
            row.createCell(cellNum++).setCellValue(geoClass.getName());
            row.createCell(cellNum++).setCellValue(geoClass.getCode());
        }
    }

    /**
     * Retrieves the status of a job.
     *
     * @param jobId the ID of the job
     * @return the JobStatus of the specified job
     */
    public JobStatus getJobStatus(String jobId) {
        return jobStatusMap.get(jobId);
    }
}