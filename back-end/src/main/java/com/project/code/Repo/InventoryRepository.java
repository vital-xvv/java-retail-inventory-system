package com.project.code.Repo;

import com.project.code.Model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {
    Optional<Inventory> findByProductIdAndStoreId(Long productId, Long storeId);
    Optional<Inventory> findByStoreId(Long storeId);
    @Modifying
    void deleteByProductId(Long productId);
}
