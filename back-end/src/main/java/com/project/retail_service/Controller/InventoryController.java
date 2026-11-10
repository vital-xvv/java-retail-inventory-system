package com.project.retail_service.Controller;

import com.project.retail_service.Model.Inventory;
import com.project.retail_service.Model.Product;
import com.project.retail_service.Repo.InventoryRepository;
import com.project.retail_service.Repo.ProductRepository;
import com.project.retail_service.Repo.specification.ProductSpecifications;
import com.project.retail_service.Repo.StoreRepository;
import com.project.retail_service.Service.ServiceClass;
import com.project.retail_service.dto.request.CombinedRequest;
import com.project.retail_service.dto.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private final ServiceClass serviceClass;
    private final InventoryRepository inventoryRepository;
    private final StoreRepository storeRepository;
    private final ProductRepository productRepository;

    public InventoryController(ServiceClass serviceClass,
                               InventoryRepository inventoryRepository,
                               StoreRepository storeRepository,
                               ProductRepository productRepository) {
        this.serviceClass = serviceClass;
        this.inventoryRepository = inventoryRepository;
        this.storeRepository = storeRepository;
        this.productRepository = productRepository;
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateInventory(@Valid @RequestBody CombinedRequest combinedRequest) {
        boolean isProductValid = serviceClass.validateProduct(combinedRequest.product().getId());
        boolean isInventoryValid = serviceClass.validateInventory(combinedRequest.product().getId(), combinedRequest.inventory().getId());
        if (isInventoryValid && isProductValid) {
            combinedRequest.inventory().setProduct(combinedRequest.product());
            inventoryRepository.save(combinedRequest.inventory());
            return ResponseEntity.ok(new ApiResponse<>("success", "Inventory updated successfully"));
        } else {
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", "Invalid inventory or product"));
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Void>> saveInventory(@Valid @RequestBody Inventory inventory) {
        if (serviceClass.validateInventory(inventory.getProduct().getId(), inventory.getStore().getId())) {
            return ResponseEntity.ok().body(new ApiResponse<>("success", "Inventory already exists"));
        } else {
            if (serviceClass.validateProduct(inventory.getProduct().getId())
                    && serviceClass.validateStore(inventory.getStore().getId())) {
                inventoryRepository.save(inventory);
                return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("success", "Inventory saved successfully"));
            } else {
                return ResponseEntity.badRequest().body(new ApiResponse<>("error", "Invalid inventory configuration"));
            }
        }
    }

    @GetMapping(value = "/products/{storeId}", params = "!name")
    public ResponseEntity<ApiResponse<Map<String, List<Product>>>> getAllProducts(@PathVariable("storeId") long storeId) {
        return storeRepository.findWithInventoryAndProductsById(storeId)
                .map(store -> {
                    List<Product> products = store.getInventories().stream()
                            .map(Inventory::getProduct)
                            .toList();
                    return ResponseEntity.ok(new ApiResponse<>(
                            "success",
                            "Products retrieved successfully",
                            Map.of("products", products)
                    ));
                })
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ApiResponse<>("error", "Store not found")));
    }

    @GetMapping("/products")
    public ResponseEntity<ApiResponse<Map<String, List<Product>>>> getAllProducts(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String name) {
        List<Product> products = productRepository.findAll(Specification.allOf(
                ProductSpecifications.hasCategory(category),
                ProductSpecifications.nameContains(name)
        ));
        return ResponseEntity.ok(new ApiResponse<>(
                "success",
                "Products retrieved successfully",
                Map.of("product", products)
        ));
    }

    @GetMapping(value = "/search/{name}/{storeId}")
    public ResponseEntity<ApiResponse<Map<String, List<Product>>>> searchProducts(@PathVariable("storeId") Long storeId,
                                                                                  @PathVariable("name") String name) {
        return ResponseEntity.ok(new ApiResponse<>("success", "Products retrieved successfully",
                Map.of("product", productRepository.findByStoreIdAndNameLike(storeId, name))));
    }

    @DeleteMapping("/product/{productId}")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> removeProduct(@PathVariable("productId") Long productId) {
        if (serviceClass.validateProduct(productId)) {
            inventoryRepository.deleteByProductId(productId);
            productRepository.deleteById(productId);
            return ResponseEntity.ok(new ApiResponse<>("success", "Product removed successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>("error", "Product not found"));
    }

    @GetMapping("/products/available")
    public boolean isProductAvailable(@RequestParam("productId") Long productId,
                                      @RequestParam("storeId") Long storeId,
                                      @RequestParam("quantity") Integer quantity) {
        if (quantity == null || quantity < 0) {
            return false;
        }
        return inventoryRepository.findByProductIdAndStoreId(productId, storeId)
                .map(inventory -> inventory.getStockLevel() >= quantity)
                .orElse(false);
    }
}
