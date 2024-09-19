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
 * Criteria class for the {@link com.server.hkj.domain.HkjProject} entity. This class is used
 * in {@link com.server.hkj.web.rest.HkjProjectResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /hkj-projects?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjProjectCriteria implements Serializable, Criteria {

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

    private StringFilter description;

    private InstantFilter startDate;

    private InstantFilter expectDate;

    private InstantFilter endDate;

    private HkjOrderStatusFilter status;

    private HkjPriorityFilter priority;

    private BigDecimalFilter budget;

    private BigDecimalFilter actualCost;

    private BooleanFilter qualityCheck;

    private StringFilter notes;

    private BooleanFilter isDeleted;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter templateId;

    private LongFilter tasksId;

    private LongFilter managerId;

    private LongFilter hkjJewelryModelId;

    private LongFilter hkjOrderId;

    private Boolean distinct;

    public HkjProjectCriteria() {}

    public HkjProjectCriteria(HkjProjectCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.description = other.optionalDescription().map(StringFilter::copy).orElse(null);
        this.startDate = other.optionalStartDate().map(InstantFilter::copy).orElse(null);
        this.expectDate = other.optionalExpectDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(HkjOrderStatusFilter::copy).orElse(null);
        this.priority = other.optionalPriority().map(HkjPriorityFilter::copy).orElse(null);
        this.budget = other.optionalBudget().map(BigDecimalFilter::copy).orElse(null);
        this.actualCost = other.optionalActualCost().map(BigDecimalFilter::copy).orElse(null);
        this.qualityCheck = other.optionalQualityCheck().map(BooleanFilter::copy).orElse(null);
        this.notes = other.optionalNotes().map(StringFilter::copy).orElse(null);
        this.isDeleted = other.optionalIsDeleted().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.templateId = other.optionalTemplateId().map(LongFilter::copy).orElse(null);
        this.tasksId = other.optionalTasksId().map(LongFilter::copy).orElse(null);
        this.managerId = other.optionalManagerId().map(LongFilter::copy).orElse(null);
        this.hkjJewelryModelId = other.optionalHkjJewelryModelId().map(LongFilter::copy).orElse(null);
        this.hkjOrderId = other.optionalHkjOrderId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public HkjProjectCriteria copy() {
        return new HkjProjectCriteria(this);
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

    public InstantFilter getStartDate() {
        return startDate;
    }

    public Optional<InstantFilter> optionalStartDate() {
        return Optional.ofNullable(startDate);
    }

    public InstantFilter startDate() {
        if (startDate == null) {
            setStartDate(new InstantFilter());
        }
        return startDate;
    }

    public void setStartDate(InstantFilter startDate) {
        this.startDate = startDate;
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

    public InstantFilter getEndDate() {
        return endDate;
    }

    public Optional<InstantFilter> optionalEndDate() {
        return Optional.ofNullable(endDate);
    }

    public InstantFilter endDate() {
        if (endDate == null) {
            setEndDate(new InstantFilter());
        }
        return endDate;
    }

    public void setEndDate(InstantFilter endDate) {
        this.endDate = endDate;
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

    public BigDecimalFilter getBudget() {
        return budget;
    }

    public Optional<BigDecimalFilter> optionalBudget() {
        return Optional.ofNullable(budget);
    }

    public BigDecimalFilter budget() {
        if (budget == null) {
            setBudget(new BigDecimalFilter());
        }
        return budget;
    }

    public void setBudget(BigDecimalFilter budget) {
        this.budget = budget;
    }

    public BigDecimalFilter getActualCost() {
        return actualCost;
    }

    public Optional<BigDecimalFilter> optionalActualCost() {
        return Optional.ofNullable(actualCost);
    }

    public BigDecimalFilter actualCost() {
        if (actualCost == null) {
            setActualCost(new BigDecimalFilter());
        }
        return actualCost;
    }

    public void setActualCost(BigDecimalFilter actualCost) {
        this.actualCost = actualCost;
    }

    public BooleanFilter getQualityCheck() {
        return qualityCheck;
    }

    public Optional<BooleanFilter> optionalQualityCheck() {
        return Optional.ofNullable(qualityCheck);
    }

    public BooleanFilter qualityCheck() {
        if (qualityCheck == null) {
            setQualityCheck(new BooleanFilter());
        }
        return qualityCheck;
    }

    public void setQualityCheck(BooleanFilter qualityCheck) {
        this.qualityCheck = qualityCheck;
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

    public LongFilter getTemplateId() {
        return templateId;
    }

    public Optional<LongFilter> optionalTemplateId() {
        return Optional.ofNullable(templateId);
    }

    public LongFilter templateId() {
        if (templateId == null) {
            setTemplateId(new LongFilter());
        }
        return templateId;
    }

    public void setTemplateId(LongFilter templateId) {
        this.templateId = templateId;
    }

    public LongFilter getTasksId() {
        return tasksId;
    }

    public Optional<LongFilter> optionalTasksId() {
        return Optional.ofNullable(tasksId);
    }

    public LongFilter tasksId() {
        if (tasksId == null) {
            setTasksId(new LongFilter());
        }
        return tasksId;
    }

    public void setTasksId(LongFilter tasksId) {
        this.tasksId = tasksId;
    }

    public LongFilter getManagerId() {
        return managerId;
    }

    public Optional<LongFilter> optionalManagerId() {
        return Optional.ofNullable(managerId);
    }

    public LongFilter managerId() {
        if (managerId == null) {
            setManagerId(new LongFilter());
        }
        return managerId;
    }

    public void setManagerId(LongFilter managerId) {
        this.managerId = managerId;
    }

    public LongFilter getHkjJewelryModelId() {
        return hkjJewelryModelId;
    }

    public Optional<LongFilter> optionalHkjJewelryModelId() {
        return Optional.ofNullable(hkjJewelryModelId);
    }

    public LongFilter hkjJewelryModelId() {
        if (hkjJewelryModelId == null) {
            setHkjJewelryModelId(new LongFilter());
        }
        return hkjJewelryModelId;
    }

    public void setHkjJewelryModelId(LongFilter hkjJewelryModelId) {
        this.hkjJewelryModelId = hkjJewelryModelId;
    }

    public LongFilter getHkjOrderId() {
        return hkjOrderId;
    }

    public Optional<LongFilter> optionalHkjOrderId() {
        return Optional.ofNullable(hkjOrderId);
    }

    public LongFilter hkjOrderId() {
        if (hkjOrderId == null) {
            setHkjOrderId(new LongFilter());
        }
        return hkjOrderId;
    }

    public void setHkjOrderId(LongFilter hkjOrderId) {
        this.hkjOrderId = hkjOrderId;
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
        final HkjProjectCriteria that = (HkjProjectCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(description, that.description) &&
            Objects.equals(startDate, that.startDate) &&
            Objects.equals(expectDate, that.expectDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(status, that.status) &&
            Objects.equals(priority, that.priority) &&
            Objects.equals(budget, that.budget) &&
            Objects.equals(actualCost, that.actualCost) &&
            Objects.equals(qualityCheck, that.qualityCheck) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(templateId, that.templateId) &&
            Objects.equals(tasksId, that.tasksId) &&
            Objects.equals(managerId, that.managerId) &&
            Objects.equals(hkjJewelryModelId, that.hkjJewelryModelId) &&
            Objects.equals(hkjOrderId, that.hkjOrderId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            description,
            startDate,
            expectDate,
            endDate,
            status,
            priority,
            budget,
            actualCost,
            qualityCheck,
            notes,
            isDeleted,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            templateId,
            tasksId,
            managerId,
            hkjJewelryModelId,
            hkjOrderId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjProjectCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalDescription().map(f -> "description=" + f + ", ").orElse("") +
            optionalStartDate().map(f -> "startDate=" + f + ", ").orElse("") +
            optionalExpectDate().map(f -> "expectDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalPriority().map(f -> "priority=" + f + ", ").orElse("") +
            optionalBudget().map(f -> "budget=" + f + ", ").orElse("") +
            optionalActualCost().map(f -> "actualCost=" + f + ", ").orElse("") +
            optionalQualityCheck().map(f -> "qualityCheck=" + f + ", ").orElse("") +
            optionalNotes().map(f -> "notes=" + f + ", ").orElse("") +
            optionalIsDeleted().map(f -> "isDeleted=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalTemplateId().map(f -> "templateId=" + f + ", ").orElse("") +
            optionalTasksId().map(f -> "tasksId=" + f + ", ").orElse("") +
            optionalManagerId().map(f -> "managerId=" + f + ", ").orElse("") +
            optionalHkjJewelryModelId().map(f -> "hkjJewelryModelId=" + f + ", ").orElse("") +
            optionalHkjOrderId().map(f -> "hkjOrderId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
