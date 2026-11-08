package com.project.code.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record PlaceOrderRequestDTO(@Min(1) Long storeId,
                                   @NotBlank String customerName,
                                   @NotBlank @Email String customerEmail,
                                   @NotBlank String customerPhone,
                                   @NotBlank String datetime,
                                   @Min(1) List<PurchaseProductDTO> purchaseProduct,
                                   @Min(1) Double totalPrice) {
}
