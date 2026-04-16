package com.github.nikita65288.service;

import com.github.nikita65288.dto.media.ContentDto;
import com.github.nikita65288.entity.MediaFile;
import com.github.nikita65288.exception.FFMediaNotFoundException;
import com.github.nikita65288.exception.FFNotFoundException;
import com.github.nikita65288.mapper.MediaMapper;
import com.github.nikita65288.repository.MediaRepository;
import com.github.nikita65288.validator.MediaValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

import java.util.UUID;

@Service
@Slf4j
public class MediaService {

    private final MediaRepository mediaRepository;
    private final MediaMapper mediaMapper;
    private final MediaValidator mediaValidator;

    @Autowired
    public MediaService(
            MediaRepository mediaRepository,
            MediaMapper mediaMapper,
            MediaValidator mediaValidator
    ) {
        this.mediaRepository = mediaRepository;
        this.mediaMapper = mediaMapper;
        this.mediaValidator = mediaValidator;
    }

    @Transactional
    public String saveFile(MultipartFile file, Long userId) {

        mediaValidator.validate(file);

        MediaFile entity = mediaMapper.toEntity(file, userId);

        String id = UUID.randomUUID().toString();
        entity.setId(id);

        MediaFile savedFile = mediaRepository.save(entity);

        log.info("The media file was saved successfully: File ID = {}, User ID = {}", savedFile.getId(), userId);

        return "/media/download/" + savedFile.getId();
    }

    @Transactional(readOnly = true)
    public ContentDto getFile(String id) {
        MediaFile mediaFile = mediaRepository.findById(id)
                .orElseThrow(() -> new FFNotFoundException("Файл не найден: ID файла = " + id));


        return mediaMapper.mediaFileToContentDto(mediaFile);
    }

    public DownloadedFile loadFile(String id) {

        MediaFile mediaFile = mediaRepository.findById(id)
                .orElseThrow(() -> new FFMediaNotFoundException("Файл не найден: ID файла = " + id));

        String contentType = (mediaFile.getContentType() == null || mediaFile.getContentType().isBlank())
                ? "application/octet-stream"
                : mediaFile.getContentType();

        String filename = (mediaFile.getFileName() == null || mediaFile.getFileName().isBlank())
                ? id
                : mediaFile.getFileName();

        Resource resource = new ByteArrayResource(mediaFile.getContent()) {
            @Override
            public String getFilename() {
                return filename;
            }
        };

        return new DownloadedFile(resource, contentType, filename);
    }

    public record DownloadedFile(Resource resource, String contentType, String filename) {}
}
