package com.server.hkj.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.server.hkj.domain.HkjJewelryImage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjJewelryImageDTO implements Serializable {

    private Long id;

    @NotNull
    private String url;

    @NotNull
    private Boolean isSearchImage;

    private String description;

    private String tags;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private HkjEmployeeDTO uploadedBy;

    private HkjJewelryModelDTO jewelryModel;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIsSearchImage() {
        return isSearchImage;
    }

    public void setIsSearchImage(Boolean isSearchImage) {
        this.isSearchImage = isSearchImage;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
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

    public HkjEmployeeDTO getUploadedBy() {
        return uploadedBy;
    }

    public void setUploadedBy(HkjEmployeeDTO uploadedBy) {
        this.uploadedBy = uploadedBy;
    }

    public HkjJewelryModelDTO getJewelryModel() {
        return jewelryModel;
    }

    public void setJewelryModel(HkjJewelryModelDTO jewelryModel) {
        this.jewelryModel = jewelryModel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjJewelryImageDTO)) {
            return false;
        }

        HkjJewelryImageDTO hkjJewelryImageDTO = (HkjJewelryImageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hkjJewelryImageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjJewelryImageDTO{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", isSearchImage='" + getIsSearchImage() + "'" +
            ", description='" + getDescription() + "'" +
            ", tags='" + getTags() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", uploadedBy=" + getUploadedBy() +
            ", jewelryModel=" + getJewelryModel() +
            "}";
    }
}
