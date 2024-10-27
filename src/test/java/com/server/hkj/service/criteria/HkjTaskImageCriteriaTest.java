package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjTaskImageCriteriaTest {

    @Test
    void newHkjTaskImageCriteriaHasAllFiltersNullTest() {
        var hkjTaskImageCriteria = new HkjTaskImageCriteria();
        assertThat(hkjTaskImageCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjTaskImageCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjTaskImageCriteria = new HkjTaskImageCriteria();

        setAllFilters(hkjTaskImageCriteria);

        assertThat(hkjTaskImageCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjTaskImageCriteriaCopyCreatesNullFilterTest() {
        var hkjTaskImageCriteria = new HkjTaskImageCriteria();
        var copy = hkjTaskImageCriteria.copy();

        assertThat(hkjTaskImageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjTaskImageCriteria)
        );
    }

    @Test
    void hkjTaskImageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjTaskImageCriteria = new HkjTaskImageCriteria();
        setAllFilters(hkjTaskImageCriteria);

        var copy = hkjTaskImageCriteria.copy();

        assertThat(hkjTaskImageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjTaskImageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjTaskImageCriteria = new HkjTaskImageCriteria();

        assertThat(hkjTaskImageCriteria).hasToString("HkjTaskImageCriteria{}");
    }

    private static void setAllFilters(HkjTaskImageCriteria hkjTaskImageCriteria) {
        hkjTaskImageCriteria.id();
        hkjTaskImageCriteria.url();
        hkjTaskImageCriteria.description();
        hkjTaskImageCriteria.isDeleted();
        hkjTaskImageCriteria.createdBy();
        hkjTaskImageCriteria.createdDate();
        hkjTaskImageCriteria.lastModifiedBy();
        hkjTaskImageCriteria.lastModifiedDate();
        hkjTaskImageCriteria.taskId();
        hkjTaskImageCriteria.distinct();
    }

    private static Condition<HkjTaskImageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUrl()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getTaskId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjTaskImageCriteria> copyFiltersAre(
        HkjTaskImageCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUrl(), copy.getUrl()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getTaskId(), copy.getTaskId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
