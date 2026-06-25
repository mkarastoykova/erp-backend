package com.erp.crm;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan(basePackages = "com.erp.common.entity")
@EnableJpaRepositories(basePackages = "com.erp.crm.repository")
public class CrmServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(CrmServiceApplication.class, args);
    }
}

