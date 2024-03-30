package es.rufino.kebab.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication")
public class AuthenticationController {

    private final AuthenticationService authenticationservice;

    @Operation(
            description = "Registers a new user in the system. A new JWT token will be generated.",
            summary = "Register new user",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", useReturnTypeSchema = true),
                    @ApiResponse(description = "User email already exists", responseCode = "400")
            }
    )
    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody RegisterRequest registerRequest
    ) {
        return ResponseEntity.ok(authenticationservice.register(registerRequest));
    }

    @Operation(
            description = "Logs in into the system with the email and password of an user. A new JWT token will be generated.",
            summary = "Log in",
            responses = {
                    @ApiResponse(description = "Success", responseCode = "200", useReturnTypeSchema = true),
                    @ApiResponse(description = "User email already exists", responseCode = "400")
            }
    )
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
