/*
 * Copyright (c) Rufino Serrano. All rights reserved.
 */
package es.rufino.kebab.seguridad;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
    @Autowired
    UserDetailsService userDetailsService;
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEnconder());
    }
    
    /**
     * Cifrado de contraseña con BCrypt
     * @return Contraseña cifrada con BCrypt
     */
    @Bean
    public BCryptPasswordEncoder passwordEnconder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Autoriza ciertas conexiones, como todos los recursos estáticos
     * @param http
     * @throws Exception 
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
                .antMatchers("/", "/webjars/**", "/css/**", "/js/**", "/public/**",
                        "/auth/**", "/files/**", "/imagenes/**", "/rest-productos/**",
                        "/iniciar-sesion/**", "/registro/**", "/pedido/**", "/rest-pedidos/**",
                        "/pedidos/**", "/quitar-producto-de-pedido/**")
                .permitAll()
                .anyRequest().authenticated() // Para todas las demás peticiones debe de estar autenticado
                .and()
            .formLogin() // Formulario de login
                .loginPage("/iniciar-sesion")
                .defaultSuccessUrl("/", true) // Cuando se inicie sesión nos lleve a la página /public/index
                .loginProcessingUrl("/auth/login-post") // Controlador de login
                .permitAll()
                .and()
            .logout()
                .logoutUrl("/auth/logout")
                .logoutSuccessUrl("/");
    }
    
}
