package com.goodfood.catalogue.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ItemUpsertReq(
        @NotNull Long restaurantId,
        @NotNull Long categoryId,
        String sku,
        @NotBlank String name,
        String description,
        @NotNull @Min(0) Integer priceCents,
        String currency,
        Boolean isAvailable,
        @Min(1) Integer maxPerOrder
) {}
