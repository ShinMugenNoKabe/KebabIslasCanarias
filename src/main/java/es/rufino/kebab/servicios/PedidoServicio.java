/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.servicios;

import es.rufino.kebab.modelo.LineaPedido;
import es.rufino.kebab.modelo.Pedido;
import es.rufino.kebab.modelo.Producto;
import es.rufino.kebab.modelo.Usuario;
import es.rufino.kebab.repositories.PedidoRepository;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Service
public class PedidoServicio {

    @Autowired
    private PedidoRepository repositorio;
    
    @Autowired
    private HttpSession session;

    public Pedido insertar(Pedido p, Usuario u) {
        p.setUsuario(u);
        return repositorio.save(p);
    }

    public Pedido insertar(Pedido p) {
        return repositorio.save(p);
    }

    public Pedido addLineaPedido(LineaPedido lp, Pedido p) {
        p.getListaDeLineasDePedido().add(lp);
        return insertar(p);
    }

    public Pedido buscarPorId(Integer id) {
        return repositorio.findById(id).orElse(null);
    }

    public List<Pedido> buscarTodos() {
        return repositorio.findAll();
    }

    public List<Pedido> buscarPorComprador(Usuario u) {
        return repositorio.findByUsuario(u);
    }
    
    public List<Pedido> buscarEntreDosFechas(Date fechaMin, Date fechaMax) {
        return repositorio.findByFechaBetween(fechaMin, fechaMax);
    }
    
    public List<Pedido> buscarEntreDosFechas(Usuario usuario, Date fechaMin, Date fechaMax) {
        return repositorio.findByUsuarioEqualsAndFechaBetween(usuario, fechaMin, fechaMax);
    }
    
    /**
     * Devuelve el carrito guardado en la sesión. Si no existe, crea uno vacío
     *
     * @return Carrito guardado en la sesión
     */
    public HashMap<Producto, Integer> getCarrito() {
        HashMap<Producto, Integer> carrito = (HashMap<Producto, Integer>) session.getAttribute("carrito");

        if (carrito == null) {
            carrito = new HashMap<>();
            session.setAttribute("carrito", carrito);
        }

        return carrito;
    }
    
}
