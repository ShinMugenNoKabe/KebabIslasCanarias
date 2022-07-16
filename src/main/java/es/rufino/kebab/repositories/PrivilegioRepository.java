/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.repositories;

import es.rufino.kebab.modelo.Privilegio;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Rufino Serrano Cañas
 */
public interface PrivilegioRepository extends JpaRepository<Privilegio, Integer> {

    public Privilegio findByNombre(String nombre);
    
}
