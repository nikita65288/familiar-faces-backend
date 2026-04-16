package com.github.nikita65288.mapper;

import com.github.nikita65288.dto.media.ContentDto;
import com.github.nikita65288.entity.MediaFile;
import com.github.nikita65288.exception.FFFileOperationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;

@Slf4j
@Component
public class MediaMapper {

    public MediaFile toEntity(MultipartFile file, Long userId){

        byte[] content;
        try {
            content = file.getBytes();
        } catch (IOException e) {
            throw new FFFileOperationException("Error reading content of source file");
        }

        MediaFile entity = new MediaFile();
        entity.setUserId(userId);
        entity.setFileName(file.getOriginalFilename());
        entity.setContentType(file.getContentType());
        entity.setSize(file.getSize());
        entity.setUploadDate(LocalDateTime.now());
        entity.setContent(content);
        return entity;
    }

    public ContentDto mediaFileToContentDto(MediaFile file) {

        ContentDto dto = new ContentDto();

        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(file.getContentType());
        } catch (Exception e) {
            throw new FFFileOperationException("Invalid content type of file with ID: " + file.getId());
        }

        dto.setMediaType(mediaType);
        dto.setContent(file.getContent());
        return dto;
    }
}
