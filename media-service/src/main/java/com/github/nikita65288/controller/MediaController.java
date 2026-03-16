package com.github.nikita65288.controller;

import com.github.nikita65288.dto.media.ContentDto;
import com.github.nikita65288.service.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/media")
public class MediaController {

    private final MediaService mediaService;

    @Autowired
    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> upload(
            @RequestParam("file") MultipartFile file,
            @RequestParam("userId") Long userId
    ){
        String downloadUrl = mediaService.saveFile(file, userId);
        return ResponseEntity.ok(downloadUrl);
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> download(@PathVariable String id) {
        ContentDto file = mediaService.getFile(id);
        return ResponseEntity.ok()
                .contentType(file.getMediaType())
                .body(file.getContent());
    }
}
