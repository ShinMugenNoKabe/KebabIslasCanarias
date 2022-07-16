/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.repositories;

import es.rufino.kebab.modelo.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author Rufino Serrano Cañas
 */
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {

    Usuario findFirstByEmail(String email);

}
