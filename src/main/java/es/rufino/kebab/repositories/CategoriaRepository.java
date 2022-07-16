/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.repositories;

import es.rufino.kebab.modelo.Categoria;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Rufino Serrano Cañas
 */
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    
    public Categoria findByNombre(String nom_categoria);
    
}
