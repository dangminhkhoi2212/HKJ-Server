package com.server.hkj.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.server.hkj.domain.enumeration.HkjOrderStatus;
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
 * A HkjOrder.
 */
@Entity
@Table(name = "hkj_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjOrder extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "order_date", nullable = false)
    private Instant orderDate;

    @Column(name = "expected_delivery_date")
    private Instant expectedDeliveryDate;

    @Column(name = "actual_delivery_date")
    private Instant actualDeliveryDate;

    @Size(max = 5000)
    @Column(name = "special_requests", length = 5000)
    private String specialRequests;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private HkjOrderStatus status;

    @Min(value = 1)
    @Max(value = 5)
    @Column(name = "customer_rating")
    private Integer customerRating;

    @Column(name = "total_price", precision = 21, scale = 2)
    private BigDecimal totalPrice;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @Transient
    private boolean isPersisted;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "order")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "order" }, allowSetters = true)
    private Set<HkjOrderImage> orderImages = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "salarys" }, allowSetters = true)
    private UserExtra customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "images" }, allowSetters = true)
    private HkjMaterial material;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "images", "materials", "category", "project", "material", "hkjCart" }, allowSetters = true)
    private HkjJewelryModel jewelry;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tasks", "manager", "category", "material" }, allowSetters = true)
    private HkjProject project;

    @ManyToOne(fetch = FetchType.LAZY)
    private HkjCategory category;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HkjOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getOrderDate() {
        return this.orderDate;
    }

    public HkjOrder orderDate(Instant orderDate) {
        this.setOrderDate(orderDate);
        return this;
    }

    public void setOrderDate(Instant orderDate) {
        this.orderDate = orderDate;
    }

    public Instant getExpectedDeliveryDate() {
        return this.expectedDeliveryDate;
    }

    public HkjOrder expectedDeliveryDate(Instant expectedDeliveryDate) {
        this.setExpectedDeliveryDate(expectedDeliveryDate);
        return this;
    }

    public void setExpectedDeliveryDate(Instant expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public Instant getActualDeliveryDate() {
        return this.actualDeliveryDate;
    }

    public HkjOrder actualDeliveryDate(Instant actualDeliveryDate) {
        this.setActualDeliveryDate(actualDeliveryDate);
        return this;
    }

    public void setActualDeliveryDate(Instant actualDeliveryDate) {
        this.actualDeliveryDate = actualDeliveryDate;
    }

    public String getSpecialRequests() {
        return this.specialRequests;
    }

    public HkjOrder specialRequests(String specialRequests) {
        this.setSpecialRequests(specialRequests);
        return this;
    }

    public void setSpecialRequests(String specialRequests) {
        this.specialRequests = specialRequests;
    }

    public HkjOrderStatus getStatus() {
        return this.status;
    }

    public HkjOrder status(HkjOrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(HkjOrderStatus status) {
        this.status = status;
    }

    public Integer getCustomerRating() {
        return this.customerRating;
    }

    public HkjOrder customerRating(Integer customerRating) {
        this.setCustomerRating(customerRating);
        return this;
    }

    public void setCustomerRating(Integer customerRating) {
        this.customerRating = customerRating;
    }

    public BigDecimal getTotalPrice() {
        return this.totalPrice;
    }

    public HkjOrder totalPrice(BigDecimal totalPrice) {
        this.setTotalPrice(totalPrice);
        return this;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public HkjOrder isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    // Inherited createdBy methods
    public HkjOrder createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public HkjOrder createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public HkjOrder lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public HkjOrder lastModifiedDate(Instant lastModifiedDate) {
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

    public HkjOrder setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Set<HkjOrderImage> getOrderImages() {
        return this.orderImages;
    }

    public void setOrderImages(Set<HkjOrderImage> hkjOrderImages) {
        if (this.orderImages != null) {
            this.orderImages.forEach(i -> i.setOrder(null));
        }
        if (hkjOrderImages != null) {
            hkjOrderImages.forEach(i -> i.setOrder(this));
        }
        this.orderImages = hkjOrderImages;
    }

    public HkjOrder orderImages(Set<HkjOrderImage> hkjOrderImages) {
        this.setOrderImages(hkjOrderImages);
        return this;
    }

    public HkjOrder addOrderImages(HkjOrderImage hkjOrderImage) {
        this.orderImages.add(hkjOrderImage);
        hkjOrderImage.setOrder(this);
        return this;
    }

    public HkjOrder removeOrderImages(HkjOrderImage hkjOrderImage) {
        this.orderImages.remove(hkjOrderImage);
        hkjOrderImage.setOrder(null);
        return this;
    }

    public UserExtra getCustomer() {
        return this.customer;
    }

    public void setCustomer(UserExtra userExtra) {
        this.customer = userExtra;
    }

    public HkjOrder customer(UserExtra userExtra) {
        this.setCustomer(userExtra);
        return this;
    }

    public HkjMaterial getMaterial() {
        return this.material;
    }

    public void setMaterial(HkjMaterial hkjMaterial) {
        this.material = hkjMaterial;
    }

    public HkjOrder material(HkjMaterial hkjMaterial) {
        this.setMaterial(hkjMaterial);
        return this;
    }

    public HkjJewelryModel getJewelry() {
        return this.jewelry;
    }

    public void setJewelry(HkjJewelryModel hkjJewelryModel) {
        this.jewelry = hkjJewelryModel;
    }

    public HkjOrder jewelry(HkjJewelryModel hkjJewelryModel) {
        this.setJewelry(hkjJewelryModel);
        return this;
    }

    public HkjProject getProject() {
        return this.project;
    }

    public void setProject(HkjProject hkjProject) {
        this.project = hkjProject;
    }

    public HkjOrder project(HkjProject hkjProject) {
        this.setProject(hkjProject);
        return this;
    }

    public HkjCategory getCategory() {
        return this.category;
    }

    public void setCategory(HkjCategory hkjCategory) {
        this.category = hkjCategory;
    }

    public HkjOrder category(HkjCategory hkjCategory) {
        this.setCategory(hkjCategory);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjOrder)) {
            return false;
        }
        return getId() != null && getId().equals(((HkjOrder) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjOrder{" +
            "id=" + getId() +
            ", orderDate='" + getOrderDate() + "'" +
            ", expectedDeliveryDate='" + getExpectedDeliveryDate() + "'" +
            ", actualDeliveryDate='" + getActualDeliveryDate() + "'" +
            ", specialRequests='" + getSpecialRequests() + "'" +
            ", status='" + getStatus() + "'" +
            ", customerRating=" + getCustomerRating() +
            ", totalPrice=" + getTotalPrice() +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
