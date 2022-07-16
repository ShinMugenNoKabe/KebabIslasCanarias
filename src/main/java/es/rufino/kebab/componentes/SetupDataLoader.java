/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.componentes;

import es.rufino.kebab.modelo.Categoria;
import es.rufino.kebab.modelo.Privilegio;
import es.rufino.kebab.modelo.Producto;
import es.rufino.kebab.modelo.Rol;
import es.rufino.kebab.modelo.Usuario;
import es.rufino.kebab.repositories.PrivilegioRepository;
import es.rufino.kebab.repositories.RolRepository;
import es.rufino.kebab.servicios.CategoriaServicio;
import es.rufino.kebab.servicios.PedidoServicio;
import es.rufino.kebab.servicios.ProductoServicio;
import es.rufino.kebab.servicios.UsuarioServicio;
import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * Clase que carga datos de ejemplo al iniciar la aplicación, como roles, el
 * usuario administrador, productos, categorías, etc.
 *
 * @author Rufino Serrano Cañas
 */
@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    private Boolean alreadySetup = false;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private RolRepository rolRepositorio;

    @Autowired
    private CategoriaServicio categoriaServicio;

    @Autowired
    private ProductoServicio productoServicio;

    @Autowired
    private PrivilegioRepository privilegioRepositorio;

    @Autowired
    private PedidoServicio pedidoServicio;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        Privilegio privilegioLectura
                = createPrivilegeIfNotFound("READ_PRIVILEGE");
        Privilegio privilegioEscritura
                = createPrivilegeIfNotFound("WRITE_PRIVILEGE");

        List<Privilegio> privilegiosDeAdmin = Arrays.asList(
                privilegioLectura, privilegioEscritura);
        createRoleIfNotFound("ROLE_ADMIN", privilegiosDeAdmin);
        createRoleIfNotFound("ROLE_USER", Arrays.asList(privilegioLectura));

        Rol rolDeAdmin = rolRepositorio.findByNombre("ROLE_ADMIN");
        Usuario usuarioAdministrador = new Usuario("admin@gmail.com", "1234",
                "Administrador", "De Kebab Islas Canarias");
        usuarioAdministrador.setListaDeRoles(Arrays.asList(rolDeAdmin));
        usuarioServicio.registrar(usuarioAdministrador);

        /*Usuario usuario1 = new Usuario("rufisercay@gmail.com", "1234",
                "Rufino", "Serrano Cañas");
        usuario1.setListaDeRoles(Arrays.asList(rolRepositorio.findByNombre("ROLE_USER")));
        usuarioServicio.registrar(usuario1);*/
        
        // Insertado de categorías
        Categoria doners = new Categoria("Doners");
        Categoria durums = new Categoria("Durums");
        Categoria lahmacuns = new Categoria("Lahmacuns");
        Categoria hamburguesas = new Categoria("Hamburguesas");
        Categoria platosCombinados = new Categoria("Platos combinados");
        Categoria raciones = new Categoria("Raciones");
        Categoria ensaladas = new Categoria("Ensaladas");
        Categoria bebidas = new Categoria("Bebidas");
        
        List<Categoria> categorias = Arrays.asList(
                doners, durums, lahmacuns, hamburguesas, platosCombinados,
                raciones, ensaladas, bebidas
        );

        categorias.forEach(categoriaServicio::insertar);

        // Insertado de productos
        List<Producto> productos = Arrays.asList(
                new Producto("Doner Kebab", 3.5, "donerkebab.jpg", 0.0, true, doners),
                new Producto("Doner Kebab con queso", 4.0, "donerkebabqueso.jpg", 20.0, true, doners),
                new Producto("Doner Kebab con maíz", 4.0, "donerkebabmaiz.jpg", 0.0, true, doners),
                new Producto("Doner Kebab falafel", 3.5, "donerkebabfalafel.jpg", 50.0, true, doners),
                new Producto("Durum Kebab", 4.5, "durumkebab.jpg", 0.0, true, durums),
                new Producto("Durum Kebab con queso", 5.0, "durumkebabqueso.jpg", 15.0, false, durums),
                new Producto("Durum Kebab con maíz", 5.0, "durumkebabmaiz.jpg", 0.0, true, durums),
                new Producto("Durum Kebab doble", 5.5, "durumkebabdoble.jpg", 10.0, true, durums),
                new Producto("Durum sólo carne", 5.5, "durumcarne.jpg", 0.0, true, durums),
                new Producto("Durum Kebab falafel", 4.0, "durumkebabfalafel.jpg", 0.0, true, durums),
                new Producto("Pizza turca", 5.0, "pizzaturca.jpg", 0.0, true, lahmacuns),
                new Producto("Pizza turca con queso", 5.5, "pizzaturcaqueso.jpg", 0.0, true, lahmacuns),
                new Producto("Pizza turca con maíz", 5.5, "pizzaturcamaiz.jpg", 0.0, false, lahmacuns),
                new Producto("Pizza turca doble", 6.0, "pizzaturcadoble.jpg", 35.0, true, lahmacuns),
                new Producto("Pizza turca sólo carne", 6.0, "pizzaturcacarne.jpg", 0.0, false, lahmacuns),
                new Producto("Hamburguesa de ternera", 3.5, "hamburguesaternera.jpg", 0.0, true, hamburguesas),
                new Producto("Hamburguesa de pollo", 3.5, "hamburguesapollo.jpg", 0.0, true, hamburguesas),
                new Producto("Hamburguesa para niños", 2.5, "hamburguesaninos.jpg", 0.0, true, hamburguesas),
                new Producto("Plato Doner Kebab", 6.0, "platodonerkebab.jpg", 70.0, true, platosCombinados),
                new Producto("Plato Doner Kebab con arroz", 6.0, "platodonerkebabarroz.jpg", 0.0, false, platosCombinados),
                new Producto("Plato Doner Kebab de la casa", 6.5, "platodonerkebabcasa.jpg", 25.0, true, platosCombinados),
                new Producto("Patatas fritas", 1.5, "patatasfritas.jpg", 0.0, true, raciones),
                new Producto("Nuggets de pollo (6 unds.)", 3.5, "nuggets.jpg", 0.0, true, raciones),
                new Producto("Nuggets de pollo (9 unds.)", 6.0, "nuggets.jpg", 20.0, true, raciones),
                new Producto("Aros de cebolla (6 unds.)", 3.5, "aroscebolla.jpg", 0.0, false, raciones),
                new Producto("Calamares con patatas (6 unds.)", 4.5, "calamarespatatas.jpg", 0.0, true, raciones),
                new Producto("Alitas de pollo (6 unds.)", 3.5, "alitas.jpg", 0.0, true, raciones),
                new Producto("Alitas de pollo (9 unds.)", 6.0, "alitas.jpg", 0.0, true, raciones),
                new Producto("Pedrata", 3.0, "pedrata.jpg", 0.0, false, raciones),
                new Producto("Falafel (6 unds.)", 3.5, "falafel.jpg", 0.0, true, raciones),
                new Producto("Samosa (1 unds.)", 1.0, "samosa.jpg", 0.0, true, raciones),
                new Producto("Ensalada verde", 3.0, "ensaladaverde.jpg", 0.0, true, ensaladas),
                new Producto("Ensalada turca", 4.0, "ensaladaturca.jpg", 20.0, true, ensaladas),
                new Producto("Ensalada césar", 4.5, "ensaladacesar.jpg", 0.0, true, ensaladas),
                new Producto("Coca-Cola", 1.0, "cocacola.jpg", 0.0, true, bebidas),
                new Producto("Pepsi", 1.0, "pepsi.jpg", 0.0, true, bebidas),
                new Producto("Agua", 1.0, "agua.jpg", 0.0, true, bebidas),
                new Producto("Fanta naranja", 1.0, "fantanaranja.jpg", 0.0, true, bebidas),
                new Producto("Fanta limón", 1.0, "fantalimon.jpg", 0.0, true, bebidas),
                new Producto("Nestea", 1.0, "nestea.jpg", 0.0, true, bebidas),
                new Producto("Aquarius", 1.0, "aquarius.jpg", 0.0, true, bebidas));

        productos.forEach(productoServicio::insertar);

        /*List<LineaPedido> lineasPedido = Arrays.asList(
                new LineaPedido(productos.get(0), 4),
                new LineaPedido(productos.get(1), 5));
        
        Pedido pedido = new Pedido();
        pedido.setCodpedido(1);
        pedido.setUsuario(usuario1);
        pedido.setRecogidaEnTienda(true);
        pedido.setListaDeLineasDePedido(lineasPedido);
                
        try {
            pedido.setFecha(new SimpleDateFormat("yyyy/MM/dd").parse("2022-03-01"));
        } catch (Exception ex) {
            
        }
        
        pedidoServicio.insertar(pedido);*/
        alreadySetup = true;
    }

    @Transactional
    public Privilegio createPrivilegeIfNotFound(String nombre) {

        Privilegio privilegio = privilegioRepositorio.findByNombre(nombre);

        if (privilegio == null) {
            privilegio = new Privilegio(nombre);
            privilegioRepositorio.save(privilegio);
        }

        return privilegio;
    }

    @Transactional
    public Rol createRoleIfNotFound(
            String nombre, List<Privilegio> listaDePrivilegios) {

        Rol rol = rolRepositorio.findByNombre(nombre);
        if (rol == null) {
            rol = new Rol(nombre);
            rol.setListaDePrivilegios(listaDePrivilegios);
            rolRepositorio.save(rol);
        }

        return rol;
    }
}
