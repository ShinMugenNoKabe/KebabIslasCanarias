package es.rufino.kebab.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Autowired
//    UserDetailsService userDetailsService;

    // TODO: Cambiar

    //    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(username -> userRepo
//            .findByUsername(username)
//            .orElseThrow(
//                () -> new UsernameNotFoundException(
//                    format("User: %s, not found", username)
//                )
//            ));
//    }
//
//    /**
//     * Cifrado de contraseña con BCrypt
//     * @return Contraseña cifrada con BCrypt
//     */
    @Bean
    public BCryptPasswordEncoder passwordEnconder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Autoriza ciertas conexiones, como todos los recursos estáticos
     * @param http
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authz -> authz.anyRequest().permitAll());
//                .authorizeHttpRequests((authz) -> authz
//                        .anyRequest().authenticated()
//                )
//                .httpBasic(withDefaults());
        return http.build();
    }
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//            .authorizeRequests()
//                .antMatchers("/", "/webjars/**", "/css/**", "/js/**", "/public/**",
//                        "/auth/**", "/files/**", "/imagenes/**", "/api/v1/products/**",
//                        "/iniciar-sesion/**", "/registro/**", "/pedido/**", "/rest-pedidos/**",
//                        "/pedidos/**", "/quitar-producto-de-pedido/**")
//                .permitAll()
//                .anyRequest().authenticated() // Para todas las demás peticiones debe de estar autenticado
//                .and()
//            .formLogin() // Formulario de login
//                .loginPage("/iniciar-sesion")
//                .defaultSuccessUrl("/", true) // Cuando se inicie sesión nos lleve a la página /public/index
//                .loginProcessingUrl("/auth/login-post") // Controlador de login
//                .permitAll()
//                .and()
//            .logout()
//                .logoutUrl("/auth/logout")
//                .logoutSuccessUrl("/");
//    }
    
}
