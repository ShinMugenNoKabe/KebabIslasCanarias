package es.rufino.kebab.services;

import es.rufino.kebab.models.Privilege;
import es.rufino.kebab.models.Role;
import es.rufino.kebab.models.User;
import jakarta.transaction.Transactional;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Rufino Serrano Cañas
 */
@Service("userDetailsService")
public class UserDetailServiceImpl implements UserDetailsService {

    private final UserService userService;
    private final RoleService roleService;

    public UserDetailServiceImpl(
            UserService userService,
            RoleService roleService
    ) {
        this.userService = userService;
        this.roleService = roleService;
    }

    /**
     * Loads a username in the memory after login
     * @param email the username identifying the user whose data is required.
     * @return Auth object
     * @throws UsernameNotFoundException if the user was not found
     */
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userService.findByEmail(email);

        if (user == null) {
            return new org.springframework.security.core.userdetails.User(
                    " ", " ", true, true, true, true,
                    getAuthorities(Collections.singletonList(roleService.findByName("ROLE_USER"))));
        }

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), true, true, true,
                true, getAuthorities(user.getRoles()));
    }

    private List<? extends GrantedAuthority> getAuthorities(List<Role> roles) {
        return getGrantedAuthorities(getPrivileges(roles));
    }

    private List<String> getPrivileges(List<Role> roles) {
        List<String> privileges = new ArrayList<>();

        for (Role role : roles) {
            privileges.add(role.getName());

            for (Privilege privilege : role.getPrivileges()) {
                privileges.add(privilege.getName());
            }
        }

        return privileges;
    }

    private List<GrantedAuthority> getGrantedAuthorities(List<String> privileges) {
        return privileges
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

}
