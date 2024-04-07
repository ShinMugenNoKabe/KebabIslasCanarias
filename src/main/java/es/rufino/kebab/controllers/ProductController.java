package es.rufino.kebab.controllers;

import es.rufino.kebab.controllers.mappers.ProductMapper;
import es.rufino.kebab.models.Product;
import es.rufino.kebab.services.CategoryService;
import es.rufino.kebab.services.ProductService;
import es.rufino.kebab.services.StorageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Products")
public class ProductController {

    private final ProductService productService;
    private final CategoryService categoryService;
    private final StorageService storageService;

    @Operation(
            description = "Gets a list of products based on the introduced filters.",
            summary = "Get products",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200")
            }
    )
    @GetMapping
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ProductsResponseDtoWrapper> findAll(ProductsRequestDto productsRequest) {
        List<Product> products = productService.findAll(productsRequest);

        List<ProductResponseDto> productsResponse = products.stream()
                .map(ProductMapper::toResponseDto)
                .toList();

        return ResponseEntity.ok(new ProductsResponseDtoWrapper(productsResponse));
    }

    @Operation(
            description = "Inserts a new product in the system, with an image if needed. Only administrators can upload images.",
            summary = "Insert product",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Forbidden", responseCode = "403")
            }
    )
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<ProductResponseDto> insertProduct(
            @RequestPart(value = "name") String name,
            @RequestPart(value = "price") BigDecimal price,
            @RequestPart(value = "discountedPrice") BigDecimal discountedPrice,
            @RequestPart(value = "salePercentage") Double salePercentage,
            @RequestPart(value = "isAvailable") Boolean isAvailable,
            @RequestPart(value = "categoryId") Long categoryId,
            @RequestPart(value = "image") MultipartFile image
    ) {
        ProductRequestDto productRequest = new ProductRequestDto(
                name, price, discountedPrice, salePercentage, isAvailable, categoryId, image
        );

        String newImageFilename = null;

        if (productRequest.image() != null) {
            newImageFilename = storageService.store(productRequest.image());
        }

        Product newProduct = ProductMapper.toProduct(productRequest, newImageFilename);
        newProduct.setCategory(categoryService.findById(productRequest.categoryId()));

        newProduct = productService.insert(newProduct);

        return new ResponseEntity<>(ProductMapper.toResponseDto(newProduct), HttpStatus.CREATED);
    }

    @Operation(
            description = "Deletes one product based on its Id.",
            summary = "Delete product",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200"),
                    @ApiResponse(description = "Product not found", responseCode = "404")
            }
    )
    @DeleteMapping(value="/{id}")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteById(id);
        return ResponseEntity.noContent().build();
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
            @RequestPart(value = "name") String name,
            @RequestPart(value = "price") BigDecimal price,
            @RequestPart(value = "discountedPrice") BigDecimal discountedPrice,
            @RequestPart(value = "salePercentage") Double salePercentage,
            @RequestPart(value = "isAvailable") Boolean isAvailable,
            @RequestPart(value = "categoryId") Long categoryId,
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
