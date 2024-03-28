package ru.grak.mediasofttask.exceptions;

import java.text.MessageFormat;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(String message) {
        super(message);
    }

    public ProductNotFoundException(String message, Object... args) {
        super(MessageFormat.format(message, args));
    }
}
