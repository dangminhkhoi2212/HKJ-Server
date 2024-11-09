package com.server.hkj.service.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import lombok.Data;
import lombok.ToString;

/**
 * A DTO for the {@link com.server.hkj.domain.HkjMaterial} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@ToString
public class HkjMaterialDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String coverImage;
    private String unit;
    private Float pricePerUnit;

    private Boolean isDeleted;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;
}
