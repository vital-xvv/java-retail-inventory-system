package com.project.code.Controller;

import com.project.code.Repo.InventoryRepository;
import com.project.code.Repo.ProductRepository;
import com.project.code.Service.ServiceClass;
import com.project.code.dto.request.CombinedRequest;
import com.project.code.dto.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/inventory")
public class InventoryController {
    private final ProductRepository productRepository;
    private final ServiceClass serviceClass;
    private final InventoryRepository inventoryRepository;

    public InventoryController(ProductRepository productRepository,
                               ServiceClass serviceClass,
                               InventoryRepository inventoryRepository) {
        this.productRepository = productRepository;
        this.serviceClass = serviceClass;
        this.inventoryRepository = inventoryRepository;
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


// 4. Define the `saveInventory` Method:
//    - This method handles HTTP POST requests to save a new inventory entry.
//    - It accepts an `Inventory` object in the request body.
//    - It first validates whether the inventory already exists. If it exists, it returns a message stating so. If it doesn’t exist, it saves the inventory and returns a success message.


// 5. Define the `getAllProducts` Method:
//    - This method handles HTTP GET requests to retrieve products for a specific store.
//    - It uses the `storeId` as a path variable and fetches the list of products from the database for the given store.
//    - The products are returned in a `Map` with the key `"products"`.


// 6. Define the `getProductName` Method:
//    - This method handles HTTP GET requests to filter products by category and name.
//    - If either the category or name is `"null"`, adjust the filtering logic accordingly.
//    - Return the filtered products in the response with the key `"product"`.


// 7. Define the `searchProduct` Method:
//    - This method handles HTTP GET requests to search for products by name within a specific store.
//    - It uses `name` and `storeId` as parameters and searches for products that match the `name` in the specified store.
//    - The search results are returned in the response with the key `"product"`.


// 8. Define the `removeProduct` Method:
//    - This method handles HTTP DELETE requests to delete a product by its ID.
//    - It first validates if the product exists. If it does, it deletes the product from the `ProductRepository` and also removes the related inventory entry from the `InventoryRepository`.
//    - Returns a success message with the key `"message"` indicating successful deletion.


// 9. Define the `validateQuantity` Method:
//    - This method handles HTTP GET requests to validate if a specified quantity of a product is available in stock for a given store.
//    - It checks the inventory for the product in the specified store and compares it to the requested quantity.
//    - If sufficient stock is available, return `true`; otherwise, return `false`.

}
