package com.weconnect.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "reports")
public class Report {
    @Id
    private String id;
    private String subject;
    private String subjectLabel;
    private String title;
    private String details;
    private String location;
    private String fileUrl;
    private String status = "Pending";
    private String reply = "";
    private String userId;
    private Date createdAt = new Date();
    private Date updatedAt = new Date();
}
