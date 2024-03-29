package es.rufino.kebab.repositories;

import es.rufino.kebab.models.Category;
import es.rufino.kebab.models.Product;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Rufino Serrano Cañas
 */
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByCategory(Category category);

    List<Product> findByNameContainsIgnoreCase(String name);

    // TODO: Cambiar todo esto por queries string
//    List<Product> findByCategoryEqualsAndIsAvailableEquals(Category category, Boolean isAvailable);
//
//    List<Product> findByNameContainsIgnoreCaseAndCategoryEqualsAndIsAvailableEquals(String nombre, Category category, Boolean disponible);
//
//    List<Product> findByNombreContainsIgnoreCaseAndDisponibleEquals(String nombre, boolean disponible);
//
//    List<Product> findByNombreContainsIgnoreCaseAndCategoriaEquals(String nombre, Category category);
//
//    List<Product> findByCategoriaEquals(Category category);

}
