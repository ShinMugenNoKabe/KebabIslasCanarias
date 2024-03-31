package es.rufino.kebab.controllers;

import es.rufino.kebab.models.Product;
import es.rufino.kebab.services.CategoryService;
import es.rufino.kebab.services.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author ShinMugenNoKabe
 */
@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<ProductsResponseWrapper> findAll(ProductsRequest productRequest) {
        List<Product> products = productService.findAll(productRequest);

        List<ProductResponse> productsResponse = products.stream()
                .map(Product::convertToProductResponse)
                .toList();

        return ResponseEntity.ok(new ProductsResponseWrapper(productsResponse));
    }

    public record ProductsRequest(
            String name,
            Integer category_id,
            Sort.Direction sortOrder,
            BigDecimal minimumPrice,
            BigDecimal maximumPrice,
            Boolean isOnSale
    ) {
    }

    public record ProductsResponseWrapper(List<ProductResponse> products) {
    }

    public record ProductResponse(
            Long id,
            String name,
            String image,
            BigDecimal price,
            BigDecimal discountedPrice,
            Double salePercentage
    ) {
    }

}
