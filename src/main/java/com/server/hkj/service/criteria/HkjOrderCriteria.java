package com.server.hkj.service.criteria;

import com.server.hkj.domain.enumeration.HkjOrderStatus;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.server.hkj.domain.HkjOrder} entity. This class is used
 * in {@link com.server.hkj.web.rest.HkjOrderResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /hkj-orders?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjOrderCriteria implements Serializable, Criteria {

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

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private InstantFilter orderDate;

    private InstantFilter expectedDeliveryDate;

    private InstantFilter actualDeliveryDate;

    private StringFilter specialRequests;

    private HkjOrderStatusFilter status;

    private IntegerFilter customerRating;

    private BigDecimalFilter totalPrice;

    private BigDecimalFilter budget;

    private BigDecimalFilter depositAmount;

    private BooleanFilter isDeleted;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter orderImagesId;

    private LongFilter customerId;

    private LongFilter jewelryId;

    private LongFilter projectId;

    private LongFilter categoryId;

    private Boolean distinct;

    public HkjOrderCriteria() {}

    public HkjOrderCriteria(HkjOrderCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.orderDate = other.optionalOrderDate().map(InstantFilter::copy).orElse(null);
        this.expectedDeliveryDate = other.optionalExpectedDeliveryDate().map(InstantFilter::copy).orElse(null);
        this.actualDeliveryDate = other.optionalActualDeliveryDate().map(InstantFilter::copy).orElse(null);
        this.specialRequests = other.optionalSpecialRequests().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(HkjOrderStatusFilter::copy).orElse(null);
        this.customerRating = other.optionalCustomerRating().map(IntegerFilter::copy).orElse(null);
        this.totalPrice = other.optionalTotalPrice().map(BigDecimalFilter::copy).orElse(null);
        this.budget = other.optionalBudget().map(BigDecimalFilter::copy).orElse(null);
        this.depositAmount = other.optionalDepositAmount().map(BigDecimalFilter::copy).orElse(null);
        this.isDeleted = other.optionalIsDeleted().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.orderImagesId = other.optionalOrderImagesId().map(LongFilter::copy).orElse(null);
        this.customerId = other.optionalCustomerId().map(LongFilter::copy).orElse(null);
        this.jewelryId = other.optionalJewelryId().map(LongFilter::copy).orElse(null);
        this.projectId = other.optionalProjectId().map(LongFilter::copy).orElse(null);
        this.categoryId = other.optionalCategoryId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public HkjOrderCriteria copy() {
        return new HkjOrderCriteria(this);
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

    public InstantFilter getOrderDate() {
        return orderDate;
    }

    public Optional<InstantFilter> optionalOrderDate() {
        return Optional.ofNullable(orderDate);
    }

    public InstantFilter orderDate() {
        if (orderDate == null) {
            setOrderDate(new InstantFilter());
        }
        return orderDate;
    }

    public void setOrderDate(InstantFilter orderDate) {
        this.orderDate = orderDate;
    }

    public InstantFilter getExpectedDeliveryDate() {
        return expectedDeliveryDate;
    }

    public Optional<InstantFilter> optionalExpectedDeliveryDate() {
        return Optional.ofNullable(expectedDeliveryDate);
    }

    public InstantFilter expectedDeliveryDate() {
        if (expectedDeliveryDate == null) {
            setExpectedDeliveryDate(new InstantFilter());
        }
        return expectedDeliveryDate;
    }

    public void setExpectedDeliveryDate(InstantFilter expectedDeliveryDate) {
        this.expectedDeliveryDate = expectedDeliveryDate;
    }

    public InstantFilter getActualDeliveryDate() {
        return actualDeliveryDate;
    }

    public Optional<InstantFilter> optionalActualDeliveryDate() {
        return Optional.ofNullable(actualDeliveryDate);
    }

    public InstantFilter actualDeliveryDate() {
        if (actualDeliveryDate == null) {
            setActualDeliveryDate(new InstantFilter());
        }
        return actualDeliveryDate;
    }

    public void setActualDeliveryDate(InstantFilter actualDeliveryDate) {
        this.actualDeliveryDate = actualDeliveryDate;
    }

    public StringFilter getSpecialRequests() {
        return specialRequests;
    }

    public Optional<StringFilter> optionalSpecialRequests() {
        return Optional.ofNullable(specialRequests);
    }

    public StringFilter specialRequests() {
        if (specialRequests == null) {
            setSpecialRequests(new StringFilter());
        }
        return specialRequests;
    }

    public void setSpecialRequests(StringFilter specialRequests) {
        this.specialRequests = specialRequests;
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

    public IntegerFilter getCustomerRating() {
        return customerRating;
    }

    public Optional<IntegerFilter> optionalCustomerRating() {
        return Optional.ofNullable(customerRating);
    }

    public IntegerFilter customerRating() {
        if (customerRating == null) {
            setCustomerRating(new IntegerFilter());
        }
        return customerRating;
    }

    public void setCustomerRating(IntegerFilter customerRating) {
        this.customerRating = customerRating;
    }

    public BigDecimalFilter getTotalPrice() {
        return totalPrice;
    }

    public Optional<BigDecimalFilter> optionalTotalPrice() {
        return Optional.ofNullable(totalPrice);
    }

    public BigDecimalFilter totalPrice() {
        if (totalPrice == null) {
            setTotalPrice(new BigDecimalFilter());
        }
        return totalPrice;
    }

    public void setTotalPrice(BigDecimalFilter totalPrice) {
        this.totalPrice = totalPrice;
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

    public BigDecimalFilter getDepositAmount() {
        return depositAmount;
    }

    public Optional<BigDecimalFilter> optionalDepositAmount() {
        return Optional.ofNullable(depositAmount);
    }

    public BigDecimalFilter depositAmount() {
        if (depositAmount == null) {
            setDepositAmount(new BigDecimalFilter());
        }
        return depositAmount;
    }

    public void setDepositAmount(BigDecimalFilter depositAmount) {
        this.depositAmount = depositAmount;
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

    public LongFilter getOrderImagesId() {
        return orderImagesId;
    }

    public Optional<LongFilter> optionalOrderImagesId() {
        return Optional.ofNullable(orderImagesId);
    }

    public LongFilter orderImagesId() {
        if (orderImagesId == null) {
            setOrderImagesId(new LongFilter());
        }
        return orderImagesId;
    }

    public void setOrderImagesId(LongFilter orderImagesId) {
        this.orderImagesId = orderImagesId;
    }

    public LongFilter getCustomerId() {
        return customerId;
    }

    public Optional<LongFilter> optionalCustomerId() {
        return Optional.ofNullable(customerId);
    }

    public LongFilter customerId() {
        if (customerId == null) {
            setCustomerId(new LongFilter());
        }
        return customerId;
    }

    public void setCustomerId(LongFilter customerId) {
        this.customerId = customerId;
    }

    public LongFilter getJewelryId() {
        return jewelryId;
    }

    public Optional<LongFilter> optionalJewelryId() {
        return Optional.ofNullable(jewelryId);
    }

    public LongFilter jewelryId() {
        if (jewelryId == null) {
            setJewelryId(new LongFilter());
        }
        return jewelryId;
    }

    public void setJewelryId(LongFilter jewelryId) {
        this.jewelryId = jewelryId;
    }

    public LongFilter getProjectId() {
        return projectId;
    }

    public Optional<LongFilter> optionalProjectId() {
        return Optional.ofNullable(projectId);
    }

    public LongFilter projectId() {
        if (projectId == null) {
            setProjectId(new LongFilter());
        }
        return projectId;
    }

    public void setProjectId(LongFilter projectId) {
        this.projectId = projectId;
    }

    public LongFilter getCategoryId() {
        return categoryId;
    }

    public Optional<LongFilter> optionalCategoryId() {
        return Optional.ofNullable(categoryId);
    }

    public LongFilter categoryId() {
        if (categoryId == null) {
            setCategoryId(new LongFilter());
        }
        return categoryId;
    }

    public void setCategoryId(LongFilter categoryId) {
        this.categoryId = categoryId;
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
        final HkjOrderCriteria that = (HkjOrderCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(orderDate, that.orderDate) &&
            Objects.equals(expectedDeliveryDate, that.expectedDeliveryDate) &&
            Objects.equals(actualDeliveryDate, that.actualDeliveryDate) &&
            Objects.equals(specialRequests, that.specialRequests) &&
            Objects.equals(status, that.status) &&
            Objects.equals(customerRating, that.customerRating) &&
            Objects.equals(totalPrice, that.totalPrice) &&
            Objects.equals(budget, that.budget) &&
            Objects.equals(depositAmount, that.depositAmount) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(orderImagesId, that.orderImagesId) &&
            Objects.equals(customerId, that.customerId) &&
            Objects.equals(jewelryId, that.jewelryId) &&
            Objects.equals(projectId, that.projectId) &&
            Objects.equals(categoryId, that.categoryId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            orderDate,
            expectedDeliveryDate,
            actualDeliveryDate,
            specialRequests,
            status,
            customerRating,
            totalPrice,
            budget,
            depositAmount,
            isDeleted,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            orderImagesId,
            customerId,
            jewelryId,
            projectId,
            categoryId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjOrderCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalOrderDate().map(f -> "orderDate=" + f + ", ").orElse("") +
            optionalExpectedDeliveryDate().map(f -> "expectedDeliveryDate=" + f + ", ").orElse("") +
            optionalActualDeliveryDate().map(f -> "actualDeliveryDate=" + f + ", ").orElse("") +
            optionalSpecialRequests().map(f -> "specialRequests=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalCustomerRating().map(f -> "customerRating=" + f + ", ").orElse("") +
            optionalTotalPrice().map(f -> "totalPrice=" + f + ", ").orElse("") +
            optionalBudget().map(f -> "budget=" + f + ", ").orElse("") +
            optionalDepositAmount().map(f -> "depositAmount=" + f + ", ").orElse("") +
            optionalIsDeleted().map(f -> "isDeleted=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalOrderImagesId().map(f -> "orderImagesId=" + f + ", ").orElse("") +
            optionalCustomerId().map(f -> "customerId=" + f + ", ").orElse("") +
            optionalJewelryId().map(f -> "jewelryId=" + f + ", ").orElse("") +
            optionalProjectId().map(f -> "projectId=" + f + ", ").orElse("") +
            optionalCategoryId().map(f -> "categoryId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
