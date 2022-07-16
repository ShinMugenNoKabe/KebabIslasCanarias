/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.servicios;

import es.rufino.kebab.modelo.Categoria;
import es.rufino.kebab.modelo.LineaPedido;
import es.rufino.kebab.modelo.Pedido;
import es.rufino.kebab.modelo.Producto;
import es.rufino.kebab.modelo.Usuario;
import es.rufino.kebab.repositories.CategoriaRepository;
import es.rufino.kebab.repositories.PedidoRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Service
public class CategoriaServicio {

    @Autowired
    CategoriaRepository repositorio;

    public Categoria insertar(Categoria c) {
        return repositorio.save(c);
    }

    public Categoria buscarPorId(Integer id) {
        return repositorio.findById(id).orElse(null);
    }

    public List<Categoria>buscarTodos() {
        return repositorio.findAll();
    }
    
}
