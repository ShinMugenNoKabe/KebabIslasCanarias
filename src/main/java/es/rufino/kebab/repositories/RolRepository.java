/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.repositories;

import es.rufino.kebab.modelo.Rol;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Rufino Serrano Cañas
 */
public interface RolRepository extends JpaRepository<Rol, Integer> {
    
    public Rol findByNombre(String nombre);

}
