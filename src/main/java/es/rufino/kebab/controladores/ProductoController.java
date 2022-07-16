/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.controladores;

import es.rufino.kebab.modelo.Categoria;
import es.rufino.kebab.modelo.Producto;
import es.rufino.kebab.modelo.Rol;
import es.rufino.kebab.modelo.Usuario;
import es.rufino.kebab.repositories.RolRepository;
import es.rufino.kebab.servicios.CategoriaServicio;
import es.rufino.kebab.servicios.ProductoServicio;
import es.rufino.kebab.servicios.UsuarioServicio;
import es.rufino.kebab.upload.StorageService;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Controller
@RequestMapping("/")
public class ProductoController {

    @Autowired
    private HttpSession session;

    @Autowired
    private ProductoServicio productoServicio;

    @Autowired
    private CategoriaServicio categoriaServicio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private RolRepository rolRepositorio;
    
    @Autowired
    private UsuarioController usuarioController;

    // Servicio de almacenamiento
    @Autowired
    private StorageService storageService;

    /**
     * Lleva a la vista principal
     *
     * @param model Clase de Spring Boot para añadir atributos
     * @return Vista principal
     */
    @GetMapping({""})
    public String index(Model model) {
        List<Categoria> categorias;
        categorias = categoriaServicio.buscarTodos();

        model.addAttribute("categorias", categorias);

        return "index";
    }

    /**
     * Lleva a la vista de gestión de productos para administradores. Si el
     * usuario loggeado (o anónimo) no es administrador se le redirecciona a la
     * vista principal
     *
     * @param model Clase de Spring Boot para añadir atributos
     * @param principal Clase de Spring boot que contiene almacenado el usuario
     * loggeado
     * @return Vista de gestión de productos para administradores
     */
    @GetMapping("/gestion-productos")
    public String gestionProductos(Model model, Principal principal) {
        if (!usuarioController.comprobarAdmin(principal)) {
            return "index";
        }

        List<Producto> productos;
        productos = productoServicio.findAll();

        List<Categoria> categorias;
        categorias = categoriaServicio.buscarTodos();

        model.addAttribute("productos", productos);
        model.addAttribute("categorias", categorias);

        return "productos/productos";
    }

    @GetMapping("/producto/nuevo")
    public String nuevoProductoForm(Model model, Principal principal) {
        if (!usuarioController.comprobarAdmin(principal)) {
            return "index";
        }

        List<Categoria> categorias;
        categorias = categoriaServicio.buscarTodos();

        model.addAttribute("categorias", categorias);
        model.addAttribute("producto", new Producto());

        return "productos/formulario-editar-nuevo";
    }

    @PostMapping("/producto/nuevo/submit")
    public String nuevoProductoSubida(
            @Valid Producto nuevoProducto,
            @RequestParam("id_categoria") Integer id_categoria,
            @RequestParam("file") MultipartFile file,
            BindingResult result,
            Model model,
            Principal principal) {
        
        if (!usuarioController.comprobarAdmin(principal)) {
            return "index";
        }

        if (nuevoProducto.getNombre().equals("")) {
            nuevoProducto.setNombre("Nuevo producto");
        }

        // Comprobación del precio
        if (nuevoProducto.getPrecio() == null) {
            //nuevoProducto.setPrecio(0.0);
            result.rejectValue("precio", "error.producto",
                    "Introduzca un precio válido.");
        }

        // Comprobaciones del porcentaje de oferta
        if (nuevoProducto.getPorcentajeDeOferta() == null) {
            nuevoProducto.setPorcentajeDeOferta(0.0);
        }

        if (nuevoProducto.getPorcentajeDeOferta() < 0.0
                || nuevoProducto.getPorcentajeDeOferta() > 100.0) {
            result.rejectValue("precio", "error.producto",
                    "Introduzca un porcentaje de descuento válido.");
        }

        // Comprobación de categoría
        if (id_categoria != null && !id_categoria.equals(0)) {
            Categoria categoria = categoriaServicio.buscarPorId(id_categoria);

            if (categoria != null) {
                nuevoProducto.setCategoria(categoria);
            } else {
                result.rejectValue("categoria", "error.producto",
                        "Introduzca una categoría válida.");
            }
        } else {
            result.rejectValue("categoria", "error.producto",
                    "Introduzca una categoría válida.");
        }

        if (result.hasErrors()) {
            List<Categoria> categorias;
            categorias = categoriaServicio.buscarTodos();
            model.addAttribute("categorias", categorias);

            return "productos/formulario-editar-nuevo";
        }

        nuevoProducto.setPrecioConDescuento(nuevoProducto.calculaPrecioConDescuento());

        if (!file.isEmpty()) {
            String nombreImagen = storageService.store(file);
            nuevoProducto.setImagen(nombreImagen);
        } else {
            nuevoProducto.setImagen("vacio.jpg");
        }

        productoServicio.insertar(nuevoProducto);

        return "redirect:/gestion-productos";
    }

