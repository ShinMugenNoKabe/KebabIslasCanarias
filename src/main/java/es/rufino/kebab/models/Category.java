package es.rufino.kebab.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import jakarta.persistence.*;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Entity
@Data
@Table(name = "categories")
public class Category {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private final String name;

    public Category(String name) {
        this.name = name;
    }

}
