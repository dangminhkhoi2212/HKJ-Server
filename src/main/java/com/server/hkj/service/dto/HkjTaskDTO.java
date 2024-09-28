package com.server.hkj.service.dto;

import com.server.hkj.domain.enumeration.HkjOrderStatus;
import com.server.hkj.domain.enumeration.HkjPriority;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.server.hkj.domain.HkjTask} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjTaskDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    @Size(max = 1000)
    private String description;

    @NotNull
    private Instant assignedDate;

    @NotNull
    private Instant expectDate;

    private Instant completedDate;

    @NotNull
    private HkjOrderStatus status;

    @NotNull
    private HkjPriority priority;

    @Min(value = 0)
    @Max(value = 100)
    private Integer point;

    @Size(max = 1000)
    private String notes;

    private Boolean isDeleted;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private UserExtraDTO employee;

    private HkjProjectDTO hkjProject;

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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getAssignedDate() {
        return assignedDate;
    }

    public void setAssignedDate(Instant assignedDate) {
        this.assignedDate = assignedDate;
    }

    public Instant getExpectDate() {
        return expectDate;
    }

    public void setExpectDate(Instant expectDate) {
        this.expectDate = expectDate;
    }

    public Instant getCompletedDate() {
        return completedDate;
    }

    public void setCompletedDate(Instant completedDate) {
        this.completedDate = completedDate;
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

    public Integer getPoint() {
        return point;
    }

    public void setPoint(Integer point) {
        this.point = point;
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

    public UserExtraDTO getEmployee() {
        return employee;
    }

    public void setEmployee(UserExtraDTO employee) {
        this.employee = employee;
    }

    public HkjProjectDTO getHkjProject() {
        return hkjProject;
    }

    public void setHkjProject(HkjProjectDTO hkjProject) {
        this.hkjProject = hkjProject;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjTaskDTO)) {
            return false;
        }

        HkjTaskDTO hkjTaskDTO = (HkjTaskDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hkjTaskDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjTaskDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", assignedDate='" + getAssignedDate() + "'" +
            ", expectDate='" + getExpectDate() + "'" +
            ", completedDate='" + getCompletedDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", priority='" + getPriority() + "'" +
            ", point=" + getPoint() +
            ", notes='" + getNotes() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", employee=" + getEmployee() +
            ", hkjProject=" + getHkjProject() +
            "}";
    }
}
