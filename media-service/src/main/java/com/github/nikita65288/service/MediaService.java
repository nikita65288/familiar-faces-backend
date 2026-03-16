package com.github.nikita65288.service;

import com.github.nikita65288.dto.media.ContentDto;
import com.github.nikita65288.entity.MediaFile;
import com.github.nikita65288.exception.FFNotFoundException;
import com.github.nikita65288.mapper.MediaMapper;
import com.github.nikita65288.repository.MediaRepository;
import com.github.nikita65288.validator.MediaValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

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

        MediaFile savedFile = mediaRepository.save(entity);

        log.info("The media file was saved successfully: File ID = {}, User ID = {}", savedFile.getId(), userId);

        return "/api/media/download/" + savedFile.getId();
    }

    @Transactional(readOnly = true)
    public ContentDto getFile(String id) {
        MediaFile mediaFile = mediaRepository.findById(id)
                .orElseThrow(() -> new FFNotFoundException("File not found: File ID = " + id));


        return mediaMapper.mediaFileToContentDto(mediaFile);
    }
}
