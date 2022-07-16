/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.rest;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import es.rufino.kebab.comparadores.ComparadorPorPrecio;
import es.rufino.kebab.modelo.Categoria;
import es.rufino.kebab.modelo.Pedido;
import es.rufino.kebab.modelo.Producto;
import es.rufino.kebab.servicios.PedidoServicio;
import es.rufino.kebab.servicios.ProductoServicio;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Rufino Serrano Cañas
 */
@RequestMapping("/rest-pedidos")
@RestController
public class PedidoRestController {

    @Autowired
    private HttpSession session;

    @Autowired
    private ProductoServicio productoServicio;

    @Autowired
    private PedidoServicio pedidoServicio;

    /**
     * Añade un producto al pedido. Si ya existe en el carrito, actualiza su
     * cantidad
     *
     * @param id_producto ID del producto a añadir
     * @param cantidad Cantidad a añadir
     * @return Respuesta en JSON
     */
    @GetMapping("/anadir-producto-a-pedido")
    public String anadeProductoACarrito(
            @RequestParam(name = "id_producto") Integer id_producto,
            @RequestParam(name = "cantidad") Integer cantidad) {

        HashMap<Producto, Integer> carrito = pedidoServicio.getCarrito();

        Producto productoAAnadir = productoServicio.findById(id_producto);

        HashMap<String, String> respuesta = new HashMap<>();

        Gson gson = new Gson();

        // Si no existe el producto
        if (productoAAnadir == null) {
            respuesta.put("msg", "El producto buscado no existe.");

            return gson.toJson(respuesta);
        }

        boolean isInCarrito = false;

        for (Producto p : carrito.keySet()) {
            if (p.getCodprod().equals(productoAAnadir.getCodprod())) {
                isInCarrito = true;
                carrito.put(p, (cantidad + carrito.get(p)));
                //carrito.get(p) = cantidad;
                break;
            }
        }

        if (!isInCarrito) {
            carrito.put(productoAAnadir, cantidad);
        }

        // Vuelve a guardar el carrito
        session.setAttribute("carrito", carrito);

        respuesta.put("msg", "El producto <b>" + productoAAnadir.getNombre()
                + "</b> añadido al pedido - cantidad: " + cantidad);

        return gson.toJson(respuesta);
    }

}
