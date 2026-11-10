package com.project.retail_service.exception;

import java.io.IOException;

public class InvalidOrderException extends IOException {

    public InvalidOrderException() {
        super("The order configuration is invalid");
    }
}
