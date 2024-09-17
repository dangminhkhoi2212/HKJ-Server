package com.server.hkj.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.server.hkj.domain.HkjTemplateStep} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjTemplateStepDTO implements Serializable {

    private Long id;

    private String name;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private HkjTemplateDTO hkjTemplate;

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

    public HkjTemplateDTO getHkjTemplate() {
        return hkjTemplate;
    }

    public void setHkjTemplate(HkjTemplateDTO hkjTemplate) {
        this.hkjTemplate = hkjTemplate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjTemplateStepDTO)) {
            return false;
        }

        HkjTemplateStepDTO hkjTemplateStepDTO = (HkjTemplateStepDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hkjTemplateStepDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjTemplateStepDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", hkjTemplate=" + getHkjTemplate() +
            "}";
    }
}
