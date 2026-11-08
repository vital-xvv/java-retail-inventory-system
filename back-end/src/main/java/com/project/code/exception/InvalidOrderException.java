package com.project.code.exception;

import java.io.IOException;

public class InvalidOrderException extends IOException {

    public InvalidOrderException() {
        super("The order configuration is invalid");
    }
}
