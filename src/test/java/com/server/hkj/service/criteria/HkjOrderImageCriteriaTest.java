package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjOrderImageCriteriaTest {

    @Test
    void newHkjOrderImageCriteriaHasAllFiltersNullTest() {
        var hkjOrderImageCriteria = new HkjOrderImageCriteria();
        assertThat(hkjOrderImageCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjOrderImageCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjOrderImageCriteria = new HkjOrderImageCriteria();

        setAllFilters(hkjOrderImageCriteria);

        assertThat(hkjOrderImageCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjOrderImageCriteriaCopyCreatesNullFilterTest() {
        var hkjOrderImageCriteria = new HkjOrderImageCriteria();
        var copy = hkjOrderImageCriteria.copy();

        assertThat(hkjOrderImageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjOrderImageCriteria)
        );
    }

    @Test
    void hkjOrderImageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjOrderImageCriteria = new HkjOrderImageCriteria();
        setAllFilters(hkjOrderImageCriteria);

        var copy = hkjOrderImageCriteria.copy();

        assertThat(hkjOrderImageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjOrderImageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjOrderImageCriteria = new HkjOrderImageCriteria();

        assertThat(hkjOrderImageCriteria).hasToString("HkjOrderImageCriteria{}");
    }

    private static void setAllFilters(HkjOrderImageCriteria hkjOrderImageCriteria) {
        hkjOrderImageCriteria.id();
        hkjOrderImageCriteria.url();
        hkjOrderImageCriteria.isDeleted();
        hkjOrderImageCriteria.createdBy();
        hkjOrderImageCriteria.createdDate();
        hkjOrderImageCriteria.lastModifiedBy();
        hkjOrderImageCriteria.lastModifiedDate();
        hkjOrderImageCriteria.orderId();
        hkjOrderImageCriteria.distinct();
    }

    private static Condition<HkjOrderImageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUrl()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getOrderId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjOrderImageCriteria> copyFiltersAre(
        HkjOrderImageCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUrl(), copy.getUrl()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getOrderId(), copy.getOrderId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
