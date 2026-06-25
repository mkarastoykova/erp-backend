package com.erp.sales.controller;

import com.erp.common.entity.Quote;
import com.erp.sales.repository.QuoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales/quotes")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class QuoteController {

    private final QuoteRepository quoteRepository;

    @GetMapping
    public List<Quote> getAllQuotes() {
        return quoteRepository.findAll();
    }

    @PostMapping
    public ResponseEntity<Quote> createQuote(@RequestBody Quote quote) {
        return ResponseEntity.ok(quoteRepository.save(quote));
    }
}

