package com.server.hkj.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.server.hkj.domain.HkjMaterialUsage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjMaterialUsageDTO implements Serializable {

    private Long id;

    private Float usage;

    private String notes;

    private BigDecimal price;

    private Boolean isDeleted;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private HkjMaterialDTO material;

    private HkjJewelryModelDTO jewelry;

    private HkjTaskDTO task;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getUsage() {
        return usage;
    }

    public void setUsage(Float usage) {
        this.usage = usage;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
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

    public HkjMaterialDTO getMaterial() {
        return material;
    }

    public void setMaterial(HkjMaterialDTO material) {
        this.material = material;
    }

    public HkjJewelryModelDTO getJewelry() {
        return jewelry;
    }

    public void setJewelry(HkjJewelryModelDTO jewelry) {
        this.jewelry = jewelry;
    }

    public HkjTaskDTO getTask() {
        return task;
    }

    public void setTask(HkjTaskDTO task) {
        this.task = task;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjMaterialUsageDTO)) {
            return false;
        }

        HkjMaterialUsageDTO hkjMaterialUsageDTO = (HkjMaterialUsageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hkjMaterialUsageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjMaterialUsageDTO{" +
            "id=" + getId() +
            ", usage=" + getUsage() +
            ", notes='" + getNotes() + "'" +
            ", price=" + getPrice() +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", material=" + getMaterial() +
            ", jewelry=" + getJewelry() +
            ", task=" + getTask() +
            "}";
    }
}
