/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.seguridad;

import es.rufino.kebab.modelo.Privilegio;
import es.rufino.kebab.modelo.Rol;
import es.rufino.kebab.modelo.Usuario;
import es.rufino.kebab.repositories.RolRepository;
import es.rufino.kebab.repositories.UsuarioRepository;
import es.rufino.kebab.servicios.UsuarioServicio;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Service("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    UsuarioRepository usuarioRepositorio;

    @Autowired
    private UsuarioServicio usuarioServicio;

    @Autowired
    private MessageSource messages;

    @Autowired
    private RolRepository rolRepositorio;

    /**
     * Busca un usuario según su email para completar el proceso de
     * autenticación
     * @param email Email del usuario a buscar
     * @return Autenticación
     * @throws UsernameNotFoundException si el usuario no se ha encontrado
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email)
            throws UsernameNotFoundException {

        Usuario usuario = usuarioServicio.buscarPorEmail(email);

        if (usuario == null) {
            return new org.springframework.security.core.userdetails.User(
                    " ", " ", true, true, true, true,
                    getAuthorities(Arrays.asList(
                            rolRepositorio.findByNombre("ROLE_USER"))));
        }

        return new org.springframework.security.core.userdetails.User(
                usuario.getEmail(), usuario.getPassword(), true, true, true,
                true, getAuthorities(usuario.getListaDeRoles()));

        //usuario.isEnabled(), 
    }

    private List<? extends GrantedAuthority> getAuthorities(
            List<Rol> roles) {

        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(List<Rol> roles) {

        List<String> privilegios = new ArrayList<>();
        List<Privilegio> colleccion = new ArrayList<>();

        for (Rol rol : roles) {
            privilegios.add(rol.getNombre());
            colleccion.addAll(rol.getListaDePrivilegios());
        }

        for (Privilegio item : colleccion) {
            privilegios.add(item.getNombre());
        }

        return privilegios;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        List<GrantedAuthority> authorities = new ArrayList<>();

        for (String privilege : privileges) {
            authorities.add(new SimpleGrantedAuthority(privilege));
        }

        return authorities;
    }

}
