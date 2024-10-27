package com.server.hkj.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JewelryImageDTO {

    private Long id;

    @NotNull
    @NotNull
    private String name;

    @Size(max = 10000)
    private String description;

    private String coverImage;
    private Boolean isCustom;
    private Double weight;
    private BigDecimal price;
    private String color;
    private String notes;
    private Boolean isDeleted;

    private Boolean active;
    private String createdBy;
    private Instant createdDate;
    private String lastModifiedBy;
    private Instant lastModifiedDate;
    private HkjCategoryDTO category;
    private HkjProjectDTO project;
    private Boolean isCoverSearch;
    private String sku;
    private Set<HkjJewelryImageDTO> images;
}
