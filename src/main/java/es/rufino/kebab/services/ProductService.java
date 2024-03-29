package es.rufino.kebab.services;

import es.rufino.kebab.exceptions.ResourceNotFoundException;
import es.rufino.kebab.models.Category;
import es.rufino.kebab.models.Product;
import es.rufino.kebab.repositories.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final StorageService storageService;

    public ProductService(ProductRepository productRepository, StorageService storageService) {
        this.productRepository = productRepository;
        this.storageService = storageService;
    }

    public Product findById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The product was not found."));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findByCategory(Category category) {
        return productRepository.findByCategory(category);
    }

    public List<Product> findByContainsName(String query) {
        return productRepository.findByNameContainsIgnoreCase(query);
    }

    public Product insert(Product newProduct) {
        return productRepository.save(newProduct);
    }

    public List<Product> insertMany(List<Product> listNewProducts) {
        return productRepository.saveAll(listNewProducts);
    }

    public void delete(Product product) {
        if (product.hasImageAttached()) {
            storageService.delete(product.getImage());
        }

        productRepository.deleteById(product.getId());
    }

    public void deleteById(Long id) {
        productRepository.deleteById(id);
    }

    public Product update(Product product) {
        return productRepository.save(product);
    }

}
