/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.modelo;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Entity
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "lineas_pedido")
public class LineaPedido implements Serializable {
    
    @ManyToOne
    @Id
    private Pedido pedido;
    
    @ManyToOne
    @Id
    private Producto producto;
    
    @Positive(message = "{producto.precio.mayor_de_cero}")
    private Integer cantidad;
    
    private Double importe;

    public LineaPedido(Producto producto, Integer cantidad) {
        this.producto = producto;
        this.cantidad = cantidad;
        this.importe = producto.getPorcentajeDeOferta() * cantidad;
    }

    public LineaPedido(Integer cantidad, Double importe) {
        this.cantidad = cantidad;
        this.importe = importe;
    }
    
}