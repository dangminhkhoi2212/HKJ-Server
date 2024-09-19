package com.server.hkj.service.dto;

import com.server.hkj.domain.enumeration.HkjOrderStatus;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.server.hkj.domain.HkjOrder} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjOrderDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant orderDate;

    private Instant expectedDeliveryDate;

    private Instant actualDeliveryDate;

    @Size(max = 1000)
    private String specialRequests;

    @NotNull
    private HkjOrderStatus status;

    @Min(value = 1)
    @Max(value = 5)
    private Integer customerRating;

    private BigDecimal totalPrice;

    private BigDecimal depositAmount;

    @Size(max = 1000)
    private String notes;

    private Boolean isDeleted;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private HkjProjectDTO project;

    private UserExtraDTO customer;

    private HkjJewelryModelDTO jewelry;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Instant getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(Instant expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public Instant getActualDeliveryDate() {
        return actualDeliveryDate;
    }

    public void setActualDeliveryDate(Instant actualDeliveryDate) {
        this.actualDeliveryDate = actualDeliveryDate;
    }

    public String getSpecialRequests() {
        return specialRequests;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public HkjOrderStatus getStatus() {
        return status;
    }

    public void setStatus(HkjOrderStatus status) {
        this.status = status;
    }

    public Integer getCustomerRating() {
        return customerRating;
    }

    public void setCustomerRating(Integer customerRating) {
        this.customerRating = customerRating;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public BigDecimal getDepositAmount() {
        return depositAmount;
    }

    public void setDepositAmount(BigDecimal depositAmount) {
        this.depositAmount = depositAmount;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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

    public HkjProjectDTO getProject() {
        return project;
    }

    public void setProject(HkjProjectDTO project) {
        this.project = project;
    }

    public UserExtraDTO getCustomer() {
        return customer;
    }

    public void setCustomer(UserExtraDTO customer) {
        this.customer = customer;
    }

    public HkjJewelryModelDTO getJewelry() {
        return jewelry;
    }

    public void setJewelry(HkjJewelryModelDTO jewelry) {
        this.jewelry = jewelry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjOrderDTO)) {
            return false;
        }

        HkjOrderDTO hkjOrderDTO = (HkjOrderDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hkjOrderDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjOrderDTO{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", expectedDeliveryDate='" + getExpectedDeliveryDate() + "'" +
            ", actualDeliveryDate='" + getActualDeliveryDate() + "'" +
            ", specialRequests='" + getSpecialRequests() + "'" +
            ", status='" + getStatus() + "'" +
            ", customerRating=" + getCustomerRating() +
            ", totalPrice=" + getTotalPrice() +
            ", depositAmount=" + getDepositAmount() +
            ", notes='" + getNotes() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", project=" + getProject() +
            ", customer=" + getCustomer() +
            ", jewelry=" + getJewelry() +
            "}";
    }
}
