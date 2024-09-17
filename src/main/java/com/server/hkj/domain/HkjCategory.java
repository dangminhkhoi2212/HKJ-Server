package com.server.hkj.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.domain.Persistable;

/**
 * A HkjCategory.
 */
@Entity
@Table(name = "hkj_category")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjCategory extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @Transient
    private boolean isPersisted;

    @JsonIgnoreProperties(value = { "category", "tasks", "manager", "hkjOrder" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "category")
    private HkjProject hkjProject;

    @JsonIgnoreProperties(value = { "category", "steps" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "category")
    private HkjTemplate hkjTemplate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HkjCategory id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public HkjCategory name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Inherited createdBy methods
    public HkjCategory createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public HkjCategory createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public HkjCategory lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public HkjCategory lastModifiedDate(Instant lastModifiedDate) {
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

    public HkjCategory setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public HkjProject getHkjProject() {
        return this.hkjProject;
    }

    public void setHkjProject(HkjProject hkjProject) {
        if (this.hkjProject != null) {
            this.hkjProject.setCategory(null);
        }
        if (hkjProject != null) {
            hkjProject.setCategory(this);
        }
        this.hkjProject = hkjProject;
    }

    public HkjCategory hkjProject(HkjProject hkjProject) {
        this.setHkjProject(hkjProject);
        return this;
    }

    public HkjTemplate getHkjTemplate() {
        return this.hkjTemplate;
    }

    public void setHkjTemplate(HkjTemplate hkjTemplate) {
        if (this.hkjTemplate != null) {
            this.hkjTemplate.setCategory(null);
        }
        if (hkjTemplate != null) {
            hkjTemplate.setCategory(this);
        }
        this.hkjTemplate = hkjTemplate;
    }

    public HkjCategory hkjTemplate(HkjTemplate hkjTemplate) {
        this.setHkjTemplate(hkjTemplate);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjCategory)) {
            return false;
        }
        return getId() != null && getId().equals(((HkjCategory) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjCategory{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
