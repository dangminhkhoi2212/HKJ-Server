package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjHireCriteriaTest {

    @Test
    void newHkjHireCriteriaHasAllFiltersNullTest() {
        var hkjHireCriteria = new HkjHireCriteria();
        assertThat(hkjHireCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjHireCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjHireCriteria = new HkjHireCriteria();

        setAllFilters(hkjHireCriteria);

        assertThat(hkjHireCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjHireCriteriaCopyCreatesNullFilterTest() {
        var hkjHireCriteria = new HkjHireCriteria();
        var copy = hkjHireCriteria.copy();

        assertThat(hkjHireCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjHireCriteria)
        );
    }

    @Test
    void hkjHireCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjHireCriteria = new HkjHireCriteria();
        setAllFilters(hkjHireCriteria);

        var copy = hkjHireCriteria.copy();

        assertThat(hkjHireCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjHireCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjHireCriteria = new HkjHireCriteria();

        assertThat(hkjHireCriteria).hasToString("HkjHireCriteria{}");
    }

    private static void setAllFilters(HkjHireCriteria hkjHireCriteria) {
        hkjHireCriteria.id();
        hkjHireCriteria.beginDate();
        hkjHireCriteria.endDate();
        hkjHireCriteria.beginSalary();
        hkjHireCriteria.notes();
        hkjHireCriteria.isDeleted();
        hkjHireCriteria.createdBy();
        hkjHireCriteria.createdDate();
        hkjHireCriteria.lastModifiedBy();
        hkjHireCriteria.lastModifiedDate();
        hkjHireCriteria.positionId();
        hkjHireCriteria.employeeId();
        hkjHireCriteria.distinct();
    }

    private static Condition<HkjHireCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getBeginDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getBeginSalary()) &&
                condition.apply(criteria.getNotes()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getPositionId()) &&
                condition.apply(criteria.getEmployeeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjHireCriteria> copyFiltersAre(HkjHireCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getBeginDate(), copy.getBeginDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getBeginSalary(), copy.getBeginSalary()) &&
                condition.apply(criteria.getNotes(), copy.getNotes()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getPositionId(), copy.getPositionId()) &&
                condition.apply(criteria.getEmployeeId(), copy.getEmployeeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
