package com.pobluesky.file.controller;

import com.pobluesky.file.dto.FileInfo;
import com.pobluesky.file.service.FileService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/upload")
public class FileUploadController {

    @Autowired
    private final FileService fileService;

    @PostMapping
    @Operation(summary = "file 업로드")
    public FileInfo uploadFile(@RequestParam("file") MultipartFile file) {
        return fileService.uploadFile(file);
    }

    @GetMapping("/download")
    @Operation(summary = "파일 다운로드 URL 생성")
    public ResponseEntity<String> generateDownloadUrl(@RequestParam("objectKey") String objectKey) {
        String signedUrl = fileService.generateSignedUrl(objectKey);
        return ResponseEntity.ok(signedUrl);
    }
}
