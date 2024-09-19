package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjSalaryCriteriaTest {

    @Test
    void newHkjSalaryCriteriaHasAllFiltersNullTest() {
        var hkjSalaryCriteria = new HkjSalaryCriteria();
        assertThat(hkjSalaryCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjSalaryCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjSalaryCriteria = new HkjSalaryCriteria();

        setAllFilters(hkjSalaryCriteria);

        assertThat(hkjSalaryCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjSalaryCriteriaCopyCreatesNullFilterTest() {
        var hkjSalaryCriteria = new HkjSalaryCriteria();
        var copy = hkjSalaryCriteria.copy();

        assertThat(hkjSalaryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjSalaryCriteria)
        );
    }

    @Test
    void hkjSalaryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjSalaryCriteria = new HkjSalaryCriteria();
        setAllFilters(hkjSalaryCriteria);

        var copy = hkjSalaryCriteria.copy();

        assertThat(hkjSalaryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjSalaryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjSalaryCriteria = new HkjSalaryCriteria();

        assertThat(hkjSalaryCriteria).hasToString("HkjSalaryCriteria{}");
    }

    private static void setAllFilters(HkjSalaryCriteria hkjSalaryCriteria) {
        hkjSalaryCriteria.id();
        hkjSalaryCriteria.salary();
        hkjSalaryCriteria.isDeleted();
        hkjSalaryCriteria.createdBy();
        hkjSalaryCriteria.createdDate();
        hkjSalaryCriteria.lastModifiedBy();
        hkjSalaryCriteria.lastModifiedDate();
        hkjSalaryCriteria.hkjEmployeeId();
        hkjSalaryCriteria.distinct();
    }

    private static Condition<HkjSalaryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSalary()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getHkjEmployeeId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjSalaryCriteria> copyFiltersAre(HkjSalaryCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSalary(), copy.getSalary()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getHkjEmployeeId(), copy.getHkjEmployeeId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
