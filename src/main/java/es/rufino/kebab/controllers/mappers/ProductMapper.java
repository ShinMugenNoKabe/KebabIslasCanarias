package es.rufino.kebab.controllers.mappers;

import es.rufino.kebab.controllers.ProductController;
import es.rufino.kebab.models.Product;

public class ProductMapper {

    public static ProductController.ProductResponseDto toResponseDto(Product product) {
        return new ProductController.ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getImage(),
                product.getPrice(),
                product.calculateDiscountedPrice(),
                product.getSalePercentage(),
                new ProductController.CategoryResponseDto(product.getCategory().getId(), product.getCategory().getName())
        );
    }

    public static Product toProduct(ProductController.ProductRequestDto productRequest, String imageFilename) {
        return Product.builder()
                .name(productRequest.name())
                .price(productRequest.price())
                .salePercentage(productRequest.salePercentage())
                .isAvailable(productRequest.isAvailable())
                .image(imageFilename)
                .build();
    }

}
