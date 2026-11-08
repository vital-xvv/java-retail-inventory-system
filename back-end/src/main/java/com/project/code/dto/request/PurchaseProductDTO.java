package com.project.code.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PurchaseProductDTO(@Min(1) Long id, @NotBlank String name, @Min(1) Double price,
                                 @Min(1) Integer quantity, @Min(1) Double total) { }
