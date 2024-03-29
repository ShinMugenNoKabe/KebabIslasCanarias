package es.rufino.kebab.repositories;

import es.rufino.kebab.models.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {
    
    Category findByName(String name);
    
}
