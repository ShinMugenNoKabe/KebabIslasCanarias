package es.rufino.kebab.services;

import es.rufino.kebab.exceptions.ResourceNotFoundException;
import es.rufino.kebab.models.Category;
import es.rufino.kebab.repositories.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category findById(Long id) {
        return categoryRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Category insert(Category newCategory) {
        return categoryRepository.save(newCategory);
    }

    public List<Category> insertMany(List<Category> newCategories) {
        return categoryRepository.saveAll(newCategories);
    }
    
}
