package com.project.retail_service.dto.request;

import com.project.retail_service.Model.Inventory;
import com.project.retail_service.Model.Product;
import jakarta.validation.Valid;

public record CombinedRequest(@Valid Product product, @Valid Inventory inventory) { }