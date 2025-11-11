package com.goodfood.catalogue.api;

import com.goodfood.catalogue.dto.OptionDtos.*;
import com.goodfood.catalogue.repo.OptionRepository;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/restaurants/{restaurantId}/option-groups")
public class OptionGroupController {
    private final OptionRepository repo;
    public OptionGroupController(OptionRepository repo){ this.repo = repo; }

    @GetMapping public java.util.List<OptionGroupDto> list(@PathVariable long restaurantId){ return repo.listGroups(restaurantId); }
    @PostMapping public Map<String,Object> create(@RequestBody GroupUpsertReq req){ return Map.of("id", repo.createGroup(req)); }
    @PatchMapping("/{groupId}") public Map<String,Object> patch(@PathVariable long groupId, @RequestBody GroupUpsertReq req){ return Map.of("updated", repo.patchGroup(groupId, req)); }
    @DeleteMapping("/{groupId}") public Map<String,Object> delete(@PathVariable long groupId){ return Map.of("deleted", repo.deleteGroup(groupId)); }
}
