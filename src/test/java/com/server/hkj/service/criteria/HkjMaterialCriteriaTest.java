package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjMaterialCriteriaTest {

    @Test
    void newHkjMaterialCriteriaHasAllFiltersNullTest() {
        var hkjMaterialCriteria = new HkjMaterialCriteria();
        assertThat(hkjMaterialCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjMaterialCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjMaterialCriteria = new HkjMaterialCriteria();

        setAllFilters(hkjMaterialCriteria);

        assertThat(hkjMaterialCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjMaterialCriteriaCopyCreatesNullFilterTest() {
        var hkjMaterialCriteria = new HkjMaterialCriteria();
        var copy = hkjMaterialCriteria.copy();

        assertThat(hkjMaterialCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjMaterialCriteria)
        );
    }

    @Test
    void hkjMaterialCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjMaterialCriteria = new HkjMaterialCriteria();
        setAllFilters(hkjMaterialCriteria);

        var copy = hkjMaterialCriteria.copy();

        assertThat(hkjMaterialCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjMaterialCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjMaterialCriteria = new HkjMaterialCriteria();

        assertThat(hkjMaterialCriteria).hasToString("HkjMaterialCriteria{}");
    }

    private static void setAllFilters(HkjMaterialCriteria hkjMaterialCriteria) {
        hkjMaterialCriteria.id();
        hkjMaterialCriteria.name();
        hkjMaterialCriteria.quantity();
        hkjMaterialCriteria.unit();
        hkjMaterialCriteria.unitPrice();
        hkjMaterialCriteria.supplier();
        hkjMaterialCriteria.coverImage();
        hkjMaterialCriteria.isDeleted();
        hkjMaterialCriteria.createdBy();
        hkjMaterialCriteria.createdDate();
        hkjMaterialCriteria.lastModifiedBy();
        hkjMaterialCriteria.lastModifiedDate();
        hkjMaterialCriteria.imagesId();
        hkjMaterialCriteria.hkjMaterialUsageId();
        hkjMaterialCriteria.distinct();
    }

    private static Condition<HkjMaterialCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getUnit()) &&
                condition.apply(criteria.getUnitPrice()) &&
                condition.apply(criteria.getSupplier()) &&
                condition.apply(criteria.getCoverImage()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getImagesId()) &&
                condition.apply(criteria.getHkjMaterialUsageId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjMaterialCriteria> copyFiltersAre(HkjMaterialCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getUnit(), copy.getUnit()) &&
                condition.apply(criteria.getUnitPrice(), copy.getUnitPrice()) &&
                condition.apply(criteria.getSupplier(), copy.getSupplier()) &&
                condition.apply(criteria.getCoverImage(), copy.getCoverImage()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getImagesId(), copy.getImagesId()) &&
                condition.apply(criteria.getHkjMaterialUsageId(), copy.getHkjMaterialUsageId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
