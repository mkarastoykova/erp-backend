package com.erp.crm.exception;
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String r, String f, Object v) {
        super(String.format("%s not found with %s: '%s'", r, f, v));
    }
}

