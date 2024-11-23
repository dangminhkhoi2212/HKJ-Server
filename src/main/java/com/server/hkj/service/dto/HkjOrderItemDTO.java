package com.server.hkj.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.server.hkj.domain.HkjOrderItem} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjOrderItemDTO implements Serializable {

    private Long id;

    @Min(value = 1)
    @Max(value = 20)
    private Integer quantity;

    @Size(max = 5000)
    private String specialRequests;

    private BigDecimal price;

    private Boolean isDeleted;

    @Size(max = 5000)
    private String notes;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private HkjMaterialDTO material;

    private HkjOrderDTO order;

    private HkjJewelryModelDTO product;

    private HkjCategoryDTO category;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
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

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public HkjOrderDTO getOrder() {
        return order;
    }

    public void setOrder(HkjOrderDTO order) {
        this.order = order;
    }

    public HkjJewelryModelDTO getProduct() {
        return product;
    }

    public void setProduct(HkjJewelryModelDTO product) {
        this.product = product;
    }

    public HkjCategoryDTO getCategory() {
        return category;
    }

    public void setCategory(HkjCategoryDTO category) {
        this.category = category;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjOrderItemDTO)) {
            return false;
        }

        HkjOrderItemDTO hkjOrderItemDTO = (HkjOrderItemDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hkjOrderItemDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjOrderItemDTO{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", specialRequests='" + getSpecialRequests() + "'" +
            ", price=" + getPrice() +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", notes='" + getNotes() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", material=" + getMaterial() +
            ", order=" + getOrder() +
            ", product=" + getProduct() +
            ", category=" + getCategory() +
            "}";
    }
}
