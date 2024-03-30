package es.rufino.kebab.services;

import es.rufino.kebab.exceptions.ResourceAlreadyExistsException;
import es.rufino.kebab.models.User;
import es.rufino.kebab.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.security.Principal;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public User findById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("The user does not exist"));
    }

    public User register(User newUser) {
        if (userEmailAlreadyExists(newUser.getEmail())) {
            throw new ResourceAlreadyExistsException("A user with that email already exists.");
        }

        return userRepository.save(newUser);
    }

    public User findByEmail(String email) {
        return userRepository
                .findFirstByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("The user does not exists."));
    }

    private boolean userEmailAlreadyExists(String email) {
        try {
            return findByEmail(email) != null;
        } catch (UsernameNotFoundException ex) {
            return false;
        }
    }

    public User findCurrentUser(Principal principal) {
        return findByEmail(principal.getName());
    }
    
}
