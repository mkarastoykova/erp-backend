package com.erp.inventory.exception;
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException(String r, String f, Object v) {
        super(String.format("%s already exists with %s: '%s'", r, f, v));
    }
}

