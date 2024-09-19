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
 * A HkjEmployee.
 */
@Entity
@Table(name = "hkj_employee")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjEmployee extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Size(max = 1000)
    @Column(name = "notes", length = 1000)
    private String notes;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @Transient
    private boolean isPersisted;

    @JsonIgnoreProperties(value = { "user", "hkjEmployee" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private UserExtra userExtra;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hkjEmployee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "hkjEmployee" }, allowSetters = true)
    private Set<HkjSalary> salarys = new HashSet<>();

    @JsonIgnoreProperties(value = { "position", "employee" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "employee")
    private HkjHire hkjHire;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HkjEmployee id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNotes() {
        return this.notes;
    }

    public HkjEmployee notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public HkjEmployee isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    // Inherited createdBy methods
    public HkjEmployee createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public HkjEmployee createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public HkjEmployee lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public HkjEmployee lastModifiedDate(Instant lastModifiedDate) {
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

    public HkjEmployee setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public UserExtra getUserExtra() {
        return this.userExtra;
    }

    public void setUserExtra(UserExtra userExtra) {
        this.userExtra = userExtra;
    }

    public HkjEmployee userExtra(UserExtra userExtra) {
        this.setUserExtra(userExtra);
        return this;
    }

    public Set<HkjSalary> getSalarys() {
        return this.salarys;
    }

    public void setSalarys(Set<HkjSalary> hkjSalaries) {
        if (this.salarys != null) {
            this.salarys.forEach(i -> i.setHkjEmployee(null));
        }
        if (hkjSalaries != null) {
            hkjSalaries.forEach(i -> i.setHkjEmployee(this));
        }
        this.salarys = hkjSalaries;
    }

    public HkjEmployee salarys(Set<HkjSalary> hkjSalaries) {
        this.setSalarys(hkjSalaries);
        return this;
    }

    public HkjEmployee addSalarys(HkjSalary hkjSalary) {
        this.salarys.add(hkjSalary);
        hkjSalary.setHkjEmployee(this);
        return this;
    }

    public HkjEmployee removeSalarys(HkjSalary hkjSalary) {
        this.salarys.remove(hkjSalary);
        hkjSalary.setHkjEmployee(null);
        return this;
    }

    public HkjHire getHkjHire() {
        return this.hkjHire;
    }

    public void setHkjHire(HkjHire hkjHire) {
        if (this.hkjHire != null) {
            this.hkjHire.setEmployee(null);
        }
        if (hkjHire != null) {
            hkjHire.setEmployee(this);
        }
        this.hkjHire = hkjHire;
    }

    public HkjEmployee hkjHire(HkjHire hkjHire) {
        this.setHkjHire(hkjHire);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjEmployee)) {
            return false;
        }
        return getId() != null && getId().equals(((HkjEmployee) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjEmployee{" +
            "id=" + getId() +
            ", notes='" + getNotes() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
