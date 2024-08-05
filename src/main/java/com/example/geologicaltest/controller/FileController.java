package com.example.geologicaltest.controller;

import com.example.geologicaltest.model.JobStatus;
import com.example.geologicaltest.service.FileService;
import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import java.util.UUID;

/**
 * Controller for handling file import and export operations.
 * Provides endpoints for starting and checking the status of import/export jobs.
 *
 * @author Ibtehaj
 */
@RestController
@RequestMapping("/files")
public class FileController {
    @Autowired
    private FileService fileService;

    /**
     * Endpoint to start importing a file asynchronously.
     *
     * @param file the file to be imported
     * @return a ResponseEntity with the job ID
     */
    @PostMapping("/import")
    public ResponseEntity<String> importFile(@RequestParam("file") MultipartFile file) {
        String jobId = UUID.randomUUID().toString();
        fileService.importFile(file, jobId);
        return ResponseEntity.ok("Import job started. Job ID: " + jobId);
    }

    /**
     * Endpoint to get the status of an import job.
     *
     * @param id the job ID
     * @return a ResponseEntity with the job status
     */
    @GetMapping("/import/{id}")
    public ResponseEntity<JobStatus> getImportStatus(@PathVariable String id) {
        JobStatus jobStatus = fileService.getJobStatus(id);
        if (jobStatus != null) {
            return ResponseEntity.ok(jobStatus);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Endpoint to start exporting data to a file asynchronously.
     *
     * @return a ResponseEntity with the job ID
     */
    @GetMapping("/export")
    public ResponseEntity<String> exportFile() {
        String jobId = UUID.randomUUID().toString();
        fileService.exportFile(jobId);
        return ResponseEntity.ok("Export job started. Job ID: " + jobId);
    }

    /**
     * Endpoint to get the status of an export job.
     *
     * @param id the job ID
     * @return a ResponseEntity with the job status
     */
    @GetMapping("/export/{id}")
    public ResponseEntity<JobStatus> getExportStatus(@PathVariable String id) {
        JobStatus jobStatus = fileService.getJobStatus(id);
        if (jobStatus != null) {
            return ResponseEntity.ok(jobStatus);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    /**
     * Endpoint to download the exported file if the job is done.
     *
     * @param id the job ID
     * @return a ResponseEntity with the file resource
     */
    @GetMapping("/export/{id}/file")
    public ResponseEntity<Resource> getExportedFile(@PathVariable String id) {
        JobStatus jobStatus = fileService.getJobStatus(id);
        if (jobStatus != null && "DONE".equals(jobStatus.getStatus())) {
            Resource file = (Resource) new FileSystemResource("sections.xlsx");
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=sections.xlsx")
                    .body(file);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
    }
}
