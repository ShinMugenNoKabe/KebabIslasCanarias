package es.rufino.kebab.controllers;

import es.rufino.kebab.controllers.mappers.ProductMapper;
import es.rufino.kebab.models.Product;
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
    public ResponseEntity<ProductsResponseDtoWrapper> findAll(ProductsRequestDto productRequest) {
        List<Product> products = productService.findAll(productRequest);

        List<ProductResponseDto> productsResponse = products.stream()
                .map(ProductMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(new ProductsResponseDtoWrapper(productsResponse));
    }

    public record ProductsRequestDto(
            String name,
            Integer category_id,
            Sort.Direction sortOrder,
            BigDecimal minimumPrice,
            BigDecimal maximumPrice,
            Boolean isOnSale
    ) {
    }

    public record ProductsResponseDtoWrapper(List<ProductResponseDto> products) {
    }

    public record ProductResponseDto(
            Long id,
            String name,
            String image,
            BigDecimal price,
            BigDecimal discountedPrice,
            Double salePercentage
    ) {
    }

}
