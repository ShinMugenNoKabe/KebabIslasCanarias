package es.rufino.kebab.controllers;

import es.rufino.kebab.models.Product;
import es.rufino.kebab.services.CategoryService;
import es.rufino.kebab.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * @author ShinMugenNoKabe
 */
@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;

    @GetMapping
    public ResponseEntity<ProductsResponse> findAll(ProductFilter productFilter) {

        List<Product> productosEncontrados = new ArrayList<>();
        //productService.buscarPorNombreAdministrador(productFilter.name());

        System.out.println(productFilter);

//        // Si se ha introducido categoría
//        if (category_id != null && !category_id.equals(0)) {
//            Category categoria = categoryService.buscarPorId(category_id);
//
//            if (administrador != null) {
//                productosEncontrados = productService.buscarPorNombreEIdCategoriaAdministrador(name, categoria);
//            } else {
//                productosEncontrados = productService.buscarPorNombreEIdCategoria(name, categoria);
//            }
//        } else {
//            if (administrador != null) {
//                productosEncontrados = productService.buscarPorNombreAdministrador(name);
//            } else {
//                productosEncontrados = productService.buscarPorNombre(name);
//            }
//        }
//
//        // Si se ha introducido un orden
//        if (orden != null && !orden.equals("0")) {
//            Comparator comparadorPorPrecio = new ComparadorPorPrecio();
//
//            if (orden.equals("ascendente")) {
//                // Ordena la lista de forma ascendente
//                productosEncontrados.sort(comparadorPorPrecio);
//            } else if (orden.equals("descendente")) {
//                // Ordena la lista de forma ascendente
//                productosEncontrados.sort(comparadorPorPrecio.reversed());
//            }
//        }
//
//        // Filtrado de precio entre precio mínimo y máximo
//        if (precio_min != null && precio_max != null) {
//            productosEncontrados.removeIf((p) -> p.getPrecioConDescuento() < precio_min || p.getPrecioConDescuento() > precio_max);
//        }
//
//        // Filtrado únicamente de productos en oferta
//        if (en_oferta) {
//            productosEncontrados.removeIf((p) -> p.getPorcentajeDeOferta().equals(0.0));
//        }

        return ResponseEntity.ok(new ProductsResponse(productosEncontrados));
    }

    public record ProductsResponse(List<Product> products) {
    }

    public record ProductFilter(
            String name,
            Integer category_id,
            SortOrder sortOrder,
            BigDecimal minimumPrice,
            BigDecimal maximumPrice,
            Boolean isOnSale
    ) {
    }

    private enum SortOrder {
        ASC,
        DESC
    }

}
