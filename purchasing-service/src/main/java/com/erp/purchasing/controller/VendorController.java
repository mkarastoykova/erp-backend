package com.erp.purchasing.controller;

import com.erp.common.entity.Vendor;
import com.erp.purchasing.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/purchasing/vendors")
@RequiredArgsConstructor
@CrossOrigin(origins = "*") // to match react frontend
public class VendorController {

    private final VendorRepository vendorRepository;

    @GetMapping
    public List<Vendor> getAllVendors() {
        return vendorRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Vendor> createVendor(@RequestBody Vendor vendor) {
        Vendor saved = vendorRepository.save(vendor);
        return ResponseEntity.ok(saved);
    }
}

