package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjTemplateStepCriteriaTest {

    @Test
    void newHkjTemplateStepCriteriaHasAllFiltersNullTest() {
        var hkjTemplateStepCriteria = new HkjTemplateStepCriteria();
        assertThat(hkjTemplateStepCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjTemplateStepCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjTemplateStepCriteria = new HkjTemplateStepCriteria();

        setAllFilters(hkjTemplateStepCriteria);

        assertThat(hkjTemplateStepCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjTemplateStepCriteriaCopyCreatesNullFilterTest() {
        var hkjTemplateStepCriteria = new HkjTemplateStepCriteria();
        var copy = hkjTemplateStepCriteria.copy();

        assertThat(hkjTemplateStepCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjTemplateStepCriteria)
        );
    }

    @Test
    void hkjTemplateStepCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjTemplateStepCriteria = new HkjTemplateStepCriteria();
        setAllFilters(hkjTemplateStepCriteria);

        var copy = hkjTemplateStepCriteria.copy();

        assertThat(hkjTemplateStepCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjTemplateStepCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjTemplateStepCriteria = new HkjTemplateStepCriteria();

        assertThat(hkjTemplateStepCriteria).hasToString("HkjTemplateStepCriteria{}");
    }

    private static void setAllFilters(HkjTemplateStepCriteria hkjTemplateStepCriteria) {
        hkjTemplateStepCriteria.id();
        hkjTemplateStepCriteria.name();
        hkjTemplateStepCriteria.isDeleted();
        hkjTemplateStepCriteria.createdBy();
        hkjTemplateStepCriteria.createdDate();
        hkjTemplateStepCriteria.lastModifiedBy();
        hkjTemplateStepCriteria.lastModifiedDate();
        hkjTemplateStepCriteria.hkjTaskId();
        hkjTemplateStepCriteria.hkjTemplateId();
        hkjTemplateStepCriteria.distinct();
    }

    private static Condition<HkjTemplateStepCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getHkjTaskId()) &&
                condition.apply(criteria.getHkjTemplateId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjTemplateStepCriteria> copyFiltersAre(
        HkjTemplateStepCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getHkjTaskId(), copy.getHkjTaskId()) &&
                condition.apply(criteria.getHkjTemplateId(), copy.getHkjTemplateId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
