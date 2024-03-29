package es.rufino.kebab.models;

import lombok.*;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.math.BigDecimal;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Entity
@Data
@ToString
@Builder
@Table(name = "order_lines")
public class OrderLine {
    
    @ManyToOne
    @Id
    private Order order;
    
    @ManyToOne
    @Id
    private Product product;

    private Integer quantity;
    
    private BigDecimal totalPrice;

    public OrderLine(Product product, Integer quantity) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = BigDecimal.valueOf(product.getSalePercentage() * quantity);
    }

    public OrderLine(Integer quantity, BigDecimal totalPrice) {
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }
    
}