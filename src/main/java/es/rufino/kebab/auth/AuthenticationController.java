package es.rufino.kebab.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationservice;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest registerRequest
    ) {
        return ResponseEntity.ok(authenticationservice.register(registerRequest));
    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(
            @RequestBody AuthenticationRequest authRequest
    ) {
        return ResponseEntity.ok(authenticationservice.authenticate(authRequest));
    }

    public record AuthenticationResponse(String token) {
    }

    public record RegisterRequest(
            String firstName,
            String lastName,
            String email,
            String password
    ) {
    }

    public record AuthenticationRequest(
            String email,
            String password
    ) {
    }

}
