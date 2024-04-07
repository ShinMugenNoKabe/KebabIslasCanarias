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

    public Product findById(Long id) throws ResourceNotFoundException {
        return productRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("The product was not found."));
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public List<Product> findAll(ProductController.ProductsRequestDto productsRequestDto) {
        return productRepository.findAll(getProductSpecifications(productsRequestDto));
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
            ProductController.ProductsRequestDto productsRequestDto
    ) {
        return (root, query, criteriaBuilder) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (productsRequestDto.name() != null && !productsRequestDto.name().isEmpty()) {
                predicates.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + productsRequestDto.name().toLowerCase() + "%"));
            }

            if (productsRequestDto.category_id() != null) {
                predicates.add(criteriaBuilder.equal(root.get("category").get("id"), productsRequestDto.category_id()));
            }

            if (productsRequestDto.minimumPrice() != null) {
                predicates.add(criteriaBuilder.greaterThanOrEqualTo(root.get("price"), productsRequestDto.minimumPrice()));
            }

            if (productsRequestDto.maximumPrice() != null) {
                predicates.add(criteriaBuilder.lessThanOrEqualTo(root.get("price"), productsRequestDto.maximumPrice()));
            }

            predicates.add(criteriaBuilder.isTrue(root.get("isAvailable"))); // TODO: Check if is admin

            if (productsRequestDto.sortOrder() != null) {
                Order priceOrder =
                        productsRequestDto.sortOrder() == Sort.Direction.ASC
                                ? criteriaBuilder.asc(root.get("price"))
                                : criteriaBuilder.desc(root.get("price"));

                query.orderBy(priceOrder);
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }

}
