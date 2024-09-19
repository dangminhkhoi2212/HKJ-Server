package com.server.hkj.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.server.hkj.domain.HkjHire} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjHireDTO implements Serializable {

    private Long id;

    @NotNull
    private Instant beginDate;

    @NotNull
    private Instant endDate;

    private BigDecimal beginSalary;

    private Boolean isDeleted;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private HkjPositionDTO position;

    private HkjEmployeeDTO employee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getBeginDate() {
        return beginDate;
    }

    public void setBeginDate(Instant beginDate) {
        this.beginDate = beginDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getBeginSalary() {
        return beginSalary;
    }

    public void setBeginSalary(BigDecimal beginSalary) {
        this.beginSalary = beginSalary;
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

    public HkjPositionDTO getPosition() {
        return position;
    }

    public void setPosition(HkjPositionDTO position) {
        this.position = position;
    }

    public HkjEmployeeDTO getEmployee() {
        return employee;
    }

    public void setEmployee(HkjEmployeeDTO employee) {
        this.employee = employee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjHireDTO)) {
            return false;
        }

        HkjHireDTO hkjHireDTO = (HkjHireDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hkjHireDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjHireDTO{" +
            "id=" + getId() +
            ", beginDate='" + getBeginDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", beginSalary=" + getBeginSalary() +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", position=" + getPosition() +
            ", employee=" + getEmployee() +
            "}";
    }
}
