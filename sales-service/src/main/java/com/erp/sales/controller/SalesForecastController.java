package com.erp.sales.controller;

import com.erp.common.entity.SalesForecast;
import com.erp.sales.repository.SalesForecastRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales/forecasts")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SalesForecastController {

    private final SalesForecastRepository salesForecastRepository;

    @GetMapping
    public List<SalesForecast> getAllForecasts() {
        return salesForecastRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<SalesForecast> createForecast(@RequestBody SalesForecast forecast) {
        return ResponseEntity.ok(salesForecastRepository.save(forecast));
    }
}

