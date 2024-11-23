package com.server.hkj.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.domain.Persistable;

/**
 * A HkjJewelryModel.
 */
@Entity
@Table(name = "hkj_jewelry_model")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjJewelryModel extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "sku", nullable = false)
    private String sku;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 10000)
    @Column(name = "description", length = 10000)
    private String description;

    @Column(name = "cover_image")
    private String coverImage;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "is_cover_search")
    private Boolean isCoverSearch;

    @Column(name = "active")
    private Boolean active;

    @Column(name = "days_completed")
    private Integer daysCompleted;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @Transient
    private boolean isPersisted;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "jewelryModel")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "jewelryModel" }, allowSetters = true)
    private Set<HkjJewelryImage> images = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "jewelry")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "material", "jewelry", "task" }, allowSetters = true)
    private Set<HkjMaterialUsage> materials = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    private HkjCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tasks", "manager" }, allowSetters = true)
    private HkjProject project;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HkjJewelryModel id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSku() {
        return this.sku;
    }

    public HkjJewelryModel sku(String sku) {
        this.setSku(sku);
        return this;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getName() {
        return this.name;
    }

    public HkjJewelryModel name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public HkjJewelryModel description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCoverImage() {
        return this.coverImage;
    }

    public HkjJewelryModel coverImage(String coverImage) {
        this.setCoverImage(coverImage);
        return this;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public HkjJewelryModel price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public HkjJewelryModel isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public Boolean getIsCoverSearch() {
        return this.isCoverSearch;
    }

    public HkjJewelryModel isCoverSearch(Boolean isCoverSearch) {
        this.setIsCoverSearch(isCoverSearch);
        return this;
    }

    public void setIsCoverSearch(Boolean isCoverSearch) {
        this.isCoverSearch = isCoverSearch;
    }

    public Boolean getActive() {
        return this.active;
    }

    public HkjJewelryModel active(Boolean active) {
        this.setActive(active);
        return this;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Integer getDaysCompleted() {
        return this.daysCompleted;
    }

    public HkjJewelryModel daysCompleted(Integer daysCompleted) {
        this.setDaysCompleted(daysCompleted);
        return this;
    }

    public void setDaysCompleted(Integer daysCompleted) {
        this.daysCompleted = daysCompleted;
    }

    // Inherited createdBy methods
    public HkjJewelryModel createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public HkjJewelryModel createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public HkjJewelryModel lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public HkjJewelryModel lastModifiedDate(Instant lastModifiedDate) {
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

    public HkjJewelryModel setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Set<HkjJewelryImage> getImages() {
        return this.images;
    }

    public void setImages(Set<HkjJewelryImage> hkjJewelryImages) {
        if (this.images != null) {
            this.images.forEach(i -> i.setJewelryModel(null));
        }
        if (hkjJewelryImages != null) {
            hkjJewelryImages.forEach(i -> i.setJewelryModel(this));
        }
        this.images = hkjJewelryImages;
    }

    public HkjJewelryModel images(Set<HkjJewelryImage> hkjJewelryImages) {
        this.setImages(hkjJewelryImages);
        return this;
    }

    public HkjJewelryModel addImages(HkjJewelryImage hkjJewelryImage) {
        this.images.add(hkjJewelryImage);
        hkjJewelryImage.setJewelryModel(this);
        return this;
    }

    public HkjJewelryModel removeImages(HkjJewelryImage hkjJewelryImage) {
        this.images.remove(hkjJewelryImage);
        hkjJewelryImage.setJewelryModel(null);
        return this;
    }

    public Set<HkjMaterialUsage> getMaterials() {
        return this.materials;
    }

    public void setMaterials(Set<HkjMaterialUsage> hkjMaterialUsages) {
        if (this.materials != null) {
            this.materials.forEach(i -> i.setJewelry(null));
        }
        if (hkjMaterialUsages != null) {
            hkjMaterialUsages.forEach(i -> i.setJewelry(this));
        }
        this.materials = hkjMaterialUsages;
    }

    public HkjJewelryModel materials(Set<HkjMaterialUsage> hkjMaterialUsages) {
        this.setMaterials(hkjMaterialUsages);
        return this;
    }

    public HkjJewelryModel addMaterials(HkjMaterialUsage hkjMaterialUsage) {
        this.materials.add(hkjMaterialUsage);
        hkjMaterialUsage.setJewelry(this);
        return this;
    }

    public HkjJewelryModel removeMaterials(HkjMaterialUsage hkjMaterialUsage) {
        this.materials.remove(hkjMaterialUsage);
        hkjMaterialUsage.setJewelry(null);
        return this;
    }

    public HkjCategory getCategory() {
        return this.category;
    }

    public void setCategory(HkjCategory hkjCategory) {
        this.category = hkjCategory;
    }

    public HkjJewelryModel category(HkjCategory hkjCategory) {
        this.setCategory(hkjCategory);
        return this;
    }

    public HkjProject getProject() {
        return this.project;
    }

    public void setProject(HkjProject hkjProject) {
        this.project = hkjProject;
    }

    public HkjJewelryModel project(HkjProject hkjProject) {
        this.setProject(hkjProject);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjJewelryModel)) {
            return false;
        }
        return getId() != null && getId().equals(((HkjJewelryModel) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjJewelryModel{" +
            "id=" + getId() +
            ", sku='" + getSku() + "'" +
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", coverImage='" + getCoverImage() + "'" +
            ", price=" + getPrice() +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", isCoverSearch='" + getIsCoverSearch() + "'" +
            ", active='" + getActive() + "'" +
            ", daysCompleted=" + getDaysCompleted() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
