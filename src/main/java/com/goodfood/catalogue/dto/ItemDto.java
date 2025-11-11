package com.goodfood.catalogue.dto;
public record ItemDto(Long id, Long restaurantId, Long categoryId, String sku,
                      String name, String description, Integer priceCents,
                      String currency, Boolean isAvailable, Integer maxPerOrder) {}
