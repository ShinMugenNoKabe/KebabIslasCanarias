package es.rufino.kebab.services;

import org.springframework.core.io.Resource;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * <p>This interfaces allows us to define an abstract secondary storage of files,
 * so we can use it in any controller.</p>
 *
 * <p>This way, we can use a storage that accesses our system, or we could implement another
 * that will be in a remote system.</p>
 *
 * @author ShinMugenNoKane
 */
@Service
public interface StorageService {

    void init();

    String store(@NonNull MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(String filename);

    void delete(String filename);

    void deleteAll();

}
