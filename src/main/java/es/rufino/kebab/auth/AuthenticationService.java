package es.rufino.kebab.auth;

import es.rufino.kebab.models.Role;
import es.rufino.kebab.models.User;
import es.rufino.kebab.services.JwtService;
import es.rufino.kebab.services.RoleService;
import es.rufino.kebab.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    protected AuthenticationController.AuthenticationResponse register(
            AuthenticationController.RegisterRequest registerRequest
    ) {
        Role userRole = roleService.findByName("ROLE_USER");

        User user = User.builder()
                .firstName(registerRequest.firstName())
                .lastName(registerRequest.lastName())
                .email(registerRequest.email())
                .password(passwordEncoder.encode(registerRequest.password()))
                .roles(Collections.singletonList(userRole))
                .build();

        userService.register(user);

        return generateAuthenticationResponseWithJwtToken(user);
    }

    protected AuthenticationController.AuthenticationResponse authenticate(
            AuthenticationController.AuthenticationRequest authenticationRequest
    ) {
        // If the JWT token is not valid, an exception will be thrown by this function
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        authenticationRequest.email(),
                        authenticationRequest.password()
                )
        );

        User user = userService.findByEmail(authenticationRequest.email());
        return generateAuthenticationResponseWithJwtToken(user);
    }

    private AuthenticationController.AuthenticationResponse generateAuthenticationResponseWithJwtToken(
            User user
    ) {
        String jwtToken = jwtService.generateToken(user);
        return new AuthenticationController.AuthenticationResponse(jwtToken);
    }

}
