/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.repositories;

import es.rufino.kebab.modelo.Categoria;
import es.rufino.kebab.modelo.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Rufino Serrano Cañas
 */
public interface ProductoRepository extends JpaRepository<Producto, Integer> {

    List<Producto> findByCategoria(Categoria categoria);

    List<Producto> findByNombreContainsIgnoreCase(String nombre);

    public List<Producto> findByCategoriaEqualsAndDisponibleEquals(Categoria categoria, Boolean disponible);

    public List<Producto> findByNombreContainsIgnoreCaseAndCategoriaEqualsAndDisponibleEquals(String nombre, Categoria categoria, Boolean disponible);

    public List<Producto> findByNombreContainsIgnoreCaseAndDisponibleEquals(String nombre, boolean disponible);

    public List<Producto> findByNombreContainsIgnoreCaseAndCategoriaEquals(String nombre, Categoria categoria);

    public List<Producto> findByCategoriaEquals(Categoria categoria);

}
