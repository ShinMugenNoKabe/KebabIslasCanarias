package es.rufino.kebab.controllers;

import es.rufino.kebab.models.Role;
import es.rufino.kebab.models.User;
import es.rufino.kebab.repositories.RoleRepository;
import es.rufino.kebab.services.UserService;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 *
 * @author Rufino Serrano Cañas
 */
@RestController
@RequestMapping("api/v1/users")
public class UserController {
    
    private final UserService userService;
    private final RoleRepository rolRepository;

    public UserController(UserService userService, RoleRepository rolRepository) {
        this.userService = userService;
        this.rolRepository = rolRepository;
    }

    @PostMapping
    public User registerNewUser(UserRegisterRequest userRegisterRequest) {
        return userService.register(userRegisterRequest.buildUser());
    }

    /**
     * TODO: Change to another class maybe?
     * Comprueba si el usuario loggeado en la sesión contiene el rol de
     * administrador
     *
     * @param principal Clase de Spring boot que contiene almacenado el usuario
     * loggeado
     * @return true si el usuario tiene el rol de administrador, false si no lo
     * tiene
     */
    public Boolean isUserAdmin(Principal principal) {
        User currentUser = userService.findCurrentUser(principal);
        Role adminRole = rolRepository.findByName("ROLE_ADMIN");

        return currentUser.getRoles().contains(adminRole);
    }

    public record UserRegisterRequest(
            @NonNull String firstName,
            @NonNull String lastName,
            @NonNull String email,
            @NonNull String password
    ) {

        public User buildUser() {
            return User.builder()
                    .firstName(firstName())
                    .lastName(firstName())
                    .email(email())
                    .password(password())
                    .build();
        }

    }

}
