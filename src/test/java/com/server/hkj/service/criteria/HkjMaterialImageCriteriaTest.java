package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjMaterialImageCriteriaTest {

    @Test
    void newHkjMaterialImageCriteriaHasAllFiltersNullTest() {
        var hkjMaterialImageCriteria = new HkjMaterialImageCriteria();
        assertThat(hkjMaterialImageCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjMaterialImageCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjMaterialImageCriteria = new HkjMaterialImageCriteria();

        setAllFilters(hkjMaterialImageCriteria);

        assertThat(hkjMaterialImageCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjMaterialImageCriteriaCopyCreatesNullFilterTest() {
        var hkjMaterialImageCriteria = new HkjMaterialImageCriteria();
        var copy = hkjMaterialImageCriteria.copy();

        assertThat(hkjMaterialImageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjMaterialImageCriteria)
        );
    }

    @Test
    void hkjMaterialImageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjMaterialImageCriteria = new HkjMaterialImageCriteria();
        setAllFilters(hkjMaterialImageCriteria);

        var copy = hkjMaterialImageCriteria.copy();

        assertThat(hkjMaterialImageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjMaterialImageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjMaterialImageCriteria = new HkjMaterialImageCriteria();

        assertThat(hkjMaterialImageCriteria).hasToString("HkjMaterialImageCriteria{}");
    }

    private static void setAllFilters(HkjMaterialImageCriteria hkjMaterialImageCriteria) {
        hkjMaterialImageCriteria.id();
        hkjMaterialImageCriteria.url();
        hkjMaterialImageCriteria.isDeleted();
        hkjMaterialImageCriteria.createdBy();
        hkjMaterialImageCriteria.createdDate();
        hkjMaterialImageCriteria.lastModifiedBy();
        hkjMaterialImageCriteria.lastModifiedDate();
        hkjMaterialImageCriteria.materialId();
        hkjMaterialImageCriteria.distinct();
    }

    private static Condition<HkjMaterialImageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUrl()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getMaterialId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjMaterialImageCriteria> copyFiltersAre(
        HkjMaterialImageCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUrl(), copy.getUrl()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getMaterialId(), copy.getMaterialId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
