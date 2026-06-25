package com.erp.purchasing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.erp.common.entity")
public class PurchasingApplication {
    public static void main(String[] args) {
        SpringApplication.run(PurchasingApplication.class, args);
    }
}
