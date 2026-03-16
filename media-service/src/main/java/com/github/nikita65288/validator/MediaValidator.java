package com.github.nikita65288.validator;

import com.github.nikita65288.enums.AllowedContentType;
import com.github.nikita65288.exception.FFIllegalArgumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@Slf4j
public class MediaValidator {

    public void validate(MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new FFIllegalArgumentException("The file cannot be empty");
        }

        String contentType = file.getContentType();
        if (!AllowedContentType.isValid(contentType)) {
            log.warn("Invalid file type: {}", contentType);
            throw new FFIllegalArgumentException("File type " + contentType + " is not supported");
        }
    }
}
