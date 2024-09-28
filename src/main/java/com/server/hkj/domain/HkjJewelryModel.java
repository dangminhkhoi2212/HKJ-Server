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
    @Column(name = "name", nullable = false)
    private String name;

    @Size(max = 1000)
    @Column(name = "description", length = 1000)
    private String description;

    @Column(name = "is_custom")
    private Boolean isCustom;

    @Column(name = "weight")
    private Double weight;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @Column(name = "color")
    private String color;

    @Column(name = "notes")
    private String notes;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Column(name = "active")
    private Boolean active;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @Transient
    private boolean isPersisted;

    @JsonIgnoreProperties(value = { "category", "tasks", "manager", "jewelry", "hkjOrder" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private HkjProject project;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "jewelryModel")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "jewelryModel" }, allowSetters = true)
    private Set<HkjJewelryImage> images = new HashSet<>();

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

    public Boolean getIsCustom() {
        return this.isCustom;
    }

    public HkjJewelryModel isCustom(Boolean isCustom) {
        this.setIsCustom(isCustom);
        return this;
    }

    public void setIsCustom(Boolean isCustom) {
        this.isCustom = isCustom;
    }

    public Double getWeight() {
        return this.weight;
    }

    public HkjJewelryModel weight(Double weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Double weight) {
        this.weight = weight;
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

    public String getColor() {
        return this.color;
    }

    public HkjJewelryModel color(String color) {
        this.setColor(color);
        return this;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getNotes() {
        return this.notes;
    }

    public HkjJewelryModel notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
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
            ", name='" + getName() + "'" +
            ", description='" + getDescription() + "'" +
            ", isCustom='" + getIsCustom() + "'" +
            ", weight=" + getWeight() +
            ", price=" + getPrice() +
            ", color='" + getColor() + "'" +
            ", notes='" + getNotes() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", active='" + getActive() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
