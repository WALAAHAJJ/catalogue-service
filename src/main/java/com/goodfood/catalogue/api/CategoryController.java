package com.goodfood.catalogue.api;

import com.goodfood.catalogue.dto.CategoryDto;
import com.goodfood.catalogue.repo.CatalogueRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurants/{restaurantId}/categories")
public class CategoryController {
    private final CatalogueRepository repo;
    public CategoryController(CatalogueRepository repo){ this.repo = repo; }

    @GetMapping
    public List<CategoryDto> list(@PathVariable long restaurantId){
        return repo.listCategories(restaurantId);
    }

    record CategoryUpsertReq(String name, Integer sortOrder, Boolean isActive) {}

    @PostMapping
    public Map<String,Object> create(@PathVariable long restaurantId,
                                     @RequestBody CategoryUpsertReq req){
        Long id = repo.createCategory(restaurantId, req.name(), req.sortOrder());
        return Map.of("id", id);
    }

    @PatchMapping("/{categoryId}")
    public Map<String,Object> patch(@PathVariable long categoryId,
                                    @RequestBody CategoryUpsertReq req){
        int updated = repo.patchCategory(categoryId, req.name(), req.sortOrder(), req.isActive());
        return Map.of("updated", updated);
    }

    @DeleteMapping("/{categoryId}")
    public Map<String,Object> delete(@PathVariable long categoryId){
        return Map.of("deleted", repo.deleteCategory(categoryId));
    }
}
