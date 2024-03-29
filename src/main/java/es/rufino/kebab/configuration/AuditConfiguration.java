package es.rufino.kebab.configuration;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

/**
 * Class used to set some configuration needed for Spring Boot.
 *
 * @author ShinMugenNoKabe
 */
@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "es.rufino.kebab.repositories")
@EnableConfigurationProperties(StorageProperties.class)
public class AuditConfiguration {

}
