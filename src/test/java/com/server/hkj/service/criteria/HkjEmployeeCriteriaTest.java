package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjEmployeeCriteriaTest {

    @Test
    void newHkjEmployeeCriteriaHasAllFiltersNullTest() {
        var hkjEmployeeCriteria = new HkjEmployeeCriteria();
        assertThat(hkjEmployeeCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjEmployeeCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjEmployeeCriteria = new HkjEmployeeCriteria();

        setAllFilters(hkjEmployeeCriteria);

        assertThat(hkjEmployeeCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjEmployeeCriteriaCopyCreatesNullFilterTest() {
        var hkjEmployeeCriteria = new HkjEmployeeCriteria();
        var copy = hkjEmployeeCriteria.copy();

        assertThat(hkjEmployeeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjEmployeeCriteria)
        );
    }

    @Test
    void hkjEmployeeCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjEmployeeCriteria = new HkjEmployeeCriteria();
        setAllFilters(hkjEmployeeCriteria);

        var copy = hkjEmployeeCriteria.copy();

        assertThat(hkjEmployeeCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjEmployeeCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjEmployeeCriteria = new HkjEmployeeCriteria();

        assertThat(hkjEmployeeCriteria).hasToString("HkjEmployeeCriteria{}");
    }

    private static void setAllFilters(HkjEmployeeCriteria hkjEmployeeCriteria) {
        hkjEmployeeCriteria.id();
        hkjEmployeeCriteria.notes();
        hkjEmployeeCriteria.isDeleted();
        hkjEmployeeCriteria.createdBy();
        hkjEmployeeCriteria.createdDate();
        hkjEmployeeCriteria.lastModifiedBy();
        hkjEmployeeCriteria.lastModifiedDate();
        hkjEmployeeCriteria.userExtraId();
        hkjEmployeeCriteria.salarysId();
        hkjEmployeeCriteria.hkjHireId();
        hkjEmployeeCriteria.distinct();
    }

    private static Condition<HkjEmployeeCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getNotes()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getUserExtraId()) &&
                condition.apply(criteria.getSalarysId()) &&
                condition.apply(criteria.getHkjHireId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjEmployeeCriteria> copyFiltersAre(HkjEmployeeCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getNotes(), copy.getNotes()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getUserExtraId(), copy.getUserExtraId()) &&
                condition.apply(criteria.getSalarysId(), copy.getSalarysId()) &&
                condition.apply(criteria.getHkjHireId(), copy.getHkjHireId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
