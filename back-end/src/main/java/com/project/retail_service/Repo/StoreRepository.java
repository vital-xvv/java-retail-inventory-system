package com.project.retail_service.Repo;

import com.project.retail_service.Model.Store;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StoreRepository extends JpaRepository<Store, Long> {
    @EntityGraph(value = "Store.withInventoryAndProducts", type = EntityGraph.EntityGraphType.FETCH)
    Optional<Store> findWithInventoryAndProductsById(Long id);
}
