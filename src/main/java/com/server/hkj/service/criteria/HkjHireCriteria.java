package com.server.hkj.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import lombok.Data;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.BigDecimalFilter;
import tech.jhipster.service.filter.BooleanFilter;
import tech.jhipster.service.filter.Filter;
import tech.jhipster.service.filter.InstantFilter;
import tech.jhipster.service.filter.LongFilter;
import tech.jhipster.service.filter.StringFilter;

/**
 * Criteria class for the {@link com.server.hkj.domain.HkjHire} entity. This class is used
 * in {@link com.server.hkj.web.rest.HkjHireResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /hkj-hires?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@Data
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjHireCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter beginDate;

    private InstantFilter endDate;

    private BigDecimalFilter beginSalary;

    private StringFilter notes;

    private BooleanFilter isDeleted;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter positionId;

    private LongFilter employeeId;

    private Boolean distinct;
    private StringFilter employeeName;

    private StringFilter positionName;

    public HkjHireCriteria() {}

    public HkjHireCriteria(HkjHireCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.beginDate = other.optionalBeginDate().map(InstantFilter::copy).orElse(null);
        this.endDate = other.optionalEndDate().map(InstantFilter::copy).orElse(null);
        this.beginSalary = other.optionalBeginSalary().map(BigDecimalFilter::copy).orElse(null);
        this.notes = other.optionalNotes().map(StringFilter::copy).orElse(null);
        this.isDeleted = other.optionalIsDeleted().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.positionId = other.optionalPositionId().map(LongFilter::copy).orElse(null);
        this.employeeId = other.optionalEmployeeId().map(LongFilter::copy).orElse(null);
        this.employeeName = other.optionalEmployeeName().map(StringFilter::copy).orElse(null);
        this.positionName = other.optionalPostitonName().map(StringFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public HkjHireCriteria copy() {
        return new HkjHireCriteria(this);
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

    public InstantFilter getBeginDate() {
        return beginDate;
    }

    public Optional<InstantFilter> optionalBeginDate() {
        return Optional.ofNullable(beginDate);
    }

    public InstantFilter beginDate() {
        if (beginDate == null) {
            setBeginDate(new InstantFilter());
        }
        return beginDate;
    }

    public void setBeginDate(InstantFilter beginDate) {
        this.beginDate = beginDate;
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

    public BigDecimalFilter getBeginSalary() {
        return beginSalary;
    }

    public Optional<BigDecimalFilter> optionalBeginSalary() {
        return Optional.ofNullable(beginSalary);
    }

    public BigDecimalFilter beginSalary() {
        if (beginSalary == null) {
            setBeginSalary(new BigDecimalFilter());
        }
        return beginSalary;
    }

    public void setBeginSalary(BigDecimalFilter beginSalary) {
        this.beginSalary = beginSalary;
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

    public LongFilter getPositionId() {
        return positionId;
    }

    public Optional<LongFilter> optionalPositionId() {
        return Optional.ofNullable(positionId);
    }

    public LongFilter positionId() {
        if (positionId == null) {
            setPositionId(new LongFilter());
        }
        return positionId;
    }

    public void setPositionId(LongFilter positionId) {
        this.positionId = positionId;
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

    public Optional<StringFilter> optionalEmployeeName() {
        return Optional.ofNullable(employeeName);
    }

    public Optional<StringFilter> optionalPostitonName() {
        return Optional.ofNullable(positionName);
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
        final HkjHireCriteria that = (HkjHireCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(beginDate, that.beginDate) &&
            Objects.equals(endDate, that.endDate) &&
            Objects.equals(beginSalary, that.beginSalary) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(positionId, that.positionId) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            beginDate,
            endDate,
            beginSalary,
            notes,
            isDeleted,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            positionId,
            employeeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjHireCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalBeginDate().map(f -> "beginDate=" + f + ", ").orElse("") +
            optionalEndDate().map(f -> "endDate=" + f + ", ").orElse("") +
            optionalBeginSalary().map(f -> "beginSalary=" + f + ", ").orElse("") +
            optionalNotes().map(f -> "notes=" + f + ", ").orElse("") +
            optionalIsDeleted().map(f -> "isDeleted=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalPositionId().map(f -> "positionId=" + f + ", ").orElse("") +
            optionalEmployeeId().map(f -> "employeeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
