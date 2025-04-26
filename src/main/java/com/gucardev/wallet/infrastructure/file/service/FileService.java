package com.gucardev.wallet.infrastructure.file.service;

import com.gucardev.wallet.infrastructure.file.dto.FileDTO;
import jakarta.annotation.PostConstruct;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class FileService {

    @Value("${app-specific-configs.storage.directory:./storage}")
    private String storageDirectory;

    @Value("${app-specific-configs.storage.allowed-extensions:jpg,jpeg,png,pdf,doc,docx}")
    private String allowedExtensions;

    private List<String> allowedExtensionsList;

    @Getter
    private Path storageLocation;

    @PostConstruct
    public void init() {
        try {
            // Parse allowed extensions
            allowedExtensionsList = Arrays.asList(allowedExtensions.split(","));
            log.info("Allowed file extensions: {}", allowedExtensionsList);

            // Create storage directory if it doesn't exist
            storageLocation = Paths.get(storageDirectory).toAbsolutePath().normalize();
            Files.createDirectories(storageLocation);

            // Check if the directory is readable and writable
            File directory = storageLocation.toFile();
            if (!directory.canRead()) {
                throw new RuntimeException("Storage directory is not readable: " + storageLocation);
            }
            if (!directory.canWrite()) {
                throw new RuntimeException("Storage directory is not writable: " + storageLocation);
            }

            log.info("File storage directory initialized at: {}", storageLocation);
        } catch (IOException ex) {
            throw new RuntimeException("Could not initialize storage directory", ex);
        }
    }

    public FileDTO storeFile(MultipartFile file) throws IOException {
        // Check if file is empty
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Failed to store empty file.");
        }

        // Check file extension
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || originalFilename.isEmpty()) {
            throw new IllegalArgumentException("File must have a name.");
        }

        // Sanitize the original filename
        String sanitizedOriginalName = sanitizeFileName(originalFilename);

        String fileExtension = getFileExtension(originalFilename);
        if (!isAllowedExtension(fileExtension)) {
            throw new IllegalArgumentException("File extension not allowed: " + fileExtension);
        }

        // Generate new filename with UUID
        String storedFilename = UUID.randomUUID() + "." + fileExtension;
        Path targetLocation = storageLocation.resolve(storedFilename);

        // Store the file
        Files.copy(file.getInputStream(), targetLocation);

        log.info("Stored file {} as {}", sanitizedOriginalName, storedFilename);

        // Create and return FileDTO
        String fileId = UUID.randomUUID().toString();
        String fileUrl = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/api/files/")
                .path(storedFilename)
                .toUriString();

        return new FileDTO(
                fileId,
                sanitizedOriginalName,
                storedFilename,
                fileExtension,
                file.getSize(),
                file.getContentType(),
                LocalDateTime.now(),
                fileUrl
        );
    }

    public byte[] retrieveFile(String filename) throws IOException {
        Path filePath = storageLocation.resolve(filename).normalize();

        // Check if the file exists
        if (!Files.exists(filePath)) {
            throw new IllegalArgumentException("File not found: " + filename);
        }

        // Validate that the file is within the storage directory (prevents path traversal)
        if (!filePath.toAbsolutePath().startsWith(storageLocation.toAbsolutePath())) {
            throw new SecurityException("Access to file outside storage directory denied");
        }

        return Files.readAllBytes(filePath);
    }

    private String getFileExtension(String filename) {
        int lastDotIndex = filename.lastIndexOf('.');
        if (lastDotIndex == -1) {
            return ""; // No extension
        }
        return filename.substring(lastDotIndex + 1).toLowerCase();
    }

    private boolean isAllowedExtension(String extension) {
        return allowedExtensionsList.contains(extension.toLowerCase());
    }

    private String sanitizeFileName(String fileName) {
        // Remove path information if present
        String name = new File(fileName).getName();

        // Remove any character that's not alphanumeric, dot, dash or underscore
        name = name.replaceAll("[^a-zA-Z0-9.\\-_]", "_");

        // Convert multiple sequential special characters to single ones
        name = name.replaceAll("_{2,}", "_");

        // Limit length to prevent extremely long filenames
        if (name.length() > 100) {
            int dotIndex = name.lastIndexOf('.');
            if (dotIndex > 0) {
                String baseName = name.substring(0, dotIndex);
                String extension = name.substring(dotIndex);
                if (baseName.length() > 90) {
                    baseName = baseName.substring(0, 90);
                }
                name = baseName + extension;
            } else {
                name = name.substring(0, 100);
            }
        }

        return name;
    }
}