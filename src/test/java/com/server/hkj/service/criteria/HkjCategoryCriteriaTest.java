package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjCategoryCriteriaTest {

    @Test
    void newHkjCategoryCriteriaHasAllFiltersNullTest() {
        var hkjCategoryCriteria = new HkjCategoryCriteria();
        assertThat(hkjCategoryCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjCategoryCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjCategoryCriteria = new HkjCategoryCriteria();

        setAllFilters(hkjCategoryCriteria);

        assertThat(hkjCategoryCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjCategoryCriteriaCopyCreatesNullFilterTest() {
        var hkjCategoryCriteria = new HkjCategoryCriteria();
        var copy = hkjCategoryCriteria.copy();

        assertThat(hkjCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjCategoryCriteria)
        );
    }

    @Test
    void hkjCategoryCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjCategoryCriteria = new HkjCategoryCriteria();
        setAllFilters(hkjCategoryCriteria);

        var copy = hkjCategoryCriteria.copy();

        assertThat(hkjCategoryCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjCategoryCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjCategoryCriteria = new HkjCategoryCriteria();

        assertThat(hkjCategoryCriteria).hasToString("HkjCategoryCriteria{}");
    }

    private static void setAllFilters(HkjCategoryCriteria hkjCategoryCriteria) {
        hkjCategoryCriteria.id();
        hkjCategoryCriteria.name();
        hkjCategoryCriteria.isDeleted();
        hkjCategoryCriteria.createdBy();
        hkjCategoryCriteria.createdDate();
        hkjCategoryCriteria.lastModifiedBy();
        hkjCategoryCriteria.lastModifiedDate();
        hkjCategoryCriteria.hkjTemplateId();
        hkjCategoryCriteria.distinct();
    }

    private static Condition<HkjCategoryCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getHkjTemplateId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjCategoryCriteria> copyFiltersAre(HkjCategoryCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getHkjTemplateId(), copy.getHkjTemplateId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
