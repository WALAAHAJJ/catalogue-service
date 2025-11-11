package com.goodfood.catalogue.dto;
public record CategoryDto(Long id, Long restaurantId, String name, Integer sortOrder, Boolean isActive) {}
