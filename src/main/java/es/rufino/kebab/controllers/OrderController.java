package es.rufino.kebab.controllers;

import es.rufino.kebab.models.Order;
import es.rufino.kebab.services.OrderService;
import es.rufino.kebab.services.ProductService;
import es.rufino.kebab.services.UserService;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

/**
 *
 * @author Rufino Serrano Cañas
 */
@RestController
@RequestMapping("/api/v1/orders")
public class OrderController {

    // TODO: Ordenar
    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final UserController userController;

    public OrderController(
            ProductService productService,
            UserService userService,
            OrderService orderService,
            UserController userController
    ) {
        this.productService = productService;
        this.userService = userService;
        this.orderService = orderService;
        this.userController = userController;
    }

    // TODO: Cambiar
    @PostMapping("/realizar-pedido")
    public String realizarPedido(Model model,
            Order nuevoOrder,
            BindingResult result,
            Principal principal) {

//        HashMap<Product, Integer> carrito = orderService.getCarrito();
//
//        if (nuevoOrder.getAddress() != null) {
//            if (nuevoOrder.getPickupAtStore() == true && nuevoOrder.getAddress().length() > 0) {
//                result.rejectValue("recogidaEnTienda", "error.pedido",
//                        "Marque esta opción si va a recoger el producto en la tienda.");
//            }
//
//            if (nuevoOrder.getPickupAtStore() == false && nuevoOrder.getAddress().length() == 0) {
//                result.rejectValue("direccionDeEnvio", "error.pedido",
//                        "Indique una dirección de envío.");
//            }
//        }
//
//        if (nuevoOrder.getPickupAtStore() == true) {
//            nuevoOrder.setAddress(null);
//        }
//
//        if (result.hasErrors()) {
//            model.addAttribute("precioTotal", calculoPrecioTotalCarrito(carrito));
//            model.addAttribute("carrito", carrito);
//
//            return "pedidos/pedido";
//        }
//
//        List<OrderLine> lineasDePedido = new LinkedList<>();
//
//        OrderLine lp;
//
//        for (Product p : carrito.keySet()) {
//            lp = new OrderLine(carrito.get(p), (p.getDiscountedPrice() * carrito.get(p)));
//            lp.setOrder(nuevoOrder);
//            lp.setProduct(p);
//            lineasDePedido.add(lp);
//        }
//
//        nuevoOrder.setOrderLines(lineasDePedido);
//
//        User userLoggeado = userService.findCurrentUser(principal);
//
//        nuevoOrder.setUser(userLoggeado);
//        orderService.insertar(nuevoOrder);
//
//        carrito.clear();

        return "redirect:/mis-pedidos";
    }

    // TODO: Cambiar
    @GetMapping("/mis-pedidos")
    public String misPedidos(Model model,
            Principal principal,
            @RequestParam(name = "fecha_min", required = false) String fecha_min,
            @RequestParam(name = "fecha_max", required = false) String fecha_max) {
//        User userLoggeado = userService.findCurrentUser(principal);
//
//        List<Order> pedidosEncontrados;
//
//        // Si se ha introducido fecha
//        if (fecha_min != null && fecha_max != null) {
//            try {
//                Date fechaMin = null, fechaMax = null;
//
//                fechaMin = new SimpleDateFormat("yyyy-MM-dd").parse(fecha_min);
//                fechaMax = new SimpleDateFormat("yyyy-MM-dd").parse(fecha_max);
//
//                pedidosEncontrados = orderService.buscarEntreDosFechas(userLoggeado, fechaMin, fechaMax);
//            } catch (ParseException ex) {
//                pedidosEncontrados = userLoggeado.getOrders();
//            }
//        } else {
//            pedidosEncontrados = userLoggeado.getOrders();
//        }
//
//        model.addAttribute("pedidos", pedidosEncontrados);

        return "pedidos/lista-pedidos";
    }

    // TODO: Cambiar
    @GetMapping("/gestion-pedidos")
    public String todosLosPedidos(Model model,
            Principal principal,
            @RequestParam(name = "fecha_min", required = false) String fecha_min,
            @RequestParam(name = "fecha_max", required = false) String fecha_max,
            @RequestParam(name = "email", required = false) String email) {

//        if (!userController.isUserAdmin(principal)) {
//            return "index";
//        }
//
//        User userBuscado = userService.findByEmail(email);
//
//        List<Order> pedidosEncontrados = orderService.buscarTodos();
//
//        // Si se ha introducido fecha
//        if (fecha_min != null && fecha_max != null) {
//            if (!fecha_min.equals("") && !fecha_max.equals("")) {
//                try {
//                    Date fechaMin = null, fechaMax = null;
//
//                    fechaMin = new SimpleDateFormat("yyyy-MM-dd").parse(fecha_min);
//                    fechaMax = new SimpleDateFormat("yyyy-MM-dd").parse(fecha_max);
//
//                    pedidosEncontrados = orderService.buscarEntreDosFechas(userBuscado, fechaMin, fechaMax);
//                } catch (ParseException ex) {
//                    pedidosEncontrados = orderService.buscarTodos();
//                }
//            } else {
//                if (userBuscado != null) {
//                    pedidosEncontrados = userBuscado.getOrders();
//                }
//            }
//        } else {
//            if (userBuscado != null) {
//                pedidosEncontrados = userBuscado.getOrders();
//            }
//        }
//
//        model.addAttribute("pedidos", pedidosEncontrados);

        return "pedidos/lista-pedidos";
    }

}
