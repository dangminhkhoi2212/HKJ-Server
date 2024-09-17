package com.server.hkj.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.server.hkj.domain.HkjEmployee} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjEmployeeDTO implements Serializable {

    private Long id;

    @Size(max = 1000)
    private String notes;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private UserExtraDTO userExtra;

    private HkjHireDTO hkjHire;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public UserExtraDTO getUserExtra() {
        return userExtra;
    }

    public void setUserExtra(UserExtraDTO userExtra) {
        this.userExtra = userExtra;
    }

    public HkjHireDTO getHkjHire() {
        return hkjHire;
    }

    public void setHkjHire(HkjHireDTO hkjHire) {
        this.hkjHire = hkjHire;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjEmployeeDTO)) {
            return false;
        }

        HkjEmployeeDTO hkjEmployeeDTO = (HkjEmployeeDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hkjEmployeeDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjEmployeeDTO{" +
            "id=" + getId() +
            ", notes='" + getNotes() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", userExtra=" + getUserExtra() +
            ", hkjHire=" + getHkjHire() +
            "}";
    }
}
