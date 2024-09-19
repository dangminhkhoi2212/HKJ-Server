package com.server.hkj.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.Instant;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.domain.Persistable;

/**
 * A HkjTemplateStep.
 */
@Entity
@Table(name = "hkj_template_step")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjTemplateStep extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

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

    @JsonIgnoreProperties(value = { "templateStep", "images", "materials", "employee", "hkjProject" }, allowSetters = true)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "templateStep")
    private HkjTask hkjTask;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "category", "steps", "creater", "hkjProject" }, allowSetters = true)
    private HkjTemplate hkjTemplate;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HkjTemplateStep id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public HkjTemplateStep name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public HkjTemplateStep isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    // Inherited createdBy methods
    public HkjTemplateStep createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public HkjTemplateStep createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public HkjTemplateStep lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public HkjTemplateStep lastModifiedDate(Instant lastModifiedDate) {
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

    public HkjTemplateStep setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public HkjTask getHkjTask() {
        return this.hkjTask;
    }

    public void setHkjTask(HkjTask hkjTask) {
        if (this.hkjTask != null) {
            this.hkjTask.setTemplateStep(null);
        }
        if (hkjTask != null) {
            hkjTask.setTemplateStep(this);
        }
        this.hkjTask = hkjTask;
    }

    public HkjTemplateStep hkjTask(HkjTask hkjTask) {
        this.setHkjTask(hkjTask);
        return this;
    }

    public HkjTemplate getHkjTemplate() {
        return this.hkjTemplate;
    }

    public void setHkjTemplate(HkjTemplate hkjTemplate) {
        this.hkjTemplate = hkjTemplate;
    }

    public HkjTemplateStep hkjTemplate(HkjTemplate hkjTemplate) {
        this.setHkjTemplate(hkjTemplate);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjTemplateStep)) {
            return false;
        }
        return getId() != null && getId().equals(((HkjTemplateStep) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjTemplateStep{" +
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
