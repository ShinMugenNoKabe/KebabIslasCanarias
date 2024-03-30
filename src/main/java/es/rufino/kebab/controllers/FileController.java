package es.rufino.kebab.controllers;

import es.rufino.kebab.services.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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
@RequiredArgsConstructor
@Tag(name = "Files")
public class FileController {

    private final StorageService storageService;

    @Operation(
            description = "Gets an image and displays it based on its stored filename.",
            summary = "Get image",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Image not found", responseCode = "400")
            }
    )
    @GetMapping("/image/{filename:.+}")
    public ResponseEntity<Resource> serveFile(@PathVariable String filename) {
        Resource file = storageService.loadAsResource(filename);
        return ResponseEntity.ok()
                .contentType(MediaTypeFactory.getMediaType(file).orElse(MediaType.TEXT_PLAIN))
                .body(file);
    }

    @Operation(
            description = "Uploads a file. This file currently must be an image. Only administrators can upload images.",
            summary = "Get image",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", useReturnTypeSchema = true),
                    @ApiResponse(description = "File is not an image", responseCode = "415"),
                    @ApiResponse(description = "Internal Server Error", responseCode = "500")
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<NewFileResponse> storeFile(@RequestPart(value = "file") MultipartFile file) {
        String newFilename = storageService.store(file);
        return ResponseEntity.ok()
                .body(new NewFileResponse(newFilename));
    }

    public record NewFileResponse(String filename) {
    }

}
