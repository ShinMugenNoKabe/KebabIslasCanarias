package es.rufino.kebab.controllers;

import es.rufino.kebab.controllers.mappers.ProductMapper;
import es.rufino.kebab.models.Product;
import es.rufino.kebab.services.CategoryService;
import es.rufino.kebab.services.ProductService;
import es.rufino.kebab.services.StorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    private final CategoryService categoryService;
    private final StorageService storageService;

    @GetMapping
    public ResponseEntity<ProductsResponseDtoWrapper> findAll(ProductsRequestDto productsRequest) {
        List<Product> products = productService.findAll(productsRequest);

        List<ProductResponseDto> productsResponse = products.stream()
                .map(ProductMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(new ProductsResponseDtoWrapper(productsResponse));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    //@SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ProductResponseDto> insert(ProductRequestDto productRequest) {
        String newImageFilename = null;

        if (productRequest.image() != null) {
            newImageFilename = storageService.store(productRequest.image());
        }

        Product newProduct = ProductMapper.toProduct(productRequest, newImageFilename);
        newProduct.setCategory(categoryService.findById(productRequest.categoryId()));

        newProduct = productService.insert(newProduct);

        return new ResponseEntity<>(ProductMapper.toResponseDto(newProduct), HttpStatus.CREATED);
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

    public record ProductRequestDto(
            String name,
            BigDecimal price,
            BigDecimal discountedPrice,
            Double salePercentage,
            Boolean isAvailable,
            Long categoryId,
            @RequestPart(value = "image") MultipartFile image
    ) {
    }

    public record CategoryResponseDto(
            Long id,
            String name
    ) {
    }

    public record ProductResponseDto(
            Long id,
            String name,
            String image,
            BigDecimal price,
            BigDecimal discountedPrice,
            Double salePercentage,
            CategoryResponseDto category
    ) {
    }

}
