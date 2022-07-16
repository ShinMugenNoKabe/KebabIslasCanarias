/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */

package es.rufino.kebab.configuracion;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Configuration
@EnableJpaAuditing // A las fechas les insertará la fecha y la hora actual
public class ConfigurationAuditoria {

}
