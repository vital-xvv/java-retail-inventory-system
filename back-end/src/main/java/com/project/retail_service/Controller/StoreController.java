package com.project.retail_service.Controller;

import com.project.retail_service.dto.request.PlaceOrderRequestDTO;
import com.project.retail_service.Model.Store;
import com.project.retail_service.Repo.StoreRepository;
import com.project.retail_service.Service.OrderService;
import com.project.retail_service.dto.response.ApiResponse;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/store")
public class StoreController {

    private final StoreRepository storeRepository;
    private final OrderService orderService;

    public StoreController(StoreRepository storeRepository,
                           OrderService orderService) {
        this.storeRepository = storeRepository;
        this.orderService = orderService;
    }

    @PostMapping()
    public ResponseEntity<ApiResponse<?>> addStore(@Valid @RequestBody Store store) {
        storeRepository.save(store);
        return ResponseEntity.ok(new ApiResponse<>("success", "Store created successfully"));
    }

    @GetMapping("/validate/{storeId}")
    public boolean validateStore(@PathVariable("storeId") long storeId) {
        return storeRepository.findById(storeId).isPresent();
    }

    @PostMapping("/placeOrder")
    public ResponseEntity<ApiResponse<?>> placeOrder(@Valid @RequestBody PlaceOrderRequestDTO placeOrderRequest) {
        try {
            orderService.saveOrder(placeOrderRequest);
            return ResponseEntity.ok(new ApiResponse<>("success", "Order placed successfully"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new ApiResponse<>("error", "Failed to place order", e.getMessage()));
        }
    }
}
