package com.goodfood.catalogue.api;

import com.goodfood.catalogue.repo.RestaurantRepository;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/stocks")
public class StockController {
    private final RestaurantRepository repo;
    public StockController(RestaurantRepository repo) { this.repo = repo; }

    public record AdjustReq(String sku, int delta) {}

    @PostMapping("/adjust")
    public Map<String, Object> adjust(@RequestBody AdjustReq req) {
        int updated = repo.adjustStock(req.sku(), req.delta());
        return Map.of("updated", updated);
    }
}
