package com.server.hkj.service.criteria;

import com.server.hkj.domain.enumeration.HkjOrderStatus;
import com.server.hkj.domain.enumeration.HkjPriority;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.server.hkj.domain.HkjTask} entity. This class is used
 * in {@link com.server.hkj.web.rest.HkjTaskResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /hkj-tasks?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjTaskCriteria implements Serializable, Criteria {

    /**
     * Class for filtering HkjOrderStatus
     */
    public static class HkjOrderStatusFilter extends Filter<HkjOrderStatus> {

        public HkjOrderStatusFilter() {}

        public HkjOrderStatusFilter(HkjOrderStatusFilter filter) {
            super(filter);
        }

        @Override
        public HkjOrderStatusFilter copy() {
            return new HkjOrderStatusFilter(this);
        }
    }

    /**
     * Class for filtering HkjPriority
     */
    public static class HkjPriorityFilter extends Filter<HkjPriority> {

        public HkjPriorityFilter() {}

        public HkjPriorityFilter(HkjPriorityFilter filter) {
            super(filter);
        }

        @Override
        public HkjPriorityFilter copy() {
            return new HkjPriorityFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private StringFilter coverImage;

    private StringFilter description;

    private InstantFilter assignedDate;

    private InstantFilter expectDate;

    private InstantFilter completedDate;

    private HkjOrderStatusFilter status;

    private HkjPriorityFilter priority;

    private IntegerFilter point;

    private StringFilter notes;

    private BooleanFilter isDeleted;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter employeeId;

    private LongFilter imagesId;

    private LongFilter materialsId;

    private LongFilter hkjProjectId;

    private Boolean distinct;

    public HkjTaskCriteria() {}

    public HkjTaskCriteria(HkjTaskCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.coverImage = other.optionalCoverImage().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.assignedDate = other.optionalAssignedDate().map(InstantFilter::copy).orElse(null);
        this.expectDate = other.optionalExpectDate().map(InstantFilter::copy).orElse(null);
        this.completedDate = other.optionalCompletedDate().map(InstantFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(HkjOrderStatusFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(HkjPriorityFilter::copy).orElse(null);
        this.point = other.optionalPoint().map(IntegerFilter::copy).orElse(null);
        this.notes = other.optionalNotes().map(StringFilter::copy).orElse(null);
        this.isDeleted = other.optionalIsDeleted().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.employeeId = other.optionalEmployeeId().map(LongFilter::copy).orElse(null);
        this.imagesId = other.optionalImagesId().map(LongFilter::copy).orElse(null);
        this.materialsId = other.optionalMaterialsId().map(LongFilter::copy).orElse(null);
        this.hkjProjectId = other.optionalHkjProjectId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public HkjTaskCriteria copy() {
        return new HkjTaskCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getName() {
        return name;
    }

    public Optional<StringFilter> optionalName() {
        return Optional.ofNullable(name);
    }

    public StringFilter name() {
        if (name == null) {
            setName(new StringFilter());
        }
        return name;
    }

    public void setName(StringFilter name) {
        this.name = name;
    }

    public StringFilter getCoverImage() {
        return coverImage;
    }

    public Optional<StringFilter> optionalCoverImage() {
        return Optional.ofNullable(coverImage);
    }

    public StringFilter coverImage() {
        if (coverImage == null) {
            setCoverImage(new StringFilter());
        }
        return coverImage;
    }

    public void setCoverImage(StringFilter coverImage) {
        this.coverImage = coverImage;
    }

    public StringFilter getDescription() {
        return description;
    }

    public Optional<StringFilter> optionalDescription() {
        return Optional.ofNullable(description);
    }

    public StringFilter description() {
        if (description == null) {
            setDescription(new StringFilter());
        }
        return description;
    }

    public void setDescription(StringFilter description) {
        this.description = description;
    }

    public InstantFilter getAssignedDate() {
        return assignedDate;
    }

    public Optional<InstantFilter> optionalAssignedDate() {
        return Optional.ofNullable(assignedDate);
    }

    public InstantFilter assignedDate() {
        if (assignedDate == null) {
            setAssignedDate(new InstantFilter());
        }
        return assignedDate;
    }

    public void setAssignedDate(InstantFilter assignedDate) {
        this.assignedDate = assignedDate;
    }

    public InstantFilter getExpectDate() {
        return expectDate;
    }

    public Optional<InstantFilter> optionalExpectDate() {
        return Optional.ofNullable(expectDate);
    }

    public InstantFilter expectDate() {
        if (expectDate == null) {
            setExpectDate(new InstantFilter());
        }
        return expectDate;
    }

    public void setExpectDate(InstantFilter expectDate) {
        this.expectDate = expectDate;
    }

    public InstantFilter getCompletedDate() {
        return completedDate;
    }

    public Optional<InstantFilter> optionalCompletedDate() {
        return Optional.ofNullable(completedDate);
    }

    public InstantFilter completedDate() {
        if (completedDate == null) {
            setCompletedDate(new InstantFilter());
        }
        return completedDate;
    }

    public void setCompletedDate(InstantFilter completedDate) {
        this.completedDate = completedDate;
    }

    public HkjOrderStatusFilter getStatus() {
        return status;
    }

    public Optional<HkjOrderStatusFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public HkjOrderStatusFilter status() {
        if (status == null) {
            setStatus(new HkjOrderStatusFilter());
        }
        return status;
    }

    public void setStatus(HkjOrderStatusFilter status) {
        this.status = status;
    }

    public HkjPriorityFilter getPriority() {
        return priority;
    }

    public Optional<HkjPriorityFilter> optionalPriority() {
        return Optional.ofNullable(priority);
    }

    public HkjPriorityFilter priority() {
        if (priority == null) {
            setPriority(new HkjPriorityFilter());
        }
        return priority;
    }

    public void setPriority(HkjPriorityFilter priority) {
        this.priority = priority;
    }

    public IntegerFilter getPoint() {
        return point;
    }

    public Optional<IntegerFilter> optionalPoint() {
        return Optional.ofNullable(point);
    }

    public IntegerFilter point() {
        if (point == null) {
            setPoint(new IntegerFilter());
        }
        return point;
    }

    public void setPoint(IntegerFilter point) {
        this.point = point;
    }

    public StringFilter getNotes() {
        return notes;
    }

    public Optional<StringFilter> optionalNotes() {
        return Optional.ofNullable(notes);
    }

    public StringFilter notes() {
        if (notes == null) {
            setNotes(new StringFilter());
        }
        return notes;
    }

    public void setNotes(StringFilter notes) {
        this.notes = notes;
    }

    public BooleanFilter getIsDeleted() {
        return isDeleted;
    }

    public Optional<BooleanFilter> optionalIsDeleted() {
        return Optional.ofNullable(isDeleted);
    }

    public BooleanFilter isDeleted() {
        if (isDeleted == null) {
            setIsDeleted(new BooleanFilter());
        }
        return isDeleted;
    }

    public void setIsDeleted(BooleanFilter isDeleted) {
        this.isDeleted = isDeleted;
    }

    public StringFilter getCreatedBy() {
        return createdBy;
    }

    public Optional<StringFilter> optionalCreatedBy() {
        return Optional.ofNullable(createdBy);
    }

    public StringFilter createdBy() {
        if (createdBy == null) {
            setCreatedBy(new StringFilter());
        }
        return createdBy;
    }

    public void setCreatedBy(StringFilter createdBy) {
        this.createdBy = createdBy;
    }

    public InstantFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<InstantFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public InstantFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new InstantFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(InstantFilter createdDate) {
        this.createdDate = createdDate;
    }

    public StringFilter getLastModifiedBy() {
        return lastModifiedBy;
    }

    public Optional<StringFilter> optionalLastModifiedBy() {
        return Optional.ofNullable(lastModifiedBy);
    }

    public StringFilter lastModifiedBy() {
        if (lastModifiedBy == null) {
            setLastModifiedBy(new StringFilter());
        }
        return lastModifiedBy;
    }

    public void setLastModifiedBy(StringFilter lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public InstantFilter getLastModifiedDate() {
        return lastModifiedDate;
    }

    public Optional<InstantFilter> optionalLastModifiedDate() {
        return Optional.ofNullable(lastModifiedDate);
    }

    public InstantFilter lastModifiedDate() {
        if (lastModifiedDate == null) {
            setLastModifiedDate(new InstantFilter());
        }
        return lastModifiedDate;
    }

    public void setLastModifiedDate(InstantFilter lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    public LongFilter getEmployeeId() {
        return employeeId;
    }

    public Optional<LongFilter> optionalEmployeeId() {
        return Optional.ofNullable(employeeId);
    }

    public LongFilter employeeId() {
        if (employeeId == null) {
            setEmployeeId(new LongFilter());
        }
        return employeeId;
    }

    public void setEmployeeId(LongFilter employeeId) {
        this.employeeId = employeeId;
    }

    public LongFilter getImagesId() {
        return imagesId;
    }

    public Optional<LongFilter> optionalImagesId() {
        return Optional.ofNullable(imagesId);
    }

    public LongFilter imagesId() {
        if (imagesId == null) {
            setImagesId(new LongFilter());
        }
        return imagesId;
    }

    public void setImagesId(LongFilter imagesId) {
        this.imagesId = imagesId;
    }

    public LongFilter getMaterialsId() {
        return materialsId;
    }

    public Optional<LongFilter> optionalMaterialsId() {
        return Optional.ofNullable(materialsId);
    }

    public LongFilter materialsId() {
        if (materialsId == null) {
            setMaterialsId(new LongFilter());
        }
        return materialsId;
    }

    public void setMaterialsId(LongFilter materialsId) {
        this.materialsId = materialsId;
    }

    public LongFilter getHkjProjectId() {
        return hkjProjectId;
    }

    public Optional<LongFilter> optionalHkjProjectId() {
        return Optional.ofNullable(hkjProjectId);
    }

    public LongFilter hkjProjectId() {
        if (hkjProjectId == null) {
            setHkjProjectId(new LongFilter());
        }
        return hkjProjectId;
    }

    public void setHkjProjectId(LongFilter hkjProjectId) {
        this.hkjProjectId = hkjProjectId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final HkjTaskCriteria that = (HkjTaskCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(coverImage, that.coverImage) &&
            Objects.equals(description, that.description) &&
            Objects.equals(assignedDate, that.assignedDate) &&
            Objects.equals(expectDate, that.expectDate) &&
            Objects.equals(completedDate, that.completedDate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(point, that.point) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(imagesId, that.imagesId) &&
            Objects.equals(materialsId, that.materialsId) &&
            Objects.equals(hkjProjectId, that.hkjProjectId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            coverImage,
            description,
            assignedDate,
            expectDate,
            completedDate,
            status,
            priority,
            point,
            notes,
            isDeleted,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            employeeId,
            imagesId,
            materialsId,
            hkjProjectId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjTaskCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalCoverImage().map(f -> "coverImage=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalAssignedDate().map(f -> "assignedDate=" + f + ", ").orElse("") +
            optionalExpectDate().map(f -> "expectDate=" + f + ", ").orElse("") +
            optionalCompletedDate().map(f -> "completedDate=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalPoint().map(f -> "point=" + f + ", ").orElse("") +
            optionalNotes().map(f -> "notes=" + f + ", ").orElse("") +
            optionalIsDeleted().map(f -> "isDeleted=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalEmployeeId().map(f -> "employeeId=" + f + ", ").orElse("") +
            optionalImagesId().map(f -> "imagesId=" + f + ", ").orElse("") +
            optionalMaterialsId().map(f -> "materialsId=" + f + ", ").orElse("") +
            optionalHkjProjectId().map(f -> "hkjProjectId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
