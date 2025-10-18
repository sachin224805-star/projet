package com.weconnect.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "users")
public class User {
    @Id
    private String id;
    private String name;
    private String phone;
    private String password; // hashed
    private String role = "user";
    private Date createdAt = new Date();
}
