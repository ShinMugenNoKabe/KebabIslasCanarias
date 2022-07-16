/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.controladores;

import com.google.gson.Gson;
import es.rufino.kebab.modelo.Categoria;
import es.rufino.kebab.modelo.LineaPedido;
import es.rufino.kebab.modelo.Pedido;
import es.rufino.kebab.modelo.Producto;
import es.rufino.kebab.modelo.Usuario;
import es.rufino.kebab.servicios.PedidoServicio;
import es.rufino.kebab.servicios.ProductoServicio;
import es.rufino.kebab.servicios.UsuarioServicio;
import java.security.Principal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import static org.springframework.jdbc.core.JdbcOperationsExtensionsKt.query;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Controller
@RequestMapping("/")
public class PedidoController {

    @Autowired
    private PedidoServicio compraServicio;

    @Autowired
    private ProductoServicio productoServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private PedidoServicio pedidoServicio;

    @Autowired
    private HttpSession session;

    @Autowired
    private UsuarioController usuarioController;

    @GetMapping("/pedido")
    public String verPedido(Model model) {
        HashMap<Producto, Integer> carrito = pedidoServicio.getCarrito();

        model.addAttribute("pedido", new Pedido());
        model.addAttribute("precioTotal", calculoPrecioTotalCarrito(carrito));
        model.addAttribute("carrito", carrito);

        return "pedidos/pedido";
    }

    @PostMapping("/realizar-pedido")
    public String realizarPedido(Model model,
            @Valid Pedido nuevoPedido,
            BindingResult result,
            Principal principal) {

        HashMap<Producto, Integer> carrito = pedidoServicio.getCarrito();

        if (nuevoPedido.getDireccionDeEnvio() != null) {
            if (nuevoPedido.getRecogidaEnTienda() == true && nuevoPedido.getDireccionDeEnvio().length() > 0) {
                result.rejectValue("recogidaEnTienda", "error.pedido",
                        "Marque esta opción si va a recoger el producto en la tienda.");
            }

            if (nuevoPedido.getRecogidaEnTienda() == false && nuevoPedido.getDireccionDeEnvio().length() == 0) {
                result.rejectValue("direccionDeEnvio", "error.pedido",
                        "Indique una dirección de envío.");
            }
        }

        if (nuevoPedido.getRecogidaEnTienda() == true) {
            nuevoPedido.setDireccionDeEnvio(null);
        }

        if (result.hasErrors()) {
            model.addAttribute("precioTotal", calculoPrecioTotalCarrito(carrito));
            model.addAttribute("carrito", carrito);

            return "pedidos/pedido";
        }

        List<LineaPedido> lineasDePedido = new LinkedList<>();

        LineaPedido lp;

        for (Producto p : carrito.keySet()) {
            lp = new LineaPedido(carrito.get(p), (p.getPrecioConDescuento() * carrito.get(p)));
            lp.setPedido(nuevoPedido);
            lp.setProducto(p);
            lineasDePedido.add(lp);
        }

        nuevoPedido.setListaDeLineasDePedido(lineasDePedido);

        Usuario usuarioLoggeado = usuarioServicio.getUsuarioLoggeado(principal);

        nuevoPedido.setUsuario(usuarioLoggeado);
        pedidoServicio.insertar(nuevoPedido);

        carrito.clear();

        return "redirect:/mis-pedidos";
    }

    @GetMapping("/mis-pedidos")
    public String misPedidos(Model model,
            Principal principal,
            @RequestParam(name = "fecha_min", required = false) String fecha_min,
            @RequestParam(name = "fecha_max", required = false) String fecha_max) {
        Usuario usuarioLoggeado = usuarioServicio.getUsuarioLoggeado(principal);

        List<Pedido> pedidosEncontrados;

        // Si se ha introducido fecha
        if (fecha_min != null && fecha_max != null) {
            try {
                Date fechaMin = null, fechaMax = null;

                fechaMin = new SimpleDateFormat("yyyy-MM-dd").parse(fecha_min);
                fechaMax = new SimpleDateFormat("yyyy-MM-dd").parse(fecha_max);

                pedidosEncontrados = pedidoServicio.buscarEntreDosFechas(usuarioLoggeado, fechaMin, fechaMax);
            } catch (ParseException ex) {
                pedidosEncontrados = usuarioLoggeado.getListaDePedidos();
            }
        } else {
            pedidosEncontrados = usuarioLoggeado.getListaDePedidos();
        }

        model.addAttribute("pedidos", pedidosEncontrados);

        return "pedidos/lista-pedidos";
    }

