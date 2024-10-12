package com.server.hkj.service.dto;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link com.server.hkj.domain.HkjTrackSearchImage} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjTrackSearchImageDTO implements Serializable {

    private Long id;

    private Integer searchOrder;

    private String createdBy;

    private Instant createdDate;

    private String lastModifiedBy;

    private Instant lastModifiedDate;

    private UserExtraDTO user;

    private HkjJewelryModelDTO jewelry;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSearchOrder() {
        return searchOrder;
    }

    public void setSearchOrder(Integer searchOrder) {
        this.searchOrder = searchOrder;
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

    public UserExtraDTO getUser() {
        return user;
    }

    public void setUser(UserExtraDTO user) {
        this.user = user;
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
        if (!(o instanceof HkjTrackSearchImageDTO)) {
            return false;
        }

        HkjTrackSearchImageDTO hkjTrackSearchImageDTO = (HkjTrackSearchImageDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, hkjTrackSearchImageDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjTrackSearchImageDTO{" +
            "id=" + getId() +
            ", searchOrder=" + getSearchOrder() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            ", user=" + getUser() +
            ", jewelry=" + getJewelry() +
            "}";
    }
}
