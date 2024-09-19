package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjTemplateCriteriaTest {

    @Test
    void newHkjTemplateCriteriaHasAllFiltersNullTest() {
        var hkjTemplateCriteria = new HkjTemplateCriteria();
        assertThat(hkjTemplateCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjTemplateCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjTemplateCriteria = new HkjTemplateCriteria();

        setAllFilters(hkjTemplateCriteria);

        assertThat(hkjTemplateCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjTemplateCriteriaCopyCreatesNullFilterTest() {
        var hkjTemplateCriteria = new HkjTemplateCriteria();
        var copy = hkjTemplateCriteria.copy();

        assertThat(hkjTemplateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjTemplateCriteria)
        );
    }

    @Test
    void hkjTemplateCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjTemplateCriteria = new HkjTemplateCriteria();
        setAllFilters(hkjTemplateCriteria);

        var copy = hkjTemplateCriteria.copy();

        assertThat(hkjTemplateCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjTemplateCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjTemplateCriteria = new HkjTemplateCriteria();

        assertThat(hkjTemplateCriteria).hasToString("HkjTemplateCriteria{}");
    }

    private static void setAllFilters(HkjTemplateCriteria hkjTemplateCriteria) {
        hkjTemplateCriteria.id();
        hkjTemplateCriteria.name();
        hkjTemplateCriteria.isDeleted();
        hkjTemplateCriteria.createdBy();
        hkjTemplateCriteria.createdDate();
        hkjTemplateCriteria.lastModifiedBy();
        hkjTemplateCriteria.lastModifiedDate();
        hkjTemplateCriteria.categoryId();
        hkjTemplateCriteria.stepsId();
        hkjTemplateCriteria.createrId();
        hkjTemplateCriteria.hkjProjectId();
        hkjTemplateCriteria.distinct();
    }

    private static Condition<HkjTemplateCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getCategoryId()) &&
                condition.apply(criteria.getStepsId()) &&
                condition.apply(criteria.getCreaterId()) &&
                condition.apply(criteria.getHkjProjectId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjTemplateCriteria> copyFiltersAre(HkjTemplateCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getCategoryId(), copy.getCategoryId()) &&
                condition.apply(criteria.getStepsId(), copy.getStepsId()) &&
                condition.apply(criteria.getCreaterId(), copy.getCreaterId()) &&
                condition.apply(criteria.getHkjProjectId(), copy.getHkjProjectId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
