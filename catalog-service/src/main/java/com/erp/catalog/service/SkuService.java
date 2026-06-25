package com.erp.catalog.service;
import com.erp.common.entity.Sku;
import com.erp.catalog.repository.SkuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service
@Transactional
public class SkuService {
    private final SkuRepository skuRepository;
    public SkuService(SkuRepository skuRepository) {
        this.skuRepository = skuRepository;
    }
    public List<Sku> getAllSkus() {
        return skuRepository.findAll();
    }
    public Sku getSkuById(String id) {
        return skuRepository.findById(id).orElse(null);
    }
    public Sku createOrUpdateSku(Sku sku) {
        if (sku.getFields() != null) {
            sku.getFields().forEach(f -> f.setSku(sku));
        }
        if (sku.getRelations() != null) {
            sku.getRelations().forEach(r -> r.setSku(sku));
        }
        return skuRepository.save(sku);
    }
    public void deleteSku(String id) {
        skuRepository.deleteById(id);
    }
}
