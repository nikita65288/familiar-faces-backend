package com.github.nikita65288.dto.media;

import lombok.Data;
import org.springframework.http.MediaType;

@Data
public class ContentDto {

    private MediaType mediaType;
    private byte[] content;
}
