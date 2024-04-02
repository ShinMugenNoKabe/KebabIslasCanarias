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
                product.getSalePercentage()
        );
    }

}
