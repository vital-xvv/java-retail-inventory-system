package com.project.code.Service;

import com.project.code.Model.Inventory;
import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import org.springframework.stereotype.Component;

@Component
public class ServiceClass {
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;

    public ServiceClass(InventoryRepository inventoryRepository, ProductRepository productRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
    }

    public boolean validateInventory(long productId, long storeId) {
        return inventoryRepository.findByProductIdAndStoreId(productId, storeId).isPresent();
    }

    public boolean validateProduct(long productId) {
        return productRepository.findById(productId).isPresent();
    }

    public boolean validateProduct(String productName) {
        return productRepository.findByName(productName).isPresent();
    }

    public Inventory getInventory(long productId, long storeId) {
        return inventoryRepository.findByProductIdAndStoreId(productId, storeId).orElseThrow();
    }
}
