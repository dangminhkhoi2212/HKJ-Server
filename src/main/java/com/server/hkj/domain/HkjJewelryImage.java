package com.server.hkj.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.domain.Persistable;

/**
 * A HkjJewelryImage.
 */
@Entity
@Table(name = "hkj_jewelry_image")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjJewelryImage extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "url", nullable = false)
    private String url;

    @NotNull
    @Column(name = "is_search_image", nullable = false)
    private Boolean isSearchImage;

    @Column(name = "description")
    private String description;

    @Column(name = "tags")
    private String tags;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @Transient
    private boolean isPersisted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userExtra", "salarys", "hkjHire" }, allowSetters = true)
    private HkjEmployee uploadedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "images", "hkjOrder" }, allowSetters = true)
    private HkjJewelryModel jewelryModel;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HkjJewelryImage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUrl() {
        return this.url;
    }

    public HkjJewelryImage url(String url) {
        this.setUrl(url);
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Boolean getIsSearchImage() {
        return this.isSearchImage;
    }

    public HkjJewelryImage isSearchImage(Boolean isSearchImage) {
        this.setIsSearchImage(isSearchImage);
        return this;
    }

    public void setIsSearchImage(Boolean isSearchImage) {
        this.isSearchImage = isSearchImage;
    }

    public String getDescription() {
        return this.description;
    }

    public HkjJewelryImage description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return this.tags;
    }

    public HkjJewelryImage tags(String tags) {
        this.setTags(tags);
        return this;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    // Inherited createdBy methods
    public HkjJewelryImage createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public HkjJewelryImage createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public HkjJewelryImage lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public HkjJewelryImage lastModifiedDate(Instant lastModifiedDate) {
        this.setLastModifiedDate(lastModifiedDate);
        return this;
    }

    @PostLoad
    @PostPersist
    public void updateEntityState() {
        this.setIsPersisted();
    }

    @Transient
    @Override
    public boolean isNew() {
        return !this.isPersisted;
    }

    public HkjJewelryImage setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public HkjEmployee getUploadedBy() {
        return this.uploadedBy;
    }

    public void setUploadedBy(HkjEmployee hkjEmployee) {
        this.uploadedBy = hkjEmployee;
    }

    public HkjJewelryImage uploadedBy(HkjEmployee hkjEmployee) {
        this.setUploadedBy(hkjEmployee);
        return this;
    }

    public HkjJewelryModel getJewelryModel() {
        return this.jewelryModel;
    }

    public void setJewelryModel(HkjJewelryModel hkjJewelryModel) {
        this.jewelryModel = hkjJewelryModel;
    }

    public HkjJewelryImage jewelryModel(HkjJewelryModel hkjJewelryModel) {
        this.setJewelryModel(hkjJewelryModel);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjJewelryImage)) {
            return false;
        }
        return getId() != null && getId().equals(((HkjJewelryImage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjJewelryImage{" +
            "id=" + getId() +
            ", url='" + getUrl() + "'" +
            ", isSearchImage='" + getIsSearchImage() + "'" +
            ", description='" + getDescription() + "'" +
            ", tags='" + getTags() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
