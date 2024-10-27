package com.server.hkj.service.dto;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * A DTO for the {@link com.server.hkj.domain.HkjJewelryImage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
@Data
@AllArgsConstructor
public class HkjJewelryImageDTO implements Serializable {

    private Long id;

    @NotNull
    private String url;

    @NotNull
    private Boolean isSearchImage;

    private String description;

    private String tags;

    private Boolean isDeleted;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

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
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", jewelryModel=" + getJewelryModel() +
            "}";
    }
}
