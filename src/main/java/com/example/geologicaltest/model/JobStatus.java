package com.example.geologicaltest.model;

import lombok.Data;

/**
 * Model representing the status of a job.
 * Contains the job ID and status (e.g., "DONE", "IN PROGRESS", "ERROR").
 *
 * author Ibtehaj
 */
@Data
public class JobStatus {
    private String jobId;
    private String status; // "DONE", "IN PROGRESS", "ERROR"

    public JobStatus(String jobId, String status) {
        this.jobId = jobId;
        this.status = status;
    }
}
