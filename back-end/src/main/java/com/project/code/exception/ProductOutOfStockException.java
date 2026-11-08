package com.project.code.exception;

import java.io.IOException;

public class ProductOutOfStockException extends IOException {

    public ProductOutOfStockException(long productId, String productName) {
        super("Product %s with id %d is out of stock.".formatted(productName, productId));
    }
}
