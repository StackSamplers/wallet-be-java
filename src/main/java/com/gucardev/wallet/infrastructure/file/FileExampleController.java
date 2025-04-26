package com.gucardev.wallet.infrastructure.file;

import com.gucardev.wallet.infrastructure.file.dto.FileDTO;
import com.gucardev.wallet.infrastructure.file.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

//curl --location 'http://localhost:8080/api/files/upload' \
//        --form 'file=@"/C:/Users/user/Desktop/report.pdf"'

//curl --location 'http://localhost:8080/api/files/110b49e6-8300-41c9-ac40-a53dd3a57562.pdf'

//curl --location 'http://localhost:8080/api/files-example/stream/110b49e6-8300-41c9-ac40-a53dd3a57562.pdf'

@Slf4j
@RestController
@RequestMapping("/api/files-example")
@RequiredArgsConstructor
public class FileExampleController {

    private final FileService fileService;

    @PostMapping("/upload")
    public ResponseEntity<FileDTO> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            FileDTO fileDTO = fileService.storeFile(file);
            return ResponseEntity.ok(fileDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @PostMapping("/uploads")
    public ResponseEntity<List<FileDTO>> uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
        List<FileDTO> uploadedFiles = new ArrayList<>();

        for (MultipartFile file : files) {
            try {
                FileDTO fileDTO = fileService.storeFile(file);
                uploadedFiles.add(fileDTO);
            } catch (Exception e) {
                // Log the error but continue with other files
                // Consider returning partial success with error details
            }
        }

        if (uploadedFiles.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(uploadedFiles);
    }

    @GetMapping("/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) {
        try {
            byte[] fileData = fileService.retrieveFile(filename);

            ByteArrayResource resource = new ByteArrayResource(fileData);

            return ResponseEntity.ok()
                    .contentLength(fileData.length)
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + filename + "\"")
                    .body(resource);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        } catch (SecurityException | IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @GetMapping("/stream/{filename}")
    public ResponseEntity<StreamingResponseBody> streamFile(@PathVariable String filename) {
        try {
            // Get the path to the file
            Path filePath = Paths.get(fileService.getStorageLocation().toString(), filename).normalize();

            // Check if file exists
            if (!Files.exists(filePath)) {
                return ResponseEntity.notFound().build();
            }

            // Security check to prevent directory traversal
            if (!filePath.toAbsolutePath().startsWith(fileService.getStorageLocation().toAbsolutePath())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            // Get file size for Content-Length header
            long fileSize = Files.size(filePath);

            // Detect content type
            String contentType = Files.probeContentType(filePath);
            if (contentType == null) {
                contentType = MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"");
            headers.add(HttpHeaders.CONTENT_TYPE, contentType);
            headers.add(HttpHeaders.CONTENT_LENGTH, String.valueOf(fileSize));

            // Create StreamingResponseBody
            StreamingResponseBody responseBody = outputStream -> {
                try (InputStream inputStream = Files.newInputStream(filePath)) {
                    // Buffer for reading data
                    byte[] buffer = new byte[4096];
                    int bytesRead;

                    // Read from file and write to output stream
                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                        outputStream.flush();
                    }
                } catch (IOException e) {
                    log.error("Error streaming file: {}", filename, e);
                    throw new RuntimeException("Error streaming file", e);
                }
            };

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(responseBody);

        } catch (IOException e) {
            log.error("Error preparing file for streaming: {}", filename, e);
            return ResponseEntity.internalServerError().build();
        }
    }
}