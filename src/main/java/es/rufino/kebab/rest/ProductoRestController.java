/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.rest;

import es.rufino.kebab.comparadores.ComparadorPorPrecio;
import es.rufino.kebab.controladores.UsuarioController;
import es.rufino.kebab.modelo.Categoria;
import es.rufino.kebab.modelo.Producto;
import es.rufino.kebab.servicios.CategoriaServicio;
import es.rufino.kebab.servicios.ProductoServicio;
import java.security.Principal;
import java.util.Comparator;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Rufino Serrano Cañas
 */
@RequestMapping("/rest-productos")
@RestController
public class ProductoRestController {

    @Autowired
    ProductoServicio productoServicio;

    @Autowired
    CategoriaServicio categoriaServicio;

    @Autowired
    HttpSession session;

    @GetMapping("/busqueda")
    public List<Producto> encuentraProductos(
            @RequestParam(name = "nombre") String nombre,
            @RequestParam(name = "id_categoria", required = false) Integer id_categoria,
            @RequestParam(name = "orden", required = false) String orden,
            @RequestParam(name = "precio_min", required = false) Double precio_min,
            @RequestParam(name = "precio_max", required = false) Double precio_max,
            @RequestParam(name = "en_oferta") Boolean en_oferta,
            @RequestParam(name = "administrador", required = false) String administrador,
            Principal principal) {

        List<Producto> productosEncontrados;

        // Si se ha introducido categoría
        if (id_categoria != null && !id_categoria.equals(0)) {
            Categoria categoria = categoriaServicio.buscarPorId(id_categoria);
            
            if (administrador != null) {
                productosEncontrados = productoServicio.buscarPorNombreEIdCategoriaAdministrador(nombre, categoria);
            } else {
                productosEncontrados = productoServicio.buscarPorNombreEIdCategoria(nombre, categoria);
            }
        } else {
            if (administrador != null) {
                productosEncontrados = productoServicio.buscarPorNombreAdministrador(nombre);
            } else {
                productosEncontrados = productoServicio.buscarPorNombre(nombre);
            }
        }

        // Si se ha introducido un orden
        if (orden != null && !orden.equals("0")) {
            Comparator comparadorPorPrecio = new ComparadorPorPrecio();

            if (orden.equals("ascendente")) {
                // Ordena la lista de forma ascendente
                productosEncontrados.sort(comparadorPorPrecio);
            } else if (orden.equals("descendente")) {
                // Ordena la lista de forma ascendente
                productosEncontrados.sort(comparadorPorPrecio.reversed());
            }
        }

        // Filtrado de precio entre precio mínimo y máximo
        if (precio_min != null && precio_max != null) {
            productosEncontrados.removeIf((p) -> p.getPrecioConDescuento() < precio_min || p.getPrecioConDescuento() > precio_max);
        }

        // Filtrado únicamente de productos en oferta
        if (en_oferta) {
            productosEncontrados.removeIf((p) -> p.getPorcentajeDeOferta().equals(0.0));
        }

        return productosEncontrados;
    }
    
}
