package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjPositionCriteriaTest {

    @Test
    void newHkjPositionCriteriaHasAllFiltersNullTest() {
        var hkjPositionCriteria = new HkjPositionCriteria();
        assertThat(hkjPositionCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjPositionCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjPositionCriteria = new HkjPositionCriteria();

        setAllFilters(hkjPositionCriteria);

        assertThat(hkjPositionCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjPositionCriteriaCopyCreatesNullFilterTest() {
        var hkjPositionCriteria = new HkjPositionCriteria();
        var copy = hkjPositionCriteria.copy();

        assertThat(hkjPositionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjPositionCriteria)
        );
    }

    @Test
    void hkjPositionCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjPositionCriteria = new HkjPositionCriteria();
        setAllFilters(hkjPositionCriteria);

        var copy = hkjPositionCriteria.copy();

        assertThat(hkjPositionCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjPositionCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjPositionCriteria = new HkjPositionCriteria();

        assertThat(hkjPositionCriteria).hasToString("HkjPositionCriteria{}");
    }

    private static void setAllFilters(HkjPositionCriteria hkjPositionCriteria) {
        hkjPositionCriteria.id();
        hkjPositionCriteria.name();
        hkjPositionCriteria.isDeleted();
        hkjPositionCriteria.createdBy();
        hkjPositionCriteria.createdDate();
        hkjPositionCriteria.lastModifiedBy();
        hkjPositionCriteria.lastModifiedDate();
        hkjPositionCriteria.hireId();
        hkjPositionCriteria.distinct();
    }

    private static Condition<HkjPositionCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getHireId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjPositionCriteria> copyFiltersAre(HkjPositionCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getHireId(), copy.getHireId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
