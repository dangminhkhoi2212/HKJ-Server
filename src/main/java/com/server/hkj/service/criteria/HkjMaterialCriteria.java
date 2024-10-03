package com.server.hkj.service.criteria;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.server.hkj.domain.HkjMaterial} entity. This class is used
 * in {@link com.server.hkj.web.rest.HkjMaterialResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /hkj-materials?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class HkjMaterialCriteria implements Serializable, Criteria {

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter name;

    private IntegerFilter quantity;

    private StringFilter unit;

    private BigDecimalFilter unitPrice;

    private StringFilter supplier;

    private StringFilter coverImage;

    private BooleanFilter isDeleted;

    private StringFilter createdBy;

    private InstantFilter createdDate;

    private StringFilter lastModifiedBy;

    private InstantFilter lastModifiedDate;

    private LongFilter imagesId;

    private LongFilter hkjMaterialUsageId;

    private Boolean distinct;

    public HkjMaterialCriteria() {}

    public HkjMaterialCriteria(HkjMaterialCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.name = other.optionalName().map(StringFilter::copy).orElse(null);
        this.quantity = other.optionalQuantity().map(IntegerFilter::copy).orElse(null);
        this.unit = other.optionalUnit().map(StringFilter::copy).orElse(null);
        this.unitPrice = other.optionalUnitPrice().map(BigDecimalFilter::copy).orElse(null);
        this.supplier = other.optionalSupplier().map(StringFilter::copy).orElse(null);
        this.coverImage = other.optionalCoverImage().map(StringFilter::copy).orElse(null);
        this.isDeleted = other.optionalIsDeleted().map(BooleanFilter::copy).orElse(null);
        this.createdBy = other.optionalCreatedBy().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(InstantFilter::copy).orElse(null);
        this.lastModifiedBy = other.optionalLastModifiedBy().map(StringFilter::copy).orElse(null);
        this.lastModifiedDate = other.optionalLastModifiedDate().map(InstantFilter::copy).orElse(null);
        this.imagesId = other.optionalImagesId().map(LongFilter::copy).orElse(null);
        this.hkjMaterialUsageId = other.optionalHkjMaterialUsageId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public HkjMaterialCriteria copy() {
        return new HkjMaterialCriteria(this);
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

    public IntegerFilter getQuantity() {
        return quantity;
    }

    public Optional<IntegerFilter> optionalQuantity() {
        return Optional.ofNullable(quantity);
    }

    public IntegerFilter quantity() {
        if (quantity == null) {
            setQuantity(new IntegerFilter());
        }
        return quantity;
    }

    public void setQuantity(IntegerFilter quantity) {
        this.quantity = quantity;
    }

    public StringFilter getUnit() {
        return unit;
    }

    public Optional<StringFilter> optionalUnit() {
        return Optional.ofNullable(unit);
    }

    public StringFilter unit() {
        if (unit == null) {
            setUnit(new StringFilter());
        }
        return unit;
    }

    public void setUnit(StringFilter unit) {
        this.unit = unit;
    }

    public BigDecimalFilter getUnitPrice() {
        return unitPrice;
    }

    public Optional<BigDecimalFilter> optionalUnitPrice() {
        return Optional.ofNullable(unitPrice);
    }

    public BigDecimalFilter unitPrice() {
        if (unitPrice == null) {
            setUnitPrice(new BigDecimalFilter());
        }
        return unitPrice;
    }

    public void setUnitPrice(BigDecimalFilter unitPrice) {
        this.unitPrice = unitPrice;
    }

    public StringFilter getSupplier() {
        return supplier;
    }

    public Optional<StringFilter> optionalSupplier() {
        return Optional.ofNullable(supplier);
    }

    public StringFilter supplier() {
        if (supplier == null) {
            setSupplier(new StringFilter());
        }
        return supplier;
    }

    public void setSupplier(StringFilter supplier) {
        this.supplier = supplier;
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

    public LongFilter getHkjMaterialUsageId() {
        return hkjMaterialUsageId;
    }

    public Optional<LongFilter> optionalHkjMaterialUsageId() {
        return Optional.ofNullable(hkjMaterialUsageId);
    }

    public LongFilter hkjMaterialUsageId() {
        if (hkjMaterialUsageId == null) {
            setHkjMaterialUsageId(new LongFilter());
        }
        return hkjMaterialUsageId;
    }

    public void setHkjMaterialUsageId(LongFilter hkjMaterialUsageId) {
        this.hkjMaterialUsageId = hkjMaterialUsageId;
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
        final HkjMaterialCriteria that = (HkjMaterialCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(name, that.name) &&
            Objects.equals(quantity, that.quantity) &&
            Objects.equals(unit, that.unit) &&
            Objects.equals(unitPrice, that.unitPrice) &&
            Objects.equals(supplier, that.supplier) &&
            Objects.equals(coverImage, that.coverImage) &&
            Objects.equals(isDeleted, that.isDeleted) &&
            Objects.equals(createdBy, that.createdBy) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(lastModifiedBy, that.lastModifiedBy) &&
            Objects.equals(lastModifiedDate, that.lastModifiedDate) &&
            Objects.equals(imagesId, that.imagesId) &&
            Objects.equals(hkjMaterialUsageId, that.hkjMaterialUsageId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            name,
            quantity,
            unit,
            unitPrice,
            supplier,
            coverImage,
            isDeleted,
            createdBy,
            createdDate,
            lastModifiedBy,
            lastModifiedDate,
            imagesId,
            hkjMaterialUsageId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "HkjMaterialCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalName().map(f -> "name=" + f + ", ").orElse("") +
            optionalQuantity().map(f -> "quantity=" + f + ", ").orElse("") +
            optionalUnit().map(f -> "unit=" + f + ", ").orElse("") +
            optionalUnitPrice().map(f -> "unitPrice=" + f + ", ").orElse("") +
            optionalSupplier().map(f -> "supplier=" + f + ", ").orElse("") +
            optionalCoverImage().map(f -> "coverImage=" + f + ", ").orElse("") +
            optionalIsDeleted().map(f -> "isDeleted=" + f + ", ").orElse("") +
            optionalCreatedBy().map(f -> "createdBy=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalLastModifiedBy().map(f -> "lastModifiedBy=" + f + ", ").orElse("") +
            optionalLastModifiedDate().map(f -> "lastModifiedDate=" + f + ", ").orElse("") +
            optionalImagesId().map(f -> "imagesId=" + f + ", ").orElse("") +
            optionalHkjMaterialUsageId().map(f -> "hkjMaterialUsageId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
