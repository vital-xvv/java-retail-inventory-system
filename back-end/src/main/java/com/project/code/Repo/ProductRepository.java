package com.project.code.Repo;

import com.project.code.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory(String category);
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
    Product findBySku(String sku);
    Optional<Product> findByName(String name);
    List<Product> findByNameLike(String name);
}
