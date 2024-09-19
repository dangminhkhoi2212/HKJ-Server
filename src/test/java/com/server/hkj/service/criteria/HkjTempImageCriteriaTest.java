package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjTempImageCriteriaTest {

    @Test
    void newHkjTempImageCriteriaHasAllFiltersNullTest() {
        var hkjTempImageCriteria = new HkjTempImageCriteria();
        assertThat(hkjTempImageCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjTempImageCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjTempImageCriteria = new HkjTempImageCriteria();

        setAllFilters(hkjTempImageCriteria);

        assertThat(hkjTempImageCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjTempImageCriteriaCopyCreatesNullFilterTest() {
        var hkjTempImageCriteria = new HkjTempImageCriteria();
        var copy = hkjTempImageCriteria.copy();

        assertThat(hkjTempImageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjTempImageCriteria)
        );
    }

    @Test
    void hkjTempImageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjTempImageCriteria = new HkjTempImageCriteria();
        setAllFilters(hkjTempImageCriteria);

        var copy = hkjTempImageCriteria.copy();

        assertThat(hkjTempImageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjTempImageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjTempImageCriteria = new HkjTempImageCriteria();

        assertThat(hkjTempImageCriteria).hasToString("HkjTempImageCriteria{}");
    }

    private static void setAllFilters(HkjTempImageCriteria hkjTempImageCriteria) {
        hkjTempImageCriteria.id();
        hkjTempImageCriteria.url();
        hkjTempImageCriteria.isUsed();
        hkjTempImageCriteria.isDeleted();
        hkjTempImageCriteria.createdBy();
        hkjTempImageCriteria.createdDate();
        hkjTempImageCriteria.lastModifiedBy();
        hkjTempImageCriteria.lastModifiedDate();
        hkjTempImageCriteria.distinct();
    }

    private static Condition<HkjTempImageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUrl()) &&
                condition.apply(criteria.getIsUsed()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjTempImageCriteria> copyFiltersAre(
        HkjTempImageCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUrl(), copy.getUrl()) &&
                condition.apply(criteria.getIsUsed(), copy.getIsUsed()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
