package es.rufino.kebab.repositories;

import es.rufino.kebab.models.Product;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Rufino Serrano Cañas
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findAll(Specification<Product> specifications);

}
