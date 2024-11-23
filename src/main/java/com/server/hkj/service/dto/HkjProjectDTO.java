package com.server.hkj.service.dto;

import com.server.hkj.domain.enumeration.HkjOrderStatus;
import com.server.hkj.domain.enumeration.HkjPriority;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.server.hkj.domain.HkjProject} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjProjectDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String coverImage;

    @Size(max = 10000)
    private String description;

    @NotNull
    private Instant startDate;

    private Instant expectDate;

    private Instant endDate;

    @NotNull
    private HkjOrderStatus status;

    @NotNull
    private HkjPriority priority;

    private Boolean isDeleted;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private UserExtraDTO manager;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getExpectDate() {
        return expectDate;
    }

    public void setExpectDate(Instant expectDate) {
        this.expectDate = expectDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public HkjOrderStatus getStatus() {
        return status;
    }

    public void setStatus(HkjOrderStatus status) {
        this.status = status;
    }

    public HkjPriority getPriority() {
        return priority;
    }

    public void setPriority(HkjPriority priority) {
        this.priority = priority;
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

    public UserExtraDTO getManager() {
        return manager;
    }

    public void setManager(UserExtraDTO manager) {
        this.manager = manager;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjProjectDTO)) {
            return false;
        }

        HkjProjectDTO hkjProjectDTO = (HkjProjectDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hkjProjectDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjProjectDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", coverImage='" + getCoverImage() + "'" +
            ", description='" + getDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", expectDate='" + getExpectDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", priority='" + getPriority() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", manager=" + getManager() +
            "}";
    }
}
