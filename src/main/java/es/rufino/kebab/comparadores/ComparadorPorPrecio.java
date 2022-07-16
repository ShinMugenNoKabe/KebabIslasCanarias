/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */

package es.rufino.kebab.comparadores;

import es.rufino.kebab.modelo.Producto;
import java.util.Comparator;

/**
 *
 * @author Rufino Serrano Cañas
 */
public class ComparadorPorPrecio implements Comparator<Producto> {
    
    /**
     * Ordena la lista de productos según el precio de forma ascendente
     * @param p1 Primer producto a comparar
     * @param p2 Segundo producto a comparar
     * @return Si el precio del primer producto es mayor, menor o igual
     */
    @Override
    public int compare(Producto p1, Producto p2) {
        if (p1.getPrecioConDescuento().equals(p2.getPrecioConDescuento())) {
            return 0;
        } else if (p1.getPrecioConDescuento() < p2.getPrecioConDescuento()) {
            return -1;
        } else {
            return 1;
        }
    }

}
