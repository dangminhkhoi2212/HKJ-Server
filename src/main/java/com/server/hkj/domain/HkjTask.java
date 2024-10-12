package com.server.hkj.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.server.hkj.domain.enumeration.HkjOrderStatus;
import com.server.hkj.domain.enumeration.HkjPriority;
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
 * A HkjTask.
 */
@Entity
@Table(name = "hkj_task")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjTask extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "cover_image")
    private String coverImage;

    @Column(name = "description")
    private String description;

    @NotNull
    @Column(name = "assigned_date", nullable = false)
    private Instant assignedDate;

    @NotNull
    @Column(name = "expect_date", nullable = false)
    private Instant expectDate;

    @Column(name = "completed_date")
    private Instant completedDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private HkjOrderStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private HkjPriority priority;

    @Min(value = 0)
    @Max(value = 100)
    @Column(name = "point")
    private Integer point;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hkjTask")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "hkjTask" }, allowSetters = true)
    private Set<HkjTaskImage> images = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hkjTask")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "material", "hkjTask" }, allowSetters = true)
    private Set<HkjMaterialUsage> materials = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "salarys" }, allowSetters = true)
    private UserExtra employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "tasks", "manager", "category" }, allowSetters = true)
    private HkjProject project;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HkjTask id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public HkjTask name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverImage() {
        return this.coverImage;
    }

    public HkjTask coverImage(String coverImage) {
        this.setCoverImage(coverImage);
        return this;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getDescription() {
        return this.description;
    }

    public HkjTask description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getAssignedDate() {
        return this.assignedDate;
    }

    public HkjTask assignedDate(Instant assignedDate) {
        this.setAssignedDate(assignedDate);
        return this;
    }

    public void setAssignedDate(Instant assignedDate) {
        this.assignedDate = assignedDate;
    }

    public Instant getExpectDate() {
        return this.expectDate;
    }

    public HkjTask expectDate(Instant expectDate) {
        this.setExpectDate(expectDate);
        return this;
    }

    public void setExpectDate(Instant expectDate) {
        this.expectDate = expectDate;
    }

    public Instant getCompletedDate() {
        return this.completedDate;
    }

    public HkjTask completedDate(Instant completedDate) {
        this.setCompletedDate(completedDate);
        return this;
    }

    public void setCompletedDate(Instant completedDate) {
        this.completedDate = completedDate;
    }

    public HkjOrderStatus getStatus() {
        return this.status;
    }

    public HkjTask status(HkjOrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(HkjOrderStatus status) {
        this.status = status;
    }

    public HkjPriority getPriority() {
        return this.priority;
    }

    public HkjTask priority(HkjPriority priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(HkjPriority priority) {
        this.priority = priority;
    }

    public Integer getPoint() {
        return this.point;
    }

    public HkjTask point(Integer point) {
        this.setPoint(point);
        return this;
    }

    public void setPoint(Integer point) {
        this.point = point;
    }

    public String getNotes() {
        return this.notes;
    }

    public HkjTask notes(String notes) {
        this.setNotes(notes);
        return this;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public HkjTask isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    // Inherited createdBy methods
    public HkjTask createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public HkjTask createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public HkjTask lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public HkjTask lastModifiedDate(Instant lastModifiedDate) {
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

    public HkjTask setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Set<HkjTaskImage> getImages() {
        return this.images;
    }

    public void setImages(Set<HkjTaskImage> hkjTaskImages) {
        if (this.images != null) {
            this.images.forEach(i -> i.setHkjTask(null));
        }
        if (hkjTaskImages != null) {
            hkjTaskImages.forEach(i -> i.setHkjTask(this));
        }
        this.images = hkjTaskImages;
    }

    public HkjTask images(Set<HkjTaskImage> hkjTaskImages) {
        this.setImages(hkjTaskImages);
        return this;
    }

    public HkjTask addImages(HkjTaskImage hkjTaskImage) {
        this.images.add(hkjTaskImage);
        hkjTaskImage.setHkjTask(this);
        return this;
    }

    public HkjTask removeImages(HkjTaskImage hkjTaskImage) {
        this.images.remove(hkjTaskImage);
        hkjTaskImage.setHkjTask(null);
        return this;
    }

    public Set<HkjMaterialUsage> getMaterials() {
        return this.materials;
    }

    public void setMaterials(Set<HkjMaterialUsage> hkjMaterialUsages) {
        if (this.materials != null) {
            this.materials.forEach(i -> i.setHkjTask(null));
        }
        if (hkjMaterialUsages != null) {
            hkjMaterialUsages.forEach(i -> i.setHkjTask(this));
        }
        this.materials = hkjMaterialUsages;
    }

    public HkjTask materials(Set<HkjMaterialUsage> hkjMaterialUsages) {
        this.setMaterials(hkjMaterialUsages);
        return this;
    }

    public HkjTask addMaterials(HkjMaterialUsage hkjMaterialUsage) {
        this.materials.add(hkjMaterialUsage);
        hkjMaterialUsage.setHkjTask(this);
        return this;
    }

    public HkjTask removeMaterials(HkjMaterialUsage hkjMaterialUsage) {
        this.materials.remove(hkjMaterialUsage);
        hkjMaterialUsage.setHkjTask(null);
        return this;
    }

    public UserExtra getEmployee() {
        return this.employee;
    }

    public void setEmployee(UserExtra userExtra) {
        this.employee = userExtra;
    }

    public HkjTask employee(UserExtra userExtra) {
        this.setEmployee(userExtra);
        return this;
    }

    public HkjProject getProject() {
        return this.project;
    }

    public void setProject(HkjProject hkjProject) {
        this.project = hkjProject;
    }

    public HkjTask project(HkjProject hkjProject) {
        this.setProject(hkjProject);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjTask)) {
            return false;
        }
        return getId() != null && getId().equals(((HkjTask) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjTask{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", coverImage='" + getCoverImage() + "'" +
            ", description='" + getDescription() + "'" +
            ", assignedDate='" + getAssignedDate() + "'" +
            ", expectDate='" + getExpectDate() + "'" +
            ", completedDate='" + getCompletedDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", priority='" + getPriority() + "'" +
            ", point=" + getPoint() +
            ", notes='" + getNotes() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
