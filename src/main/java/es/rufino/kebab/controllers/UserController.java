package es.rufino.kebab.controllers;

import es.rufino.kebab.services.RoleService;
import es.rufino.kebab.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author Rufino Serrano Cañas
 */
@RestController
@RequestMapping("api/v1/users")
@RequiredArgsConstructor
public class UserController {
    
    private final UserService userService;
    private final RoleService roleService;

}
