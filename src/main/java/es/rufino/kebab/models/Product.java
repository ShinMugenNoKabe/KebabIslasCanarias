package es.rufino.kebab.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Entity
@Data
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @ManyToOne
    private Category category;

    private String name;
    private Boolean available;
    private String image;

    private BigDecimal price;
    private Double salePercentage;
    private BigDecimal discountedPrice;

    public Product(
            String name,
            Double price,
            String image,
            Double salePercentage,
            Boolean available,
            Category category
    ) {
        this(name, BigDecimal.valueOf(price), image, salePercentage, available, category);
    }

    public Product(
            String name,
            BigDecimal price,
            String image,
            Double salePercentage,
            Boolean available,
            Category category
    ) {
        this.name = name;
        this.price = price;
        this.image = image;
        this.salePercentage = salePercentage;
        this.discountedPrice = calculateDiscountedPrice();
        this.available = available;
        this.category = category;
    }

    private BigDecimal calculateDiscountedPrice() {
        if (priceIsNotDiscounted()) {
            return getPrice();
        }

        BigDecimal calculatedDiscount = getPrice().multiply(BigDecimal.valueOf(getSalePercentage() / 100));
        BigDecimal discountedPrice = getPrice().subtract(calculatedDiscount);

        // We want to show the client the price rounded to 2 decimals (euros have 2 decimals)
        return discountedPrice.setScale(2, RoundingMode.UP);
    }

    private boolean priceIsNotDiscounted() {
        return getSalePercentage().equals(0.0);
    }

    public boolean hasImageAttached() {
        return !getImage().isEmpty();
    }

}
