package com.server.hkj.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.domain.Persistable;

/**
 * A HkjTrackSearchImage.
 */
@Entity
@Table(name = "hkj_track_search_image")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjTrackSearchImage extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "search_order")
    private Integer searchOrder;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @Transient
    private boolean isPersisted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "salarys" }, allowSetters = true)
    private UserExtra user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "images", "category", "project" }, allowSetters = true)
    private HkjJewelryModel jewelry;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HkjTrackSearchImage id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getSearchOrder() {
        return this.searchOrder;
    }

    public HkjTrackSearchImage searchOrder(Integer searchOrder) {
        this.setSearchOrder(searchOrder);
        return this;
    }

    public void setSearchOrder(Integer searchOrder) {
        this.searchOrder = searchOrder;
    }

    // Inherited createdBy methods
    public HkjTrackSearchImage createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public HkjTrackSearchImage createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public HkjTrackSearchImage lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public HkjTrackSearchImage lastModifiedDate(Instant lastModifiedDate) {
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

    public HkjTrackSearchImage setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public UserExtra getUser() {
        return this.user;
    }

    public void setUser(UserExtra userExtra) {
        this.user = userExtra;
    }

    public HkjTrackSearchImage user(UserExtra userExtra) {
        this.setUser(userExtra);
        return this;
    }

    public HkjJewelryModel getJewelry() {
        return this.jewelry;
    }

    public void setJewelry(HkjJewelryModel hkjJewelryModel) {
        this.jewelry = hkjJewelryModel;
    }

    public HkjTrackSearchImage jewelry(HkjJewelryModel hkjJewelryModel) {
        this.setJewelry(hkjJewelryModel);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjTrackSearchImage)) {
            return false;
        }
        return getId() != null && getId().equals(((HkjTrackSearchImage) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjTrackSearchImage{" +
            "id=" + getId() +
            ", searchOrder=" + getSearchOrder() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
