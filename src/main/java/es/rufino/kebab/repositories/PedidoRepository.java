/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.repositories;
import es.rufino.kebab.modelo.Pedido;
import es.rufino.kebab.modelo.Usuario;
import java.util.Date;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Rufino Serrano Cañas
 */
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {

    List<Pedido> findByUsuario(Usuario usuario);

    public List<Pedido> findByFechaBetween(Date fechaMin, Date fechaMax);

    public List<Pedido> findByUsuarioEqualsAndFechaBetween(Usuario usuario, Date fechaMin, Date fechaMax);
    
}