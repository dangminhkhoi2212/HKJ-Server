package com.server.hkj.service.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.server.hkj.domain.HkjMaterial} entity.
 */

@SuppressWarnings("common-java:DuplicatedBlocks")
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

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Float getPricePerUnit() {
        return pricePerUnit;
    }

    public void setPricePerUnit(Float pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    public Boolean getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Instant getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Instant createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Instant getLastModifiedDate() {
        return lastModifiedDate;
    }

    public void setLastModifiedDate(Instant lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HkjMaterialDTO that = (HkjMaterialDTO) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(coverImage, that.coverImage) &&
            Objects.equals(unit, that.unit) &&
            Objects.equals(pricePerUnit, that.pricePerUnit) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, coverImage, unit, pricePerUnit, isDeleted, createdBy, createdDate, lastModifiedBy, lastModifiedDate);
    }

    @Override
    public String toString() {
        return (
            "HkjMaterialDTO [id=" +
            id +
            ", name=" +
            name +
            ", coverImage=" +
            coverImage +
            ", unit=" +
            unit +
            ", pricePerUnit=" +
            pricePerUnit +
            ", isDeleted=" +
            isDeleted +
            ", createdBy=" +
            createdBy +
            ", createdDate=" +
            createdDate +
            ", lastModifiedBy=" +
            lastModifiedBy +
            ", lastModifiedDate=" +
            lastModifiedDate +
            "]"
        );
    }
}