    @GetMapping("/producto/editar/{id}")
    public String editarProductoForm(
            @PathVariable Integer id,
            Model model,
            Principal principal) {
        
        if (!usuarioController.comprobarAdmin(principal)) {
            return "index";
        }
        
        Producto productoAEditar = productoServicio.findById(id);

        if (productoAEditar != null) {
            List<Categoria> categorias;
            categorias = categoriaServicio.buscarTodos();
            model.addAttribute("categorias", categorias);
            
            model.addAttribute("producto", productoAEditar);
            model.addAttribute("id_categoria", productoAEditar.getCodprod());

            return "productos/formulario-editar-nuevo";
        } else {
            return "redirect:/productos/nuevo";
        }
    }
    
    @PostMapping("/producto/editar/submit")
    public String editarProductoSubida(
            @Valid @ModelAttribute("producto") Producto productoEditado,
            @RequestParam("id_categoria") Integer id_categoria,
            @RequestParam("file") MultipartFile file,
            BindingResult result,
            Model model,
            Principal principal) {
        
        if (!usuarioController.comprobarAdmin(principal)) {
            return "index";
        }

        if (productoEditado.getNombre().equals("")) {
            productoEditado.setNombre("Nuevo producto");
        }

        // Comprobación del precio
        if (productoEditado.getPrecio() == null) {
            //nuevoProducto.setPrecio(0.0);
            result.rejectValue("precio", "error.producto",
                    "Introduzca un precio válido.");
        }

        // Comprobaciones del porcentaje de oferta
        if (productoEditado.getPorcentajeDeOferta() == null) {
            productoEditado.setPorcentajeDeOferta(0.0);
        }

        if (productoEditado.getPorcentajeDeOferta() < 0.0
                || productoEditado.getPorcentajeDeOferta() > 100.0) {
            result.rejectValue("precio", "error.producto",
                    "Introduzca un porcentaje de descuento válido.");
        }

        // Comprobación de categoría
        if (id_categoria != null && !id_categoria.equals(0)) {
            Categoria categoria = categoriaServicio.buscarPorId(id_categoria);

            if (categoria != null) {
                productoEditado.setCategoria(categoria);
            } else {
                result.rejectValue("categoria", "error.producto",
                        "Introduzca una categoría válida.");
            }
        } else {
            result.rejectValue("categoria", "error.producto",
                    "Introduzca una categoría válida.");
        }

        if (result.hasErrors()) {
            List<Categoria> categorias;
            categorias = categoriaServicio.buscarTodos();
            model.addAttribute("categorias", categorias);

            return "productos/formulario-editar-nuevo";
        }

        productoEditado.setPrecioConDescuento(productoEditado.calculaPrecioConDescuento());

        if (!file.isEmpty()) {
            String nombreImagen = storageService.store(file);
            
            productoEditado.setImagen(nombreImagen);
        } else {
            Producto p = productoServicio.findById(productoEditado.getCodprod());
            
            productoEditado.setImagen(p.getImagen());
        }

        productoServicio.editar(productoEditado);

        return "redirect:/gestion-productos";
    }
    
    

    /*@GetMapping("/producto/nuevo")
    public String nuevoProductoForm(Model model) {
        model.addAttribute("producto", new Producto());

        return "app/producto/form";
    }*/

 /*@PostMapping("/producto/nuevo/submit")
    public String nuevoProductoSubmit(@ModelAttribute Producto producto,
            @RequestParam("file") MultipartFile file) {

        if (!file.isEmpty()) {
            String imagen = storageService.store(file);
            producto.setImagen(MvcUriComponentsBuilder
                .fromMethodName(FilesController.class, "serveFile", imagen).build().toUriString());
        }

        producto.setPropietario(usuario);
        productoServicio.insertar(producto);

        return "redirect:/app/misproductos";
    }*/
}
