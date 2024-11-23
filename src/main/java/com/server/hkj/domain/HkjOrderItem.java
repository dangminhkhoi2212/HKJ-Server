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
 * A HkjOrderItem.
 */
@Entity
@Table(name = "hkj_order_item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjOrderItem extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Min(value = 1)
    @Max(value = 20)
    @Column(name = "quantity")
    private Integer quantity;

    @Size(max = 5000)
    @Column(name = "special_requests", length = 5000)
    private String specialRequests;

    @Column(name = "price", precision = 21, scale = 2)
    private BigDecimal price;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    @Size(max = 5000)
    @Column(name = "notes", length = 5000)
    private String notes;

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
    @JsonIgnoreProperties(value = { "customer", "project" }, allowSetters = true)
    private HkjOrder order;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "images", "materials", "category", "project" }, allowSetters = true)
    private HkjJewelryModel product;

    @ManyToOne(fetch = FetchType.LAZY)
    private HkjCategory category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HkjOrderItem id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public HkjOrderItem quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getSpecialRequests() {
        return this.specialRequests;
    }

    public HkjOrderItem specialRequests(String specialRequests) {
        this.setSpecialRequests(specialRequests);
        return this;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public BigDecimal getPrice() {
        return this.price;
    }

    public HkjOrderItem price(BigDecimal price) {
        this.setPrice(price);
        return this;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public HkjOrderItem isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getNotes() {
        return this.notes;
    }

    public HkjOrderItem notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    // Inherited createdBy methods
    public HkjOrderItem createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public HkjOrderItem createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public HkjOrderItem lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public HkjOrderItem lastModifiedDate(Instant lastModifiedDate) {
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

    public HkjOrderItem setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public HkjMaterial getMaterial() {
        return this.material;
    }

    public void setMaterial(HkjMaterial hkjMaterial) {
        this.material = hkjMaterial;
    }

    public HkjOrderItem material(HkjMaterial hkjMaterial) {
        this.setMaterial(hkjMaterial);
        return this;
    }

    public HkjOrder getOrder() {
        return this.order;
    }

    public void setOrder(HkjOrder hkjOrder) {
        this.order = hkjOrder;
    }

    public HkjOrderItem order(HkjOrder hkjOrder) {
        this.setOrder(hkjOrder);
        return this;
    }

    public HkjJewelryModel getProduct() {
        return this.product;
    }

    public void setProduct(HkjJewelryModel hkjJewelryModel) {
        this.product = hkjJewelryModel;
    }

    public HkjOrderItem product(HkjJewelryModel hkjJewelryModel) {
        this.setProduct(hkjJewelryModel);
        return this;
    }

    public HkjCategory getCategory() {
        return this.category;
    }

    public void setCategory(HkjCategory hkjCategory) {
        this.category = hkjCategory;
    }

    public HkjOrderItem category(HkjCategory hkjCategory) {
        this.setCategory(hkjCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjOrderItem)) {
            return false;
        }
        return getId() != null && getId().equals(((HkjOrderItem) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjOrderItem{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", specialRequests='" + getSpecialRequests() + "'" +
            ", price=" + getPrice() +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", notes='" + getNotes() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
