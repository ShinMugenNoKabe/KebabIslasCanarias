/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.servicios;

import es.rufino.kebab.modelo.Usuario;
import es.rufino.kebab.repositories.RolRepository;
import es.rufino.kebab.repositories.UsuarioRepository;
import java.security.Principal;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Service
public class UsuarioServicio {

    @Autowired
    UsuarioRepository usuarioRepositorio;
    
    @Autowired
    RolRepository rolRepositorio;

    // PasswordEncoder para codificar en el registro
    @Autowired
    BCryptPasswordEncoder passwordEncoder;

    public Usuario registrar(Usuario u) {
        // Cifrado de contraseña
        u.setPassword(passwordEncoder.encode(u.getPassword()));
        //u.setListaDeRoles(Arrays.asList(rolRepositorio.findByNombre("ROLE_USER")));
        
        return usuarioRepositorio.save(u);
    }

    public Usuario findById(Integer id) {
        return usuarioRepositorio.findById(id).orElse(null);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepositorio.findFirstByEmail(email);
    }
    
    public Usuario getUsuarioLoggeado(Principal principal) {
        return buscarPorEmail(principal.getName());
    }
    
}
