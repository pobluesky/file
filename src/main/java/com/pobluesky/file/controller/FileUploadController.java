package com.pobluesky.file.controller;

import com.pobluesky.file.dto.FileInfo;
import com.pobluesky.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/upload")
public class FileUploadController {

    @Autowired
    private final FileService fileService;

    @PostMapping
    @Operation(summary = "file 업로드")
    public FileInfo uploadFile(@RequestPart("file") MultipartFile file) {
        log.debug("파일 업로드 시작 controller");
        return fileService.uploadFile(file);
    }
}
