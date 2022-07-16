/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.modelo;

import java.io.Serializable;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "categorias")
public class Categoria implements Serializable {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @PositiveOrZero(message = "Elija una categoría válida.")
    private Integer codcat;
    
    @NotEmpty(message = "Elija una categoría válida.")
    private String nombre;
    
    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "categoria")
    private List<Producto> listaDeProductos;*/

    public Categoria(String nombre) {
        this.nombre = nombre;
    }
}
