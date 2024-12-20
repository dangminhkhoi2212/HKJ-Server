package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjOrderCriteriaTest {

    @Test
    void newHkjOrderCriteriaHasAllFiltersNullTest() {
        var hkjOrderCriteria = new HkjOrderCriteria();
        assertThat(hkjOrderCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjOrderCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjOrderCriteria = new HkjOrderCriteria();

        setAllFilters(hkjOrderCriteria);

        assertThat(hkjOrderCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjOrderCriteriaCopyCreatesNullFilterTest() {
        var hkjOrderCriteria = new HkjOrderCriteria();
        var copy = hkjOrderCriteria.copy();

        assertThat(hkjOrderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjOrderCriteria)
        );
    }

    @Test
    void hkjOrderCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjOrderCriteria = new HkjOrderCriteria();
        setAllFilters(hkjOrderCriteria);

        var copy = hkjOrderCriteria.copy();

        assertThat(hkjOrderCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjOrderCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjOrderCriteria = new HkjOrderCriteria();

        assertThat(hkjOrderCriteria).hasToString("HkjOrderCriteria{}");
    }

    private static void setAllFilters(HkjOrderCriteria hkjOrderCriteria) {
        hkjOrderCriteria.id();
        hkjOrderCriteria.orderDate();
        hkjOrderCriteria.expectedDeliveryDate();
        hkjOrderCriteria.actualDeliveryDate();
        hkjOrderCriteria.status();
        hkjOrderCriteria.totalPrice();
        hkjOrderCriteria.isDeleted();
        hkjOrderCriteria.createdBy();
        hkjOrderCriteria.createdDate();
        hkjOrderCriteria.lastModifiedBy();
        hkjOrderCriteria.lastModifiedDate();
        hkjOrderCriteria.customerId();
        hkjOrderCriteria.projectId();
        hkjOrderCriteria.distinct();
    }

    private static Condition<HkjOrderCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getOrderDate()) &&
                condition.apply(criteria.getExpectedDeliveryDate()) &&
                condition.apply(criteria.getActualDeliveryDate()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getTotalPrice()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getCustomerId()) &&
                condition.apply(criteria.getProjectId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjOrderCriteria> copyFiltersAre(HkjOrderCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getOrderDate(), copy.getOrderDate()) &&
                condition.apply(criteria.getExpectedDeliveryDate(), copy.getExpectedDeliveryDate()) &&
                condition.apply(criteria.getActualDeliveryDate(), copy.getActualDeliveryDate()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getTotalPrice(), copy.getTotalPrice()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getCustomerId(), copy.getCustomerId()) &&
                condition.apply(criteria.getProjectId(), copy.getProjectId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
