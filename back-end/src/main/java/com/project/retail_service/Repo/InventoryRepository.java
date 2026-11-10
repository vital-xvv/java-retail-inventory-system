package com.project.retail_service.Repo;

import com.project.retail_service.Model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductIdAndStoreId(Long productId, Long storeId);
    Optional<Inventory> findByStoreId(Long storeId);

    @Transactional
    void deleteByProductId(Long productId);
}
