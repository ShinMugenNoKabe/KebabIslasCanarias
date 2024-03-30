package es.rufino.kebab.configuration;

import es.rufino.kebab.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Class used to set some configuration needed for Spring Boot.
 *
 * @author ShinMugenNoKabe
 */
@Configuration
@RequiredArgsConstructor
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = "es.rufino.kebab.repositories")
@EnableConfigurationProperties(StorageProperties.class)
public class ApplicationConfiguration {

    private final UserRepository userRepository;

    @Bean
    public UserDetailsService userDetailsService() throws UsernameNotFoundException {
        return username -> userRepository
                .findFirstByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("The user does not exists."));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
