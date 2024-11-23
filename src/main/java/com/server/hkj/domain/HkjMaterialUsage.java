package com.server.hkj.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.domain.Persistable;

/**
 * A HkjMaterialUsage.
 */
@Entity
@Table(name = "hkj_material_usage")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjMaterialUsage extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "usage")
    private Float usage;

    @Column(name = "notes")
    private String notes;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @Transient
    private boolean isPersisted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "images" }, allowSetters = true)
    private HkjMaterial material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "images", "materials", "category", "project" }, allowSetters = true)
    private HkjJewelryModel jewelry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "images", "materials", "employee", "project" }, allowSetters = true)
    private HkjTask task;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HkjMaterialUsage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Float getUsage() {
        return this.usage;
    }

    public HkjMaterialUsage usage(Float usage) {
        this.setUsage(usage);
        return this;
    }

    public void setUsage(Float usage) {
        this.usage = usage;
    }

    public String getNotes() {
        return this.notes;
    }

    public HkjMaterialUsage notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public HkjMaterialUsage price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public HkjMaterialUsage isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    // Inherited createdBy methods
    public HkjMaterialUsage createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public HkjMaterialUsage createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public HkjMaterialUsage lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public HkjMaterialUsage lastModifiedDate(Instant lastModifiedDate) {
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

    public HkjMaterialUsage setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public HkjMaterial getMaterial() {
        return this.material;
    }

    public void setMaterial(HkjMaterial hkjMaterial) {
        this.material = hkjMaterial;
    }

    public HkjMaterialUsage material(HkjMaterial hkjMaterial) {
        this.setMaterial(hkjMaterial);
        return this;
    }

    public HkjJewelryModel getJewelry() {
        return this.jewelry;
    }

    public void setJewelry(HkjJewelryModel hkjJewelryModel) {
        this.jewelry = hkjJewelryModel;
    }

    public HkjMaterialUsage jewelry(HkjJewelryModel hkjJewelryModel) {
        this.setJewelry(hkjJewelryModel);
        return this;
    }

    public HkjTask getTask() {
        return this.task;
    }

    public void setTask(HkjTask hkjTask) {
        this.task = hkjTask;
    }

    public HkjMaterialUsage task(HkjTask hkjTask) {
        this.setTask(hkjTask);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjMaterialUsage)) {
            return false;
        }
        return getId() != null && getId().equals(((HkjMaterialUsage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjMaterialUsage{" +
            "id=" + getId() +
            ", usage=" + getUsage() +
            ", notes='" + getNotes() + "'" +
            ", price=" + getPrice() +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
