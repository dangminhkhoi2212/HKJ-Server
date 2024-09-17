package com.server.hkj.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
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
    @Column(name = "hire_date", nullable = false)
    private Instant hireDate;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @Transient
    private boolean isPersisted;

    @JsonIgnoreProperties(value = { "hkjHire" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private HkjPosition position;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hkjHire")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "userExtra", "salarys", "hkjHire" }, allowSetters = true)
    private Set<HkjEmployee> employees = new HashSet<>();

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

    public Instant getHireDate() {
        return this.hireDate;
    }

    public HkjHire hireDate(Instant hireDate) {
        this.setHireDate(hireDate);
        return this;
    }

    public void setHireDate(Instant hireDate) {
        this.hireDate = hireDate;
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

    public Set<HkjEmployee> getEmployees() {
        return this.employees;
    }

    public void setEmployees(Set<HkjEmployee> hkjEmployees) {
        if (this.employees != null) {
            this.employees.forEach(i -> i.setHkjHire(null));
        }
        if (hkjEmployees != null) {
            hkjEmployees.forEach(i -> i.setHkjHire(this));
        }
        this.employees = hkjEmployees;
    }

    public HkjHire employees(Set<HkjEmployee> hkjEmployees) {
        this.setEmployees(hkjEmployees);
        return this;
    }

    public HkjHire addEmployees(HkjEmployee hkjEmployee) {
        this.employees.add(hkjEmployee);
        hkjEmployee.setHkjHire(this);
        return this;
    }

    public HkjHire removeEmployees(HkjEmployee hkjEmployee) {
        this.employees.remove(hkjEmployee);
        hkjEmployee.setHkjHire(null);
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
            ", hireDate='" + getHireDate() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
