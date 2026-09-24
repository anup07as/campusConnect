package CampusConnect;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin(origins = "https://campusconnect-web-me6v.onrender.com")
public class FileController {

    @GetMapping("/uploads/{fileName}")
    public ResponseEntity<Resource> getFile(
            @PathVariable String fileName) {

        try {

            Path uploadDirectory = Paths.get("uploads")
        .toAbsolutePath()
        .normalize();

Path filePath = uploadDirectory
        .resolve(fileName)
        .normalize();

if (!filePath.startsWith(uploadDirectory)) {
    return ResponseEntity
            .status(HttpStatus.FORBIDDEN)
            .build();
}

            Resource resource =
                    new UrlResource(filePath.toUri());

            if (!resource.exists()) {
                return ResponseEntity.notFound().build();
            }

            String contentType =
                    Files.probeContentType(filePath);

            if (contentType == null) {
                contentType =
                        MediaType.APPLICATION_OCTET_STREAM_VALUE;
            }

            return ResponseEntity.ok()
                    .contentType(
                            MediaType.parseMediaType(contentType)
                    )
                    .header(
                            HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" +
                            resource.getFilename() +
                            "\""
                    )
                    .body(resource);

        } catch (Exception e) {

            e.printStackTrace();

            return ResponseEntity
                    .internalServerError()
                    .build();
        }
    }
}