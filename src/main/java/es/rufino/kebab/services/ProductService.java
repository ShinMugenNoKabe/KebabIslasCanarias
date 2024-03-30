package es.rufino.kebab.services;

import es.rufino.kebab.controllers.ProductController;
import es.rufino.kebab.exceptions.ResourceNotFoundException;
import es.rufino.kebab.models.Product;
import es.rufino.kebab.repositories.ProductRepository;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Rufino Serrano Cañas
 */
@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final StorageService storageService;

    public Product findById(Long id) {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The product was not found."));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findAll(ProductController.ProductsRequest productsRequest) {
        return productRepository.findAll(getProductSpecifications(productsRequest));
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

    private Specification<Product> getProductSpecifications(
            ProductController.ProductsRequest productsRequest
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (productsRequest.name() != null && !productsRequest.name().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + productsRequest.name().toLowerCase() + "%"));
            }

            if (productsRequest.category_id() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), productsRequest.category_id()));
            }

            if (productsRequest.minimumPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), productsRequest.minimumPrice()));
            }

            if (productsRequest.maximumPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), productsRequest.maximumPrice()));
            }

            predicates.add(criteriaBuilder.isTrue(root.get("isAvailable"))); // TODO: Check if is admin

            if (productsRequest.sortOrder() != null) {
                Order priceOrder =
                        productsRequest.sortOrder() == Sort.Direction.ASC
                                ? criteriaBuilder.asc(root.get("price"))
                                : criteriaBuilder.desc(root.get("price"));

                query.orderBy(priceOrder);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
