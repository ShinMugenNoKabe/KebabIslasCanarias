/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.modelo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Formatter;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "productos")
public class Producto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer codprod;

    //@NotEmpty(message = "Introduzca un nombre.")
    private String nombre;

    @PositiveOrZero(message = "Introduzca un precio válido.")
    private Double precio;

    //@NotEmpty(message = "Introduzca una imagen")
    private String imagen;

    private Double porcentajeDeOferta;

    private Double precioConDescuento;
    
    private Boolean disponible;
    
    @ManyToOne
    private Categoria categoria;

    /*@OneToMany(cascade = CascadeType.ALL, mappedBy = "producto")
    private List<LineaPedido> listaDeLineasDePedido;*/

    public Producto(String nombre, Double precio, String imagen,
            Double porcentajeDeOferta, Boolean disponible, Categoria categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.imagen = imagen;
        this.porcentajeDeOferta = porcentajeDeOferta;
        this.precioConDescuento = calculaPrecioConDescuento();
        this.disponible = disponible;
        this.categoria = categoria;
    }

    public Double calculaPrecioConDescuento() {
        if (this.getPorcentajeDeOferta().equals(0.0)) {
            return this.getPrecio();
        }

        Double precioDescuentadoCalculado
                = (this.getPrecio() - (this.getPrecio() * (this.getPorcentajeDeOferta() / 100)));

        // Redondea el precio descuentado a 2 decimales
        BigDecimal bd = new BigDecimal(precioDescuentadoCalculado).setScale(2, RoundingMode.UP);

        return bd.doubleValue();
    }
}
