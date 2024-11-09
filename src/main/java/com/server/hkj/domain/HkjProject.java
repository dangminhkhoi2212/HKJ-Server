package com.server.hkj.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.server.hkj.domain.enumeration.HkjOrderStatus;
import com.server.hkj.domain.enumeration.HkjPriority;
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
 * A HkjProject.
 */
@Entity
@Table(name = "hkj_project")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@JsonIgnoreProperties(value = { "new" })
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjProject extends AbstractAuditingEntity<Long> implements Serializable, Persistable<Long> {

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

    @Size(max = 10000)
    @Column(name = "description", length = 10000)
    private String description;

    @NotNull
    @Column(name = "start_date", nullable = false)
    private Instant startDate;

    @Column(name = "expect_date")
    private Instant expectDate;

    @Column(name = "end_date")
    private Instant endDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private HkjOrderStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private HkjPriority priority;

    @Column(name = "actual_cost", precision = 21, scale = 2)
    private BigDecimal actualCost;

    @Column(name = "quality_check")
    private Boolean qualityCheck;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    // Inherited createdBy definition
    // Inherited createdDate definition
    // Inherited lastModifiedBy definition
    // Inherited lastModifiedDate definition
    @Transient
    private boolean isPersisted;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "project")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "images", "materials", "employee", "project" }, allowSetters = true)
    private Set<HkjTask> tasks = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "user", "salarys" }, allowSetters = true)
    private UserExtra manager;

    @ManyToOne(fetch = FetchType.LAZY)
    private HkjCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "images" }, allowSetters = true)
    private HkjMaterial material;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public HkjProject id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public HkjProject name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCoverImage() {
        return this.coverImage;
    }

    public HkjProject coverImage(String coverImage) {
        this.setCoverImage(coverImage);
        return this;
    }

    public void setCoverImage(String coverImage) {
        this.coverImage = coverImage;
    }

    public String getDescription() {
        return this.description;
    }

    public HkjProject description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public HkjProject startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getExpectDate() {
        return this.expectDate;
    }

    public HkjProject expectDate(Instant expectDate) {
        this.setExpectDate(expectDate);
        return this;
    }

    public void setExpectDate(Instant expectDate) {
        this.expectDate = expectDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public HkjProject endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public HkjOrderStatus getStatus() {
        return this.status;
    }

    public HkjProject status(HkjOrderStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(HkjOrderStatus status) {
        this.status = status;
    }

    public HkjPriority getPriority() {
        return this.priority;
    }

    public HkjProject priority(HkjPriority priority) {
        this.setPriority(priority);
        return this;
    }

    public void setPriority(HkjPriority priority) {
        this.priority = priority;
    }

    public BigDecimal getActualCost() {
        return this.actualCost;
    }

    public HkjProject actualCost(BigDecimal actualCost) {
        this.setActualCost(actualCost);
        return this;
    }

    public void setActualCost(BigDecimal actualCost) {
        this.actualCost = actualCost;
    }

    public Boolean getQualityCheck() {
        return this.qualityCheck;
    }

    public HkjProject qualityCheck(Boolean qualityCheck) {
        this.setQualityCheck(qualityCheck);
        return this;
    }

    public void setQualityCheck(Boolean qualityCheck) {
        this.qualityCheck = qualityCheck;
    }

    public Boolean getIsDeleted() {
        return this.isDeleted;
    }

    public HkjProject isDeleted(Boolean isDeleted) {
        this.setIsDeleted(isDeleted);
        return this;
    }

    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }

    // Inherited createdBy methods
    public HkjProject createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    // Inherited createdDate methods
    public HkjProject createdDate(Instant createdDate) {
        this.setCreatedDate(createdDate);
        return this;
    }

    // Inherited lastModifiedBy methods
    public HkjProject lastModifiedBy(String lastModifiedBy) {
        this.setLastModifiedBy(lastModifiedBy);
        return this;
    }

    // Inherited lastModifiedDate methods
    public HkjProject lastModifiedDate(Instant lastModifiedDate) {
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

    public HkjProject setIsPersisted() {
        this.isPersisted = true;
        return this;
    }

    public Set<HkjTask> getTasks() {
        return this.tasks;
    }

    public void setTasks(Set<HkjTask> hkjTasks) {
        if (this.tasks != null) {
            this.tasks.forEach(i -> i.setProject(null));
        }
        if (hkjTasks != null) {
            hkjTasks.forEach(i -> i.setProject(this));
        }
        this.tasks = hkjTasks;
    }

    public HkjProject tasks(Set<HkjTask> hkjTasks) {
        this.setTasks(hkjTasks);
        return this;
    }

    public HkjProject addTasks(HkjTask hkjTask) {
        this.tasks.add(hkjTask);
        hkjTask.setProject(this);
        return this;
    }

    public HkjProject removeTasks(HkjTask hkjTask) {
        this.tasks.remove(hkjTask);
        hkjTask.setProject(null);
        return this;
    }

    public UserExtra getManager() {
        return this.manager;
    }

    public void setManager(UserExtra userExtra) {
        this.manager = userExtra;
    }

    public HkjProject manager(UserExtra userExtra) {
        this.setManager(userExtra);
        return this;
    }

    public HkjCategory getCategory() {
        return this.category;
    }

    public void setCategory(HkjCategory hkjCategory) {
        this.category = hkjCategory;
    }

    public HkjProject category(HkjCategory hkjCategory) {
        this.setCategory(hkjCategory);
        return this;
    }

    public HkjMaterial getMaterial() {
        return this.material;
    }

    public void setMaterial(HkjMaterial hkjMaterial) {
        this.material = hkjMaterial;
    }

    public HkjProject material(HkjMaterial hkjMaterial) {
        this.setMaterial(hkjMaterial);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof HkjProject)) {
            return false;
        }
        return getId() != null && getId().equals(((HkjProject) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjProject{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", coverImage='" + getCoverImage() + "'" +
            ", description='" + getDescription() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", expectDate='" + getExpectDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", priority='" + getPriority() + "'" +
            ", actualCost=" + getActualCost() +
            ", qualityCheck='" + getQualityCheck() + "'" +
            ", isDeleted='" + getIsDeleted() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdDate='" + getCreatedDate() + "'" +
            ", lastModifiedBy='" + getLastModifiedBy() + "'" +
            ", lastModifiedDate='" + getLastModifiedDate() + "'" +
            "}";
    }
}
