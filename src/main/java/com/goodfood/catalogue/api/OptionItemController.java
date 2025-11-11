package com.goodfood.catalogue.api;

import com.goodfood.catalogue.dto.OptionDtos.*;
import com.goodfood.catalogue.repo.OptionRepository;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/option-groups/{groupId}/options")
public class OptionItemController {
    private final OptionRepository repo;
    public OptionItemController(OptionRepository repo){ this.repo = repo; }

    @GetMapping public java.util.List<OptionItemDto> list(@PathVariable long groupId){ return repo.listOptions(groupId); }
    @PostMapping public Map<String,Object> create(@RequestBody OptionUpsertReq req){ return Map.of("id", repo.createOption(req)); }
    @PatchMapping("/{optionId}") public Map<String,Object> patch(@PathVariable long optionId, @RequestBody OptionUpsertReq req){ return Map.of("updated", repo.patchOption(optionId, req)); }
    @DeleteMapping("/{optionId}") public Map<String,Object> delete(@PathVariable long optionId){ return Map.of("deleted", repo.deleteOption(optionId)); }
}
