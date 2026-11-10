package com.project.retail_service.Controller;

import com.project.retail_service.Model.Product;
import com.project.retail_service.Repo.InventoryRepository;
import com.project.retail_service.Repo.ProductRepository;
import com.project.retail_service.Repo.specification.ProductSpecifications;
import com.project.retail_service.Service.ServiceClass;
import com.project.retail_service.dto.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ServiceClass serviceClass;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public ProductController(ServiceClass serviceClass,
                             ProductRepository productRepository,
                             InventoryRepository inventoryRepository) {
        this.serviceClass = serviceClass;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<Void>> addProduct(@Valid @RequestBody Product product) {
        if (serviceClass.validateProduct(product.getName()) || serviceClass.validateProduct(product.getId())) {
            return ResponseEntity.ok().body(new ApiResponse<>("success", "Product already exists"));
        }
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(new ApiResponse<>("success", "Product created successfully"));
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ApiResponse<Map<String, Product>>> getProductById(@PathVariable("productId") long productId) {
        Optional<Product> product = productRepository.findById(productId);
        return product.map(value -> ResponseEntity.ok(new ApiResponse<>("success", "Product retrieved successfully", Map.of("product", value))))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("error", "Product not found")));
    }

    @PutMapping
    public ResponseEntity<ApiResponse<Void>> updateProduct(@Valid @RequestBody Product product) {
        if (serviceClass.validateProduct(product.getId())) {
            productRepository.save(product);
            return ResponseEntity.ok().body(new ApiResponse<>("success", "Product updated successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse<>("error", "Product not found"));
    }

    @GetMapping("/category/{name}/{category}")
    public ResponseEntity<Map<String, Object>> filterByCategoryProduct(
            @PathVariable("name") String name,
            @PathVariable("category") String category) {
        return ResponseEntity.ok().body(Map.of("products", productRepository.findByNameContainingAndCategory(name, category)));
    }

    @GetMapping()
    public ResponseEntity<Map<String, Object>> listProduct() {
        return ResponseEntity.ok().body(Map.of("products", productRepository.findAll()));
    }


    @GetMapping("/filter/{category}/{storeId}")
    public ResponseEntity<ApiResponse<Map<String, List<Product>>>> getProductByCategoryAndStoreId(
            @PathVariable("category") String category,
            @PathVariable("storeId") long storeId) {
        if (!serviceClass.validateStore(storeId)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "Store not found"));
        }
        List<Product> products = productRepository.findAll(Specification.allOf(
                ProductSpecifications.inStore(storeId),
                ProductSpecifications.hasCategory(category)
        ));
        return ResponseEntity.ok(new ApiResponse<>(
                "success",
                "Products retrieved successfully",
                Map.of("products", products)
        ));
    }

    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<ApiResponse<Void>> deleteProduct(@PathVariable("id") long id) {
        if (serviceClass.validateProduct(id)) {
            inventoryRepository.deleteByProductId(id);
            productRepository.deleteById(id);
            return ResponseEntity.ok(new ApiResponse<>("success", "Product deleted successfully"));
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>("error", "Product not found"));
    }

    @GetMapping("/searchProduct/{name}")
    public ResponseEntity<ApiResponse<Map<String, List<Product>>>> searchProduct(@PathVariable("name") String name) {
        List<Product> products = productRepository.findByNameContaining(name);
        return ResponseEntity.ok(new ApiResponse<>("success", "Products retrieved successfully", Map.of("products", products)));
    }
}
