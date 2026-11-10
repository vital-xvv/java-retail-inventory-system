package com.project.retail_service.Repo;

import com.project.retail_service.Model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByCategory(String category);
    List<Product> findByNameContainingAndCategory(String name, String category);
    List<Product> findByPriceBetween(double minPrice, double maxPrice);
    Optional<Product> findByName(String name);
    @Query("""
            SELECT DISTINCT p FROM Product p
            JOIN p.inventories i
            WHERE i.store.id = :storeId
              AND LOWER(p.name) LIKE LOWER(CONCAT('%', :name, '%'))
            """)
    List<Product> findByStoreIdAndNameLike(@Param("storeId") Long storeId, @Param("name") String name);
    List<Product> findByNameContaining(String name);
}
