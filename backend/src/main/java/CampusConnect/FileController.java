package CampusConnect;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.nio.file.Path;
import java.nio.file.Paths;

@RestController
@CrossOrigin(origins = "http://127.0.0.1:5500")
public class FileController {

    @GetMapping("/uploads/{fileName}")
    public ResponseEntity<Resource> getFile(@PathVariable String fileName) {

        try {

            Path filePath = Paths.get("uploads")
                    .resolve(fileName)
                    .normalize();

            Resource resource = new UrlResource(filePath.toUri());

            if (resource.exists()) {

                return ResponseEntity.ok()
                        .contentType(MediaType.APPLICATION_PDF)
                        .header(
                                HttpHeaders.CONTENT_DISPOSITION,
                                "inline; filename=\"" + resource.getFilename() + "\""
                        )
                        .body(resource);
            }

            return ResponseEntity.notFound().build();

        } catch (Exception e) {

            return ResponseEntity.internalServerError().build();
        }
    }
}