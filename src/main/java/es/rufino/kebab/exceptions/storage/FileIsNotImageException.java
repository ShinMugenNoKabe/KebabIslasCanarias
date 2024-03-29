package es.rufino.kebab.exceptions.storage;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.UnsupportedMediaTypeStatusException;

import java.util.List;

@ResponseStatus(code = HttpStatus.UNSUPPORTED_MEDIA_TYPE)
public class FileIsNotImageException extends UnsupportedMediaTypeStatusException {

    public FileIsNotImageException(String reason) {
        super(reason);
    }

    public FileIsNotImageException(String reason, List<MediaType> supportedTypes) {
        super(reason, supportedTypes);
    }

}
