package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjCartCriteriaTest {

    @Test
    void newHkjCartCriteriaHasAllFiltersNullTest() {
        var hkjCartCriteria = new HkjCartCriteria();
        assertThat(hkjCartCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjCartCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjCartCriteria = new HkjCartCriteria();

        setAllFilters(hkjCartCriteria);

        assertThat(hkjCartCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjCartCriteriaCopyCreatesNullFilterTest() {
        var hkjCartCriteria = new HkjCartCriteria();
        var copy = hkjCartCriteria.copy();

        assertThat(hkjCartCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjCartCriteria)
        );
    }

    @Test
    void hkjCartCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjCartCriteria = new HkjCartCriteria();
        setAllFilters(hkjCartCriteria);

        var copy = hkjCartCriteria.copy();

        assertThat(hkjCartCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjCartCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjCartCriteria = new HkjCartCriteria();

        assertThat(hkjCartCriteria).hasToString("HkjCartCriteria{}");
    }

    private static void setAllFilters(HkjCartCriteria hkjCartCriteria) {
        hkjCartCriteria.id();
        hkjCartCriteria.quantity();
        hkjCartCriteria.isDeleted();
        hkjCartCriteria.createdBy();
        hkjCartCriteria.createdDate();
        hkjCartCriteria.lastModifiedBy();
        hkjCartCriteria.lastModifiedDate();
        hkjCartCriteria.productId();
        hkjCartCriteria.customerId();
        hkjCartCriteria.distinct();
    }

    private static Condition<HkjCartCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getQuantity()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getProductId()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjCartCriteria> copyFiltersAre(HkjCartCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getQuantity(), copy.getQuantity()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getProductId(), copy.getProductId()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
