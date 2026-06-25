package com.erp.catalog.controller;
import com.erp.common.entity.Sku;
import com.erp.catalog.service.SkuService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
@RestController
@RequestMapping("/api/skus")
public class SkuController {
    private final SkuService skuService;
    public SkuController(SkuService skuService) {
        this.skuService = skuService;
    }
    @GetMapping
    public List<Sku> getAllSkus() {
        return skuService.getAllSkus();
    }
    @GetMapping("/{id}")
    public ResponseEntity<Sku> getSkuById(@PathVariable String id) {
        Sku sku = skuService.getSkuById(id);
        if (sku == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(sku);
    }
    @PostMapping
    public Sku createSku(@RequestBody Sku sku) {
        return skuService.createOrUpdateSku(sku);
    }
    @PutMapping("/{id}")
    public Sku updateSku(@PathVariable String id, @RequestBody Sku sku) {
        sku.setId(id);
        return skuService.createOrUpdateSku(sku);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSku(@PathVariable String id) {
        skuService.deleteSku(id);
        return ResponseEntity.ok().build();
    }
}
