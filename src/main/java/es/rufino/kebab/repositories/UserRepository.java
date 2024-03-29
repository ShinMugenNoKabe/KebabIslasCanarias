package es.rufino.kebab.repositories;

import es.rufino.kebab.exceptions.ResourceNotFoundException;
import es.rufino.kebab.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    User findFirstByEmail(String email) throws ResourceNotFoundException;

}
