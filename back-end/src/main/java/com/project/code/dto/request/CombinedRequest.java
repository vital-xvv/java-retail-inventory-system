package com.project.code.dto.request;

import com.project.code.Model.Inventory;
import com.project.code.Model.Product;
import jakarta.validation.Valid;

public record CombinedRequest(@Valid Product product, @Valid Inventory inventory) { }