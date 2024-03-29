package es.rufino.kebab.controllers;

import es.rufino.kebab.services.StorageService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.MediaTypeFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Rufino Serrano Cañas
 */
@RestController
@RequestMapping("api/v1/files")
public class FileController {

    private final StorageService storageService;

    public FileController(StorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping("/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(file).orElse(MediaType.TEXT_PLAIN))
                .body(file);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<NewFileResponse> storeFile(@RequestPart(value = "file") MultipartFile file) {
        String newFileName = storageService.store(file);
        return ResponseEntity.ok()
                .body(new NewFileResponse(newFileName));
    }

    public record NewFileResponse(String fileName) {}

}
