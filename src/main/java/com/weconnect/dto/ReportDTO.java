package com.weconnect.dto;

import jakarta.validation.constraints.NotBlank;

public class ReportDTO {
    @NotBlank
    public String subject;
    @NotBlank
    public String subjectLabel;
    @NotBlank
    public String title;
    @NotBlank
    public String details;
    @NotBlank
    public String location;
    public String fileUrl;
}
