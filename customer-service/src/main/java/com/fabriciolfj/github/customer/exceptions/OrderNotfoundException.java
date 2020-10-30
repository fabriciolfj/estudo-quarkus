package com.fabriciolfj.github.customer.exceptions;

public class OrderNotfoundException extends RuntimeException {

    public OrderNotfoundException(final String msg) {
        super(msg);
    }
}
