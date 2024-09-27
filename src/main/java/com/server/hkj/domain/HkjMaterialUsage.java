package com.server.hkj.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
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

    @NotNull
    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "loss_quantity")
    private Integer lossQuantity;

    @NotNull
    @Column(name = "usage_date", nullable = false)
    private Instant usageDate;

    @Column(name = "notes")
    private String notes;

    @Column(name = "weight")
    private Float weight;

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

    @JsonIgnoreProperties(value = { "hkjMaterialUsage" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private HkjMaterial material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "employee", "images", "materials", "project" }, allowSetters = true)
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

    public Integer getQuantity() {
        return this.quantity;
    }

    public HkjMaterialUsage quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Integer getLossQuantity() {
        return this.lossQuantity;
    }

    public HkjMaterialUsage lossQuantity(Integer lossQuantity) {
        this.setLossQuantity(lossQuantity);
        return this;
    }

    public void setLossQuantity(Integer lossQuantity) {
        this.lossQuantity = lossQuantity;
    }

    public Instant getUsageDate() {
        return this.usageDate;
    }

    public HkjMaterialUsage usageDate(Instant usageDate) {
        this.setUsageDate(usageDate);
        return this;
    }

    public void setUsageDate(Instant usageDate) {
        this.usageDate = usageDate;
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

    public Float getWeight() {
        return this.weight;
    }

    public HkjMaterialUsage weight(Float weight) {
        this.setWeight(weight);
        return this;
    }

    public void setWeight(Float weight) {
        this.weight = weight;
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
            ", quantity=" + getQuantity() +
            ", lossQuantity=" + getLossQuantity() +
            ", usageDate='" + getUsageDate() + "'" +
            ", notes='" + getNotes() + "'" +
            ", weight=" + getWeight() +
            ", price=" + getPrice() +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
