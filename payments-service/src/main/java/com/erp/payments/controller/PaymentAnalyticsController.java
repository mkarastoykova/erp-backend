package com.erp.payments.controller;

import com.erp.common.dto.ApiResponse;
import com.erp.common.dto.payments.PaymentMethodStatsDto;
import com.erp.common.dto.payments.PaymentTrendDto;
import com.erp.payments.service.PaymentTransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/analytics")
@RequiredArgsConstructor
public class PaymentAnalyticsController {

    private final PaymentTransactionService service;

    /**
     * Returns total amount and count per payment method (COMPLETED only).
     * Powers the "Volume by Payment Method" pie chart.
     */
    @GetMapping("/by-method")
    public ResponseEntity<ApiResponse<List<PaymentMethodStatsDto>>> byMethod() {
        return ResponseEntity.ok(ApiResponse.ok(service.getStatsByMethod()));
    }

    /**
     * Returns weekly received vs outstanding totals.
     * Powers the "Weekly Payments Received vs Outstanding" line chart.
     * @param weeks  Number of past weeks to include (default 7).
     */
    @GetMapping("/trend")
    public ResponseEntity<ApiResponse<List<PaymentTrendDto>>> trend(
            @RequestParam(defaultValue = "7") int weeks) {
        return ResponseEntity.ok(ApiResponse.ok(service.getWeeklyTrend(weeks)));
    }
}

