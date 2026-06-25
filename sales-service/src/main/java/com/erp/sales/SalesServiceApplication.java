package com.erp.sales;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.erp.common.entity")
public class SalesServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(SalesServiceApplication.class, args);
    }
}

