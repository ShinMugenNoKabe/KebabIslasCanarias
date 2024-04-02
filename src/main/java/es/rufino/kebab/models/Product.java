package es.rufino.kebab.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Category category;

    private String name;
    private Boolean isAvailable;
    private String image;

    private BigDecimal price;
    private Double salePercentage = 0.0;

    public Product(
            String name,
            Double price,
            String image,
            Double salePercentage,
            Boolean isAvailable,
            Category category
    ) {
        this.name = name;
        this.price = BigDecimal.valueOf(price);
        this.image = image;
        this.salePercentage = salePercentage;
        this.isAvailable = isAvailable;
        this.category = category;
    }


    public BigDecimal calculateDiscountedPrice() {
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
