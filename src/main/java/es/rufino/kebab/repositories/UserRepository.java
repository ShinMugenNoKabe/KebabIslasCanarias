package es.rufino.kebab.repositories;

import es.rufino.kebab.exceptions.ResourceNotFoundException;
import es.rufino.kebab.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmail(String email) throws ResourceNotFoundException;

}
