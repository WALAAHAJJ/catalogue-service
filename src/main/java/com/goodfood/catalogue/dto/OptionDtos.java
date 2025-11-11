package com.goodfood.catalogue.dto;

public class OptionDtos {
    public record OptionGroupDto(Long id, Long restaurantId, String name, Integer minSelect, Integer maxSelect, Boolean required) {}
    public record OptionItemDto(Long id, Long groupId, String name, Integer priceDeltaCents, Boolean isAvailable) {}
    public record GroupUpsertReq(Long restaurantId, String name, Integer minSelect, Integer maxSelect, Boolean required) {}
    public record OptionUpsertReq(Long groupId, String name, Integer priceDeltaCents, Boolean isAvailable) {}
}
