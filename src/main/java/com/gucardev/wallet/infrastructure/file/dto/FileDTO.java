package com.gucardev.wallet.infrastructure.file.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FileDTO {
    private String id;
    private String originalName;
    private String storedName;
    private String fileExtension;
    private long fileSize;
    private String contentType;
    private LocalDateTime uploadDate;
    private String fileUrl;
}
