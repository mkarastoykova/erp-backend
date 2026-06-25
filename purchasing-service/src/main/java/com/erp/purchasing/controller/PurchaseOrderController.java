package com.erp.purchasing.controller;

import com.erp.common.entity.PurchaseOrder;
import com.erp.purchasing.repository.PurchaseOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchasing/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // to match react frontend
public class PurchaseOrderController {

    private final PurchaseOrderRepository purchaseOrderRepository;

    @GetMapping
    public List<PurchaseOrder> getAllOrders() {
        return purchaseOrderRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<PurchaseOrder> createOrder(@RequestBody PurchaseOrder order) {
        PurchaseOrder saved = purchaseOrderRepository.save(order);
        return ResponseEntity.ok(saved);
    }
}

