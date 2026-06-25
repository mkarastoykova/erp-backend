package com.erp.sales.controller;

import com.erp.common.entity.SalesRep;
import com.erp.sales.repository.SalesRepRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales/reps")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SalesRepController {

    private final SalesRepRepository salesRepRepository;

    @GetMapping
    public List<SalesRep> getAllReps() {
        return salesRepRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<SalesRep> createRep(@RequestBody SalesRep rep) {
        return ResponseEntity.ok(salesRepRepository.save(rep));
    }
}

