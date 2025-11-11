package com.goodfood.catalogue.api;

import com.goodfood.catalogue.dto.RestaurantDto;
import com.goodfood.catalogue.repo.RestaurantRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
    private final RestaurantRepository repo;
    public RestaurantController(RestaurantRepository repo) { this.repo = repo; }

    // GET /restaurants?q=&openNow=&page=&size=&sort=
    @GetMapping
    public List<RestaurantDto> search(@RequestParam(required = false) String q,
                                      @RequestParam(required = false) Boolean openNow,
                                      @RequestParam(defaultValue = "0") int page,
                                      @RequestParam(defaultValue = "20") int size,
                                      @RequestParam(defaultValue = "rating_desc") String sort) {
        return repo.searchPaged(q, openNow, page, size, sort);
    }

    // GET /restaurants/{id}/menu
    @GetMapping("/{id}/menu")
    public ResponseEntity<?> menu(@PathVariable long id) {
        String json = repo.menuJson(id);
        return ResponseEntity.ok(Map.of("menu", json == null ? "[]" : json));
    }
}
