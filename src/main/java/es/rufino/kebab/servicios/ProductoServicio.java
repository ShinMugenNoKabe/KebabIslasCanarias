/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.servicios;

import es.rufino.kebab.modelo.Categoria;
import es.rufino.kebab.modelo.Producto;
import es.rufino.kebab.modelo.Usuario;
import es.rufino.kebab.repositories.ProductoRepository;
import es.rufino.kebab.upload.StorageService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Service
public class ProductoServicio {

    @Autowired
    ProductoRepository repositorio;
    
    // Servicio de almacenamiento
    @Autowired
    StorageService storageService;

    public Producto insertar(Producto p) {
        return repositorio.save(p);
    }

    public void eliminar(Integer id) {
        repositorio.deleteById(id);
    }
    
    public void eliminar(Producto p) {
        if (!p.getImagen().isEmpty()) {
            storageService.delete(p.getImagen());
        }
        
        repositorio.delete(p);
    }

    public Producto editar(Producto p) {
        return repositorio.save(p);
    }

    public Producto findById(Integer id) {
        return repositorio.findById(id).orElse(null);
    }

    public List<Producto> findAll() {
        return repositorio.findAll();
    }

    public List<Producto> productosDeUnaCategoria(Categoria categoria) {
        return repositorio.findByCategoria(categoria);
    }
    
    public List<Producto> buscarPorNombre(String query) {
        return repositorio.findByNombreContainsIgnoreCaseAndDisponibleEquals(query, true);
    }
    
    public List<Producto> buscarPorNombreEIdCategoria(String nombre, Categoria categoria) {
        return repositorio.findByNombreContainsIgnoreCaseAndCategoriaEqualsAndDisponibleEquals(nombre, categoria, true);
    }
    
    public List<Producto> buscarPorIdCategoria(Categoria categoria) {
        return repositorio.findByCategoriaEqualsAndDisponibleEquals(categoria, true);
    }
    
    public List<Producto> buscarPorNombreAdministrador(String query) {
        return repositorio.findByNombreContainsIgnoreCase(query);
    }
    
    public List<Producto> buscarPorNombreEIdCategoriaAdministrador(String nombre, Categoria categoria) {
        return repositorio.findByNombreContainsIgnoreCaseAndCategoriaEquals(nombre, categoria);
    }
    
    public List<Producto> buscarPorIdCategoriaAdministrador(Categoria categoria) {
        return repositorio.findByCategoriaEquals(categoria);
    }
    
    public List<Producto> variosPorId(List<Integer> ids) {
        return repositorio.findAllById(ids);
    }

}
