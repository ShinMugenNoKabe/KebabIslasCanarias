package es.rufino.kebab.configuration;

import es.rufino.kebab.filters.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeHttpRequests -> {
                    authorizeHttpRequests
                            .requestMatchers(HttpMethod.POST,
                                    "/api/v1/files/**"
                            )
                            .hasAnyRole("ADMIN");

                    authorizeHttpRequests
                            .requestMatchers(
                                    "/api/v1/auth/**",
                                    "/api/v1/files/image/**",

                                    // This is to prevent the SecurityConfig to override every error response with a 403 response
                                    "/error",

                                    // SpringDoc
                                    "/api/v1/docs",
                                    "/v3/api-docs",
                                    "/v3/api-docs/**",
                                    "/swagger-resources",
                                    "/swagger-resources/**",
                                    "/configuration/ui*",
                                    "/configuration/security*",
                                    "/swagger-ui/**",
                                    "/webjars/**",
                                    "/api/v1/swagger-ui/**"
                            )
                            .permitAll()
                            .anyRequest()
                            .authenticated();
                })
                .sessionManagement(sessionManagement -> {
                    sessionManagement
                            .sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Spring Boot will create new Session each time
                })
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
    
}