    @GetMapping("/gestion-pedidos")
    public String todosLosPedidos(Model model,
            Principal principal,
            @RequestParam(name = "fecha_min", required = false) String fecha_min,
            @RequestParam(name = "fecha_max", required = false) String fecha_max,
            @RequestParam(name = "email", required = false) String email) {

        if (!usuarioController.comprobarAdmin(principal)) {
            return "index";
        }

        Usuario usuarioBuscado = usuarioServicio.buscarPorEmail(email);

        List<Pedido> pedidosEncontrados = pedidoServicio.buscarTodos();

        // Si se ha introducido fecha
        if (fecha_min != null && fecha_max != null) {
            if (!fecha_min.equals("") && !fecha_max.equals("")) {
                try {
                    Date fechaMin = null, fechaMax = null;

                    fechaMin = new SimpleDateFormat("yyyy-MM-dd").parse(fecha_min);
                    fechaMax = new SimpleDateFormat("yyyy-MM-dd").parse(fecha_max);

                    pedidosEncontrados = pedidoServicio.buscarEntreDosFechas(usuarioBuscado, fechaMin, fechaMax);
                } catch (ParseException ex) {
                    pedidosEncontrados = pedidoServicio.buscarTodos();
                }
            } else {
                if (usuarioBuscado != null) {
                    pedidosEncontrados = usuarioBuscado.getListaDePedidos();
                }
            }
        } else {
            if (usuarioBuscado != null) {
                pedidosEncontrados = usuarioBuscado.getListaDePedidos();
            }
        }

        model.addAttribute("pedidos", pedidosEncontrados);

        return "pedidos/lista-pedidos";
    }

    private Double calculoPrecioTotalCarrito(HashMap<Producto, Integer> carrito) {
        Double precioTotal = 0.0;

        // Cálculo del precio total
        for (Producto p : carrito.keySet()) {
            precioTotal += (p.getPrecioConDescuento() * carrito.get(p));
        }

        return precioTotal;
    }
    
    @GetMapping("/quitar-producto-de-pedido/{id}")
    public String anadeProductoACarrito(
            Model model,
            @PathVariable Integer id) {

        HashMap<Producto, Integer> carrito = pedidoServicio.getCarrito();

        Producto productoAQuitar = productoServicio.findById(id);

        HashMap<String, String> respuesta = new HashMap<>();

        Gson gson = new Gson();

        // Si no existe el producto
        if (productoAQuitar == null) {
            respuesta.put("msg", "El producto buscado no existe.");

            return gson.toJson(respuesta);
        }

        for (Producto p : carrito.keySet()) {
            if (p.getCodprod().equals(productoAQuitar.getCodprod())) {
                carrito.remove(p);
                //carrito.get(p) = cantidad;
                break;
            }
        }

        // Vuelve a guardar el carrito
        session.setAttribute("carrito", carrito);
        
        model.addAttribute("carrito", carrito);
        
        return "redirect:/pedido";
    }

    //private Usuario usuario;

    /*@ModelAttribute("carrito")
    public List<Producto> productosCarrito() {
        List<Long> contenido = (List<Long>) session.getAttribute("carrito");
        return (contenido == null) ? null : productoServicio.variosPorId(contenido);
    }*/

 /*@ModelAttribute("total_carrito")
    public Double totalCarrito() {
        List<Producto> productosCarrito = productosCarrito();

        if (productosCarrito != null) {
            return productosCarrito.stream()
                    .mapToDouble(p -> p.getPrecio())
                    .sum();
        }

        return 0.0;
    }*/

 /*@ModelAttribute("mis_compras")
    public List<Compra> misCompras() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        this.usuario = usuarioServicio.buscarPorEmail(email);

        return compraServicio.porPropietario(usuario);
    }*/
    @GetMapping("/carrito/eliminar/{id}")
    public String borrarDeCarrito(Model model, @PathVariable Long id) {
        List<Long> contenido = (List<Long>) session.getAttribute("carrito");

        // Si el carrito es nulo lo creamos
        if (contenido == null) {
            return "redirect:/public";
        }

        contenido.remove(id);

        if (contenido.isEmpty()) {
            session.removeAttribute("carrito");
        } else {
            session.setAttribute("carrito", contenido);
        }

        return "redirect:/app/carrito";
    }

    /*@GetMapping("/carrito/finalizar")
    public String checkout() {
        List<Long> contenido = (List<Long>) session.getAttribute("carrito");

        if (contenido == null) {
            return "redirect:/public";
        }

        List<Producto> productos = productosCarrito();

        Compra c = compraServicio.insertar(new Compra(), usuario);

        productos.forEach(p -> compraServicio.addProductoCompra(p, c));
        session.removeAttribute("carrito");

        return "redirect:/app/compra/factura/" + c.getId();
    }*/

 /*@GetMapping("/compra/factura/{id}")
    public String factura(Model model, @PathVariable Long id) {
        Compra c = compraServicio.buscarPorId(id);
        List<Producto> productos = productoServicio.productosDeUnaCompra(c);
        
        model.addAttribute("productos", productos);
        model.addAttribute("compra", c);
        model.addAttribute("total_compra", productos.stream().mapToDouble(p -> p.getPrecio()).sum());
        
        return "/app/compra/factura";
    }*/
 /*@GetMapping("/miscompras")
    public String verMisCompras(Model model) {
        return "/app/compra/listado";
    }*/
}
