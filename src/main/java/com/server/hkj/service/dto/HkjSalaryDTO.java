package com.server.hkj.service.dto;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.server.hkj.domain.HkjSalary} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjSalaryDTO implements Serializable {

    private Long id;

    private BigDecimal salary;

    private Boolean isDeleted;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private HkjEmployeeDTO hkjEmployee;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
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

    public HkjEmployeeDTO getHkjEmployee() {
        return hkjEmployee;
    }

    public void setHkjEmployee(HkjEmployeeDTO hkjEmployee) {
        this.hkjEmployee = hkjEmployee;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjSalaryDTO)) {
            return false;
        }

        HkjSalaryDTO hkjSalaryDTO = (HkjSalaryDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hkjSalaryDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjSalaryDTO{" +
            "id=" + getId() +
            ", salary=" + getSalary() +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", hkjEmployee=" + getHkjEmployee() +
            "}";
    }
}
