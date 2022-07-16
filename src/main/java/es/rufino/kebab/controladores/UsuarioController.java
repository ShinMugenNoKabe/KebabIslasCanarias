/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.controladores;

import es.rufino.kebab.modelo.Rol;
import es.rufino.kebab.modelo.Usuario;
import es.rufino.kebab.repositories.RolRepository;
import es.rufino.kebab.servicios.UsuarioServicio;
import java.security.Principal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Controller
public class UsuarioController {
    
    @Autowired
    private UsuarioServicio usuarioServicio;
    
    @Autowired
    private RolRepository rolRepositorio;

    /**
     * Comprueba si el usuario loggeado en la sesión contiene el rol de
     * administrador
     *
     * @param principal Clase de Spring boot que contiene almacenado el usuario
     * loggeado
     * @return true si el usuario tiene el rol de administrador, false si no lo
     * tiene
     */
    public Boolean comprobarAdmin(Principal principal) {
        Usuario usuarioLoggeado = usuarioServicio.getUsuarioLoggeado(principal);

        Rol rolDeAdmin = rolRepositorio.findByNombre("ROLE_ADMIN");

        return usuarioLoggeado.getListaDeRoles().contains(rolDeAdmin);
    }
}
