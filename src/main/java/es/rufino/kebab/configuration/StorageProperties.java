package es.rufino.kebab.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "storage")
public class StorageProperties {

    public final String IMAGES_FOLDER_LOCATION = "src/main/resources/static/images";

}
