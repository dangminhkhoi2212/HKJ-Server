package com.server.hkj.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.server.hkj.domain.HkjSalary} entity. This class is used
 * in {@link com.server.hkj.web.rest.HkjSalaryResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /hkj-salaries?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjSalaryCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private BigDecimalFilter salary;

    private StringFilter notes;

    private InstantFilter payDate;

    private BooleanFilter isDeleted;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter employeeId;

    private Boolean distinct;

    public HkjSalaryCriteria() {}

    public HkjSalaryCriteria(HkjSalaryCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.salary = other.optionalSalary().map(BigDecimalFilter::copy).orElse(null);
        this.notes = other.optionalNotes().map(StringFilter::copy).orElse(null);
        this.payDate = other.optionalPayDate().map(InstantFilter::copy).orElse(null);
        this.isDeleted = other.optionalIsDeleted().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.employeeId = other.optionalEmployeeId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public HkjSalaryCriteria copy() {
        return new HkjSalaryCriteria(this);
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

    public BigDecimalFilter getSalary() {
        return salary;
    }

    public Optional<BigDecimalFilter> optionalSalary() {
        return Optional.ofNullable(salary);
    }

    public BigDecimalFilter salary() {
        if (salary == null) {
            setSalary(new BigDecimalFilter());
        }
        return salary;
    }

    public void setSalary(BigDecimalFilter salary) {
        this.salary = salary;
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

    public InstantFilter getPayDate() {
        return payDate;
    }

    public Optional<InstantFilter> optionalPayDate() {
        return Optional.ofNullable(payDate);
    }

    public InstantFilter payDate() {
        if (payDate == null) {
            setPayDate(new InstantFilter());
        }
        return payDate;
    }

    public void setPayDate(InstantFilter payDate) {
        this.payDate = payDate;
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
        final HkjSalaryCriteria that = (HkjSalaryCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(salary, that.salary) &&
            Objects.equals(notes, that.notes) &&
            Objects.equals(payDate, that.payDate) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(employeeId, that.employeeId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            salary,
            notes,
            payDate,
            isDeleted,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            employeeId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjSalaryCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalSalary().map(f -> "salary=" + f + ", ").orElse("") +
            optionalNotes().map(f -> "notes=" + f + ", ").orElse("") +
            optionalPayDate().map(f -> "payDate=" + f + ", ").orElse("") +
            optionalIsDeleted().map(f -> "isDeleted=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalEmployeeId().map(f -> "employeeId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
