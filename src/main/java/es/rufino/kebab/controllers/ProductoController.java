package es.rufino.kebab.controllers;

import es.rufino.kebab.repositories.RoleRepository;
import es.rufino.kebab.services.CategoryService;
import es.rufino.kebab.services.ProductService;
import es.rufino.kebab.services.UserService;
import es.rufino.kebab.services.StorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Controller
@RequestMapping
public class ProductoController {

    @Autowired
    private ProductService productService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository rolRepositorio;
    
    @Autowired
    private UserController userController;

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
//        List<Category> categories;
//        categories = categoryService.findAll();
//
//        model.addAttribute("categories", categories);

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
//        if (!userController.isUserAdmin(principal)) {
//            return "index";
//        }
//
//        List<Product> products;
//        products = productService.findAll();
//
//        List<Category> categories;
//        categories = categoryService.findAll();
//
//        model.addAttribute("products", products);
//        model.addAttribute("categories", categories);

        return "products/products";
    }

    @GetMapping("/producto/nuevo")
    public String nuevoProductoForm(Model model, Principal principal) {
//        if (!userController.isUserAdmin(principal)) {
//            return "index";
//        }

//        List<Category> categories;
//        categories = categoryService.findAll();
//
//        model.addAttribute("categories", categories);
//        model.addAttribute("producto", new Product());
//
        return "productos/formulario-editar-nuevo";
    }

    @PostMapping("/producto/nuevo/submit")
    public String nuevoProductoSubida(
            //@Valid Product nuevoProduct,
            @RequestParam("id_categoria") Integer id_categoria,
            @RequestParam("file") MultipartFile file,
            BindingResult result,
            Model model,
            Principal principal) {
        
//        if (!userController.isUserAdmin(principal)) {
//            return "index";
//        }
//
//        if (nuevoProduct.getName().equals("")) {
//            nuevoProduct.setName("Nuevo producto");
//        }
//
//        // Comprobación del precio
//        if (nuevoProduct.getPrice() == null) {
//            //nuevoProduct.setPrecio(0.0);
//            result.rejectValue("precio", "error.producto",
//                    "Introduzca un precio válido.");
//        }
//
//        // Comprobaciones del porcentaje de oferta
//        if (nuevoProduct.getSalePercentage() == null) {
//            nuevoProduct.setSalePercentage(0.0);
//        }
//
//        if (nuevoProduct.getSalePercentage() < 0.0
//                || nuevoProduct.getSalePercentage() > 100.0) {
//            result.rejectValue("precio", "error.producto",
//                    "Introduzca un porcentaje de descuento válido.");
//        }
//
//        // Comprobación de categoría
//        if (id_categoria != null && !id_categoria.equals(0)) {
//            Category category = categoryService.findById(id_categoria);
//
//            if (category != null) {
//                nuevoProduct.setCategory(category);
//            } else {
//                result.rejectValue("category", "error.producto",
//                        "Introduzca una categoría válida.");
//            }
//        } else {
//            result.rejectValue("categoria", "error.producto",
//                    "Introduzca una categoría válida.");
//        }
//
//        if (result.hasErrors()) {
//            List<Category> categories;
//            categories = categoryService.findAll();
//            model.addAttribute("categories", categories);
//
//            return "productos/formulario-editar-nuevo";
//        }
//
//        nuevoProduct.setDiscountedPrice(nuevoProduct.calculaPrecioConDescuento());
//
//        if (!file.isEmpty()) {
//            String nombreImagen = storageService.store(file);
//            nuevoProduct.setImage(nombreImagen);
//        } else {
//            nuevoProduct.setImage("vacio.jpg");
//        }
//
//        productService.insertar(nuevoProduct);

        return "redirect:/gestion-productos";
    }

    @GetMapping("/producto/editar/{id}")
    public String editarProductoForm(
            @PathVariable Integer id,
            Model model,
            Principal principal) {
        
//        if (!userController.isUserAdmin(principal)) {
//            return "index";
//        }
//
//        Product productAEditar = productService.findById(id);
//
//        if (productAEditar != null) {
//            List<Category> categories;
//            categories = categoryService.findAll();
//            model.addAttribute("categories", categories);
//
//            model.addAttribute("producto", productAEditar);
//            model.addAttribute("id_categoria", productAEditar.getId());
//
//            return "productos/formulario-editar-nuevo";
//        } else {
//            return "redirect:/productos/nuevo";
//        }

        return "";
    }
    
    @PostMapping("/producto/editar/submit")
    public String editarProductoSubida(
            //@Valid @ModelAttribute("producto") Product productEditado,
            @RequestParam("id_categoria") Integer id_categoria,
            @RequestParam("file") MultipartFile file,
            BindingResult result,
            Model model,
            Principal principal) {
        
//        if (!userController.isUserAdmin(principal)) {
//            return "index";
//        }
//
//        if (productEditado.getName().equals("")) {
//            productEditado.setName("Nuevo producto");
//        }
//
//        // Comprobación del precio
//        if (productEditado.getPrice() == null) {
//            //nuevoProducto.setPrecio(0.0);
//            result.rejectValue("precio", "error.producto",
//                    "Introduzca un precio válido.");
//        }
//
//        // Comprobaciones del porcentaje de oferta
//        if (productEditado.getSalePercentage() == null) {
//            productEditado.setSalePercentage(0.0);
//        }
//
//        if (productEditado.getSalePercentage() < 0.0
//                || productEditado.getSalePercentage() > 100.0) {
//            result.rejectValue("precio", "error.producto",
//                    "Introduzca un porcentaje de descuento válido.");
//        }
//
//        // Comprobación de categoría
//        if (id_categoria != null && !id_categoria.equals(0)) {
//            Category category = categoryService.findById(id_categoria);
//
//            if (category != null) {
//                productEditado.setCategory(category);
//            } else {
//                result.rejectValue("category", "error.producto",
//                        "Introduzca una categoría válida.");
//            }
//        } else {
//            result.rejectValue("categoria", "error.producto",
//                    "Introduzca una categoría válida.");
//        }
//
//        if (result.hasErrors()) {
//            List<Category> categories;
//            categories = categoryService.findAll();
//            model.addAttribute("categories", categories);
//
//            return "productos/formulario-editar-nuevo";
//        }
//
//        productEditado.setDiscountedPrice(productEditado.calculaPrecioConDescuento());
//
//        if (!file.isEmpty()) {
//            String nombreImagen = storageService.store(file);
//
//            productEditado.setImage(nombreImagen);
//        } else {
//            Product p = productService.findById(productEditado.getId());
//
//            productEditado.setImage(p.getImage());
//        }
//
//        productService.update(productEditado);

        return "redirect:/gestion-productos";
    }
    
    

    /*@GetMapping("/producto/nuevo")
    public String nuevoProductoForm(Model model) {
        model.addAttribute("producto", new Product());

        return "app/producto/form";
    }*/

 /*@PostMapping("/producto/nuevo/submit")
    public String nuevoProductoSubmit(@ModelAttribute Product producto,
            @RequestParam("file") MultipartFile file) {

        if (!file.isEmpty()) {
            String imagen = storageService.store(file);
            producto.setImagen(MvcUriComponentsBuilder
                .fromMethodName(FileController.class, "serveFile", imagen).build().toUriString());
        }

        producto.setPropietario(usuario);
        productService.insertar(producto);

        return "redirect:/app/misproductos";
    }*/
}
