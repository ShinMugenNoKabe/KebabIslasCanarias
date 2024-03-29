package es.rufino.kebab.services;

import es.rufino.kebab.exceptions.ResourceAlreadyExistsException;
import es.rufino.kebab.models.User;
import es.rufino.kebab.repositories.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            BCryptPasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User findById(Long id) {
        return userRepository
                .findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("The user does not exist"));
    }

    public User register(User newUser) {
        if (userEmailAlreadyExists(newUser.getEmail())) {
            throw new ResourceAlreadyExistsException("An user with that email already exists.");
        }

        newUser.setPassword(passwordEncoder.encode(newUser.getPassword())); // TODO: Mover a la clase de User?
        return userRepository.save(newUser);
    }

    private boolean userEmailAlreadyExists(String email) {
        return findByEmail(email) != null;
    }

    public User findByEmail(String email) {
        return userRepository.findFirstByEmail(email);
    }

    public User findCurrentUser(Principal principal) {
        return findByEmail(principal.getName());
    }
    
}
