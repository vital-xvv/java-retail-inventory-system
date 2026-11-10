package com.project.retail_service.Service;

import com.project.retail_service.Model.*;
import com.project.retail_service.Repo.*;
import com.project.retail_service.dto.request.PlaceOrderRequestDTO;
import com.project.retail_service.dto.request.PurchaseProductDTO;
import com.project.retail_service.exception.InvalidOrderException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {

    private final OrderDetailsRepository orderDetailsRepository;
    private final StoreRepository storeRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;

    public OrderService(OrderDetailsRepository orderDetailsRepository,
                        StoreRepository storeRepository,
                        CustomerRepository customerRepository,
                        ProductRepository productRepository,
                        InventoryRepository inventoryRepository) {
        this.orderDetailsRepository = orderDetailsRepository;
        this.storeRepository = storeRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.inventoryRepository = inventoryRepository;
    }

    @Transactional
    public void saveOrder(PlaceOrderRequestDTO placeOrderRequest) throws InvalidOrderException {
        OrderDetails orderDetails = createOrder(placeOrderRequest);
        orderDetails.setOrderItems(getAllOrderItems(placeOrderRequest));
        orderDetailsRepository.save(orderDetails);
    }

    private Customer findOrSaveCustomer(PlaceOrderRequestDTO placeOrderRequest) {
        Customer customer = customerRepository.findByEmail(placeOrderRequest.customerEmail());
        if (customer == null) {
            customer = new Customer();
            customer.setEmail(placeOrderRequest.customerEmail());
            customer.setName(placeOrderRequest.customerName());
            customer.setPhone(placeOrderRequest.customerPhone());
            customerRepository.save(customer);
        }
        return customer;
    }

    public OrderDetails createOrder(PlaceOrderRequestDTO placeOrderRequest) {
        OrderDetails orderDetails = new OrderDetails();
        orderDetails.setDate(LocalDateTime.now());
        orderDetails.setTotalPrice(placeOrderRequest.totalPrice());
        orderDetails.setStore(getStore(placeOrderRequest.storeId()));
        orderDetails.setCustomer(findOrSaveCustomer(placeOrderRequest));
        return orderDetails;
    }

    public Store getStore(long storeId) {
        return storeRepository.findById(storeId).orElseThrow();
    }

    public List<OrderItem> getAllOrderItems(PlaceOrderRequestDTO placeOrderRequest) throws InvalidOrderException {
        List<OrderItem> orderItems = new ArrayList<>();
        for( PurchaseProductDTO p : placeOrderRequest.purchaseProduct()) {
            Optional<Product> product = productRepository.findById(p.id());
            Optional<Inventory> inventory = inventoryRepository.findByProductIdAndStoreId(p.id(), placeOrderRequest.storeId());
            if (product.isEmpty() || inventory.isEmpty() || p.quantity() > inventory.get().getStockLevel()) {
                throw new InvalidOrderException();
            } else {
                OrderItem orderItem = new OrderItem();
                orderItem.setProduct(product.get());
                orderItem.setQuantity(p.quantity());
                orderItem.setPrice(p.price());
                orderItems.add(orderItem);
                inventory.get().setStockLevel(inventory.get().getStockLevel() - p.quantity());
                inventoryRepository.save(inventory.get());
            }
        }
        return orderItems;
    }
}
