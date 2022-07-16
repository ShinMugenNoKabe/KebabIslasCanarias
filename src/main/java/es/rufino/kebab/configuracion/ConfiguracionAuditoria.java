/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */

package es.rufino.kebab.configuracion;

import es.rufino.kebab.upload.StorageProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Configuration
@EnableJpaAuditing
@EnableConfigurationProperties(StorageProperties.class)
public class ConfiguracionAuditoria {

}
