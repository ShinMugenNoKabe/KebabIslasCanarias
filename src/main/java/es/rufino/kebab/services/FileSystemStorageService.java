package es.rufino.kebab.services;

import es.rufino.kebab.configuration.StorageProperties;
import es.rufino.kebab.exceptions.storage.FileIsNotImageException;
import es.rufino.kebab.exceptions.storage.StorageException;
import es.rufino.kebab.exceptions.storage.StorageFileNotFoundException;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * Service class used to upload files to the system.
 * Thanks to <a href="https://github.com/spring-guides/gs-uploading-files/blob/main/complete/src/main/java/com/example/uploadingfiles/storage/FileSystemStorageService.java">this example</a>
 *
 * @author ShinMugenNoKabe
 */
@Service
public class FileSystemStorageService implements StorageService {

    /**
     * Parent directory for all files
     */
    private final Path rootLocation;

    private final List<MediaType> ACCEPTED_MEDIA_TYPES = List.of(
            MediaType.IMAGE_JPEG, MediaType.IMAGE_PNG, MediaType.IMAGE_GIF
    );

    public FileSystemStorageService(StorageProperties properties) {
        this.rootLocation = Paths.get(properties.IMAGES_FOLDER_LOCATION);
    }

    /**
     * Store a {@link org.springframework.web.multipart.MultipartFile} object inside the system.
     * The object fill be parsed accordingly.
     * @param file File to upload
     * @return Filename saved in the system
     */
    @Override
    public String store(MultipartFile file) {
        if (file == null) {
            throw new UnsupportedMediaTypeStatusException("The uploaded file is empty.");
        }

        if (!fileIsImage(file)) {
            throw new FileIsNotImageException("The file is not an image.", ACCEPTED_MEDIA_TYPES);
        }

        String filename = StringUtils.cleanPath(Objects.requireNonNull(file.getOriginalFilename()));
        String extension = StringUtils.getFilenameExtension(filename);
        String justFilename = filename.replace("." + extension, "");
        String storedFilename = System.currentTimeMillis() + "_" + justFilename + "." + extension;

        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }

            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException("Cannot store file with relative path outside current directory " + filename);
            }

            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, this.rootLocation.resolve(storedFilename), StandardCopyOption.REPLACE_EXISTING);
                return storedFilename;
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    private boolean fileIsImage(MultipartFile file) {
        return Objects.requireNonNull(file.getContentType()).startsWith("image");
    }

    /**
     * Filters and returns all files that exist in the project's secondary storage.
     * @return Filtered files as a Stream
     */
    @Override
    public Stream<Path> loadAll() {
        try (
                Stream<Path> pathStream = Files.walk(this.rootLocation, 1);
        ) {
            return pathStream
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    /**
     * Finds and returns a file based on its filename, as a Path object.
     * @param filename Filename
     * @return Path object that represents the file
     */
    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    /**
     * Finds and returns a file based on its filename, as a Resource object.
     * @param filename Filename
     * @return Resource object that represents the file
     */
    @Override
    public Resource loadAsResource(String filename) {
        try {
            Path file = load(filename);
            Resource resource = new UrlResource(file.toUri());

            if (!resource.exists() || !resource.isReadable()) {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }

            return resource;
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    /**
     * Deletes all the files stored in the secondary storage.
     */
    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    /**
     * Initializes the project's second storage
     */
    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }

    /**
     * Deletes a File based on its filename.
     *
     * @param filename Filename
     */
    @Override
    public void delete(String filename) {
        String justFilename = StringUtils.getFilename(filename);

        try {
            Path file = load(justFilename);
            Files.deleteIfExists(file);
        } catch (IOException e) {
            throw new StorageException("Error al eliminar un fichero", e);
        }
    }

}
