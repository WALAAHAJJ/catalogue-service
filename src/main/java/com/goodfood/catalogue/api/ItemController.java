package com.goodfood.catalogue.api;

import com.goodfood.catalogue.dto.ItemDto;
import com.goodfood.catalogue.dto.ItemUpsertReq;
import com.goodfood.catalogue.repo.CatalogueRepository;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/restaurants/{restaurantId}/items")
@Validated
public class ItemController {
    private final CatalogueRepository repo;
    public ItemController(CatalogueRepository repo){ this.repo = repo; }

    // GET /restaurants/{rid}/items?categoryId=&page=&size=
    @GetMapping
    public List<ItemDto> list(@PathVariable long restaurantId,
                              @RequestParam(required=false) Long categoryId,
                              @RequestParam(defaultValue="0") int page,
                              @RequestParam(defaultValue="50") int size){
        return repo.listItemsPaged(restaurantId, categoryId, page, size);
    }

    // POST /restaurants/{rid}/items
    @PostMapping
    public Map<String,Object> create(@Valid @RequestBody ItemUpsertReq req){
        Long id = repo.createItem(req);
        return Map.of("id", id);
    }

    // PATCH /restaurants/{rid}/items/{itemId}
    @PatchMapping("/{itemId}")
    public Map<String,Object> patch(@PathVariable long itemId,
                                    @RequestBody ItemUpsertReq req){
        int updated = repo.patchItem(itemId, req);
        return Map.of("updated", updated);
    }

    // DELETE /restaurants/{rid}/items/{itemId}
    @DeleteMapping("/{itemId}")
    public Map<String,Object> delete(@PathVariable long itemId){
        return Map.of("deleted", repo.deleteItem(itemId));
    }
}
