package com.server.hkj.service.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * A DTO for the {@link com.server.hkj.domain.HkjJewelryModel} entity.
 */
@Setter
@Getter
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjJewelryModelDTO implements Serializable {

    private Long id;

    @NotNull
    private String sku;

    @NotNull
    private String name;

    @Size(max = 10000)
    private String description;

    private String coverImage;

    private BigDecimal price;

    private Boolean isDeleted;

    private Boolean isCoverSearch;

    private Boolean active;

    private Integer daysCompleted;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private HkjCategoryDTO category;

    private HkjProjectDTO project;

    private HkjCartDTO hkjCart;
}
