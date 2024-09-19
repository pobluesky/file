package com.pobluesky.file.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
public class FileInfo {

    private String originName;

    private String storedFilePath;

    @Builder
    private FileInfo(
        String originName,
        String storedFilePath
    ) {
        this.originName = originName;
        this.storedFilePath = storedFilePath;
    }
}
