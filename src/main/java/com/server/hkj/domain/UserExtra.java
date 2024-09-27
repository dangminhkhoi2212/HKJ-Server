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
 * A UserExtra.
 */
@Entity
@Table(name = "user_extra")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserExtra extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "phone", nullable = false)
    private String phone;

    @Column(name = "address")
    private String address;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @Transient
    private boolean isPersisted;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "employee" }, allowSetters = true)
    private Set<HkjSalary> salarys = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "employee")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "position", "employee" }, allowSetters = true)
    private Set<HkjHire> hires = new HashSet<>();

    @JsonIgnoreProperties(value = { "employee", "images", "materials", "project" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "employee")
    private HkjTask hkjTask;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserExtra id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhone() {
        return this.phone;
    }

    public UserExtra phone(String phone) {
        this.setPhone(phone);
        return this;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return this.address;
    }

    public UserExtra address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public UserExtra isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    // Inherited createdBy methods
    public UserExtra createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public UserExtra createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public UserExtra lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public UserExtra lastModifiedDate(Instant lastModifiedDate) {
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

    public UserExtra setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public User getUser() {
        return this.user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public UserExtra user(User user) {
        this.setUser(user);
        return this;
    }

    public Set<HkjSalary> getSalarys() {
        return this.salarys;
    }

    public void setSalarys(Set<HkjSalary> hkjSalaries) {
        if (this.salarys != null) {
            this.salarys.forEach(i -> i.setEmployee(null));
        }
        if (hkjSalaries != null) {
            hkjSalaries.forEach(i -> i.setEmployee(this));
        }
        this.salarys = hkjSalaries;
    }

    public UserExtra salarys(Set<HkjSalary> hkjSalaries) {
        this.setSalarys(hkjSalaries);
        return this;
    }

    public UserExtra addSalarys(HkjSalary hkjSalary) {
        this.salarys.add(hkjSalary);
        hkjSalary.setEmployee(this);
        return this;
    }

    public UserExtra removeSalarys(HkjSalary hkjSalary) {
        this.salarys.remove(hkjSalary);
        hkjSalary.setEmployee(null);
        return this;
    }

    public Set<HkjHire> getHires() {
        return this.hires;
    }

    public void setHires(Set<HkjHire> hkjHires) {
        if (this.hires != null) {
            this.hires.forEach(i -> i.setEmployee(null));
        }
        if (hkjHires != null) {
            hkjHires.forEach(i -> i.setEmployee(this));
        }
        this.hires = hkjHires;
    }

    public UserExtra hires(Set<HkjHire> hkjHires) {
        this.setHires(hkjHires);
        return this;
    }

    public UserExtra addHire(HkjHire hkjHire) {
        this.hires.add(hkjHire);
        hkjHire.setEmployee(this);
        return this;
    }

    public UserExtra removeHire(HkjHire hkjHire) {
        this.hires.remove(hkjHire);
        hkjHire.setEmployee(null);
        return this;
    }

    public HkjTask getHkjTask() {
        return this.hkjTask;
    }

    public void setHkjTask(HkjTask hkjTask) {
        if (this.hkjTask != null) {
            this.hkjTask.setEmployee(null);
        }
        if (hkjTask != null) {
            hkjTask.setEmployee(this);
        }
        this.hkjTask = hkjTask;
    }

    public UserExtra hkjTask(HkjTask hkjTask) {
        this.setHkjTask(hkjTask);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserExtra)) {
            return false;
        }
        return getId() != null && getId().equals(((UserExtra) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserExtra{" +
            "id=" + getId() +
            ", phone='" + getPhone() + "'" +
            ", address='" + getAddress() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
