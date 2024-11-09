package com.server.hkj.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.domain.Persistable;

/**
 * A HkjCart.
 */
@Entity
@Table(name = "hkj_cart")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjCart extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @Transient
    private boolean isPersisted;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hkjCart")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "images", "materials", "category", "project", "material", "hkjCart" }, allowSetters = true)
    private Set<HkjJewelryModel> products = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "salarys" }, allowSetters = true)
    private UserExtra customer;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HkjCart id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getQuantity() {
        return this.quantity;
    }

    public HkjCart quantity(Integer quantity) {
        this.setQuantity(quantity);
        return this;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public HkjCart isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    // Inherited createdBy methods
    public HkjCart createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public HkjCart createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public HkjCart lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public HkjCart lastModifiedDate(Instant lastModifiedDate) {
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

    public HkjCart setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Set<HkjJewelryModel> getProducts() {
        return this.products;
    }

    public void setProducts(Set<HkjJewelryModel> hkjJewelryModels) {
        if (this.products != null) {
            this.products.forEach(i -> i.setHkjCart(null));
        }
        if (hkjJewelryModels != null) {
            hkjJewelryModels.forEach(i -> i.setHkjCart(this));
        }
        this.products = hkjJewelryModels;
    }

    public HkjCart products(Set<HkjJewelryModel> hkjJewelryModels) {
        this.setProducts(hkjJewelryModels);
        return this;
    }

    public HkjCart addProduct(HkjJewelryModel hkjJewelryModel) {
        this.products.add(hkjJewelryModel);
        hkjJewelryModel.setHkjCart(this);
        return this;
    }

    public HkjCart removeProduct(HkjJewelryModel hkjJewelryModel) {
        this.products.remove(hkjJewelryModel);
        hkjJewelryModel.setHkjCart(null);
        return this;
    }

    public UserExtra getCustomer() {
        return this.customer;
    }

    public void setCustomer(UserExtra userExtra) {
        this.customer = userExtra;
    }

    public HkjCart customer(UserExtra userExtra) {
        this.setCustomer(userExtra);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjCart)) {
            return false;
        }
        return getId() != null && getId().equals(((HkjCart) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjCart{" +
            "id=" + getId() +
            ", quantity=" + getQuantity() +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
