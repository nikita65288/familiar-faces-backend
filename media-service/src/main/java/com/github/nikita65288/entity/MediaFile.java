package com.github.nikita65288.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Data
@Document(collection = "media_files")
public class MediaFile {
    @Id
    private String id;
    private Long userId;          // userId from auth-service
    private String fileName;
    private String contentType;   // image/jpeg, image/png etc.
    private long size;
    private LocalDateTime uploadDate;
    private byte[] content;
}
