package com.erp.sales.controller;

import com.erp.common.entity.SalesPipeline;
import com.erp.sales.repository.SalesPipelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales/pipeline")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SalesPipelineController {

    private final SalesPipelineRepository salesPipelineRepository;

    @GetMapping
    public List<SalesPipeline> getPipeline() {
        return salesPipelineRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<SalesPipeline> createPipeline(@RequestBody SalesPipeline pipeline) {
        return ResponseEntity.ok(salesPipelineRepository.save(pipeline));
    }
}

