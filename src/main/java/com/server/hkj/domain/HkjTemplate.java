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
 * A HkjTemplate.
 */
@Entity
@Table(name = "hkj_template")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjTemplate extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @Transient
    private boolean isPersisted;

    @JsonIgnoreProperties(value = { "hkjProject", "hkjTemplate" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(unique = true)
    private HkjCategory category;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "template")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "template" }, allowSetters = true)
    private Set<HkjTemplateStep> steps = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "salarys", "hires", "hkjTask" }, allowSetters = true)
    private UserExtra creater;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HkjTemplate id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public HkjTemplate name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public HkjTemplate isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    // Inherited createdBy methods
    public HkjTemplate createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public HkjTemplate createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public HkjTemplate lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public HkjTemplate lastModifiedDate(Instant lastModifiedDate) {
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

    public HkjTemplate setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public HkjCategory getCategory() {
        return this.category;
    }

    public void setCategory(HkjCategory hkjCategory) {
        this.category = hkjCategory;
    }

    public HkjTemplate category(HkjCategory hkjCategory) {
        this.setCategory(hkjCategory);
        return this;
    }

    public Set<HkjTemplateStep> getSteps() {
        return this.steps;
    }

    public void setSteps(Set<HkjTemplateStep> hkjTemplateSteps) {
        if (this.steps != null) {
            this.steps.forEach(i -> i.setTemplate(null));
        }
        if (hkjTemplateSteps != null) {
            hkjTemplateSteps.forEach(i -> i.setTemplate(this));
        }
        this.steps = hkjTemplateSteps;
    }

    public HkjTemplate steps(Set<HkjTemplateStep> hkjTemplateSteps) {
        this.setSteps(hkjTemplateSteps);
        return this;
    }

    public HkjTemplate addSteps(HkjTemplateStep hkjTemplateStep) {
        this.steps.add(hkjTemplateStep);
        hkjTemplateStep.setTemplate(this);
        return this;
    }

    public HkjTemplate removeSteps(HkjTemplateStep hkjTemplateStep) {
        this.steps.remove(hkjTemplateStep);
        hkjTemplateStep.setTemplate(null);
        return this;
    }

    public UserExtra getCreater() {
        return this.creater;
    }

    public void setCreater(UserExtra userExtra) {
        this.creater = userExtra;
    }

    public HkjTemplate creater(UserExtra userExtra) {
        this.setCreater(userExtra);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjTemplate)) {
            return false;
        }
        return getId() != null && getId().equals(((HkjTemplate) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjTemplate{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
