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
 * A HkjHire.
 */
@Entity
@Table(name = "hkj_hire")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjHire extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "begin_date", nullable = false)
    private Instant beginDate;

    @NotNull
    @Column(name = "end_date", nullable = false)
    private Instant endDate;

    @Column(name = "begin_salary", precision = 21, scale = 2)
    private BigDecimal beginSalary;

    @Column(name = "notes")
    private String notes;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @Transient
    private boolean isPersisted;

    @ManyToOne(fetch = FetchType.LAZY)
    private HkjPosition position;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "salarys", "hkjTask" }, allowSetters = true)
    private UserExtra employee;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HkjHire id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Instant getBeginDate() {
        return this.beginDate;
    }

    public HkjHire beginDate(Instant beginDate) {
        this.setBeginDate(beginDate);
        return this;
    }

    public void setBeginDate(Instant beginDate) {
        this.beginDate = beginDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public HkjHire endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public BigDecimal getBeginSalary() {
        return this.beginSalary;
    }

    public HkjHire beginSalary(BigDecimal beginSalary) {
        this.setBeginSalary(beginSalary);
        return this;
    }

    public void setBeginSalary(BigDecimal beginSalary) {
        this.beginSalary = beginSalary;
    }

    public String getNotes() {
        return this.notes;
    }

    public HkjHire notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public HkjHire isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    // Inherited createdBy methods
    public HkjHire createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public HkjHire createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public HkjHire lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public HkjHire lastModifiedDate(Instant lastModifiedDate) {
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

    public HkjHire setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public HkjPosition getPosition() {
        return this.position;
    }

    public void setPosition(HkjPosition hkjPosition) {
        this.position = hkjPosition;
    }

    public HkjHire position(HkjPosition hkjPosition) {
        this.setPosition(hkjPosition);
        return this;
    }

    public UserExtra getEmployee() {
        return this.employee;
    }

    public void setEmployee(UserExtra userExtra) {
        this.employee = userExtra;
    }

    public HkjHire employee(UserExtra userExtra) {
        this.setEmployee(userExtra);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjHire)) {
            return false;
        }
        return getId() != null && getId().equals(((HkjHire) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjHire{" +
            "id=" + getId() +
            ", beginDate='" + getBeginDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", beginSalary=" + getBeginSalary() +
            ", notes='" + getNotes() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
