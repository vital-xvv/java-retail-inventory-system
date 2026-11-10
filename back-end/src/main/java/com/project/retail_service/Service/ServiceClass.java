package com.project.retail_service.Service;

import com.project.retail_service.Repo.InventoryRepository;
import com.project.retail_service.Repo.ProductRepository;
import com.project.retail_service.Repo.StoreRepository;
import org.springframework.stereotype.Component;

@Component
public class ServiceClass {
    private final InventoryRepository inventoryRepository;
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;

    public ServiceClass(InventoryRepository inventoryRepository,
                        ProductRepository productRepository,
                        StoreRepository storeRepository) {
        this.inventoryRepository = inventoryRepository;
        this.productRepository = productRepository;
        this.storeRepository = storeRepository;
    }

    public boolean validateInventory(long productId, long storeId) {
        return inventoryRepository.findByProductIdAndStoreId(productId, storeId).isPresent();
    }

    public boolean validateProduct(Long productId) {
        return productId != null && productRepository.findById(productId).isPresent();
    }

    public boolean validateProduct(String productName) {
        return productRepository.findByName(productName).isPresent();
    }

    public boolean validateStore(long storeId) {
        return storeRepository.findById(storeId).isPresent();
    }
}
