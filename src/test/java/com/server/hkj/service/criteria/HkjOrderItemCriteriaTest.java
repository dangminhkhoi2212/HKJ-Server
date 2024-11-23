package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjOrderItemCriteriaTest {

    @Test
    void newHkjOrderItemCriteriaHasAllFiltersNullTest() {
        var hkjOrderItemCriteria = new HkjOrderItemCriteria();
        assertThat(hkjOrderItemCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjOrderItemCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjOrderItemCriteria = new HkjOrderItemCriteria();

        setAllFilters(hkjOrderItemCriteria);

        assertThat(hkjOrderItemCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjOrderItemCriteriaCopyCreatesNullFilterTest() {
        var hkjOrderItemCriteria = new HkjOrderItemCriteria();
        var copy = hkjOrderItemCriteria.copy();

        assertThat(hkjOrderItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjOrderItemCriteria)
        );
    }

    @Test
    void hkjOrderItemCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjOrderItemCriteria = new HkjOrderItemCriteria();
        setAllFilters(hkjOrderItemCriteria);

        var copy = hkjOrderItemCriteria.copy();

        assertThat(hkjOrderItemCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjOrderItemCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjOrderItemCriteria = new HkjOrderItemCriteria();

        assertThat(hkjOrderItemCriteria).hasToString("HkjOrderItemCriteria{}");
    }

    private static void setAllFilters(HkjOrderItemCriteria hkjOrderItemCriteria) {
        hkjOrderItemCriteria.id();
        hkjOrderItemCriteria.quantity();
        hkjOrderItemCriteria.specialRequests();
        hkjOrderItemCriteria.price();
        hkjOrderItemCriteria.isDeleted();
        hkjOrderItemCriteria.notes();
        hkjOrderItemCriteria.createdBy();
        hkjOrderItemCriteria.createdDate();
        hkjOrderItemCriteria.lastModifiedBy();
        hkjOrderItemCriteria.lastModifiedDate();
        hkjOrderItemCriteria.materialId();
        hkjOrderItemCriteria.orderId();
        hkjOrderItemCriteria.productId();
        hkjOrderItemCriteria.categoryId();
        hkjOrderItemCriteria.distinct();
    }

    private static Condition<HkjOrderItemCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getSpecialRequests()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getNotes()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getMaterialId()) &&
                condition.apply(criteria.getOrderId()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getCategoryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjOrderItemCriteria> copyFiltersAre(
        HkjOrderItemCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getSpecialRequests(), copy.getSpecialRequests()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getNotes(), copy.getNotes()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getMaterialId(), copy.getMaterialId()) &&
                condition.apply(criteria.getOrderId(), copy.getOrderId()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getCategoryId(), copy.getCategoryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
