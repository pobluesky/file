package com.pobluesky.file.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;

import com.pobluesky.file.dto.FileInfo;

import com.pobluesky.global.error.FileUploadException;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.IOException;

import java.util.UUID;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
@RequiredArgsConstructor
public class FileService {

    private final AmazonS3 amazonS3;

    // Dotenv dotenv = Dotenv.load();

    private final String bucketName = System.getenv("S3_BUCKET_NAME");
    // private final String bucketName = dotenv.get("S3_BUCKET_NAME");

    public FileInfo uploadFile(MultipartFile file) {
        String originName = file.getOriginalFilename();

        String storedFilePath = uploadFileToS3(file);

        return FileInfo.builder()
            .originName(originName)
            .storedFilePath(storedFilePath)
            .build();
    }

    private String uploadFileToS3(MultipartFile file) {
        String originName = file.getOriginalFilename();

        String ext = originName.substring(originName.lastIndexOf(".") + 1);

        String changedName = changedFileName(originName);

        ObjectMetadata metadata = new ObjectMetadata();

        metadata.setContentLength(file.getSize());

        switch (ext.toLowerCase()) {
            case "png":
                metadata.setContentType("image/png");
                break;

            case "jpg":
            case "jpeg":
                metadata.setContentType("image/jpeg");
                break;

            case "gif":
                metadata.setContentType("image/gif");
                break;

            case "xls":
                metadata.setContentType("application/vnd.ms-excel");
                break;

            case "xlsx":
                metadata.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                break;

            case "pdf":
                metadata.setContentType("application/pdf");
                break;

            // 다른 파일 타입도 필요에 따라 추가
            default:
                metadata.setContentType("application/octet-stream");
                break;
        }
        try {
            amazonS3.putObject(new PutObjectRequest(bucketName, changedName, file.getInputStream(), metadata));
        } catch (IOException e) {
            throw new FileUploadException();
        }

        return amazonS3.getUrl(bucketName, changedName).toString();
    }

    private String changedFileName(String originName) {
        String random = UUID.randomUUID().toString();

        return random + originName;
    }
}
