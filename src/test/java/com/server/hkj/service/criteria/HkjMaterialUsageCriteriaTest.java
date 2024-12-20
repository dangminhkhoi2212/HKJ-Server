package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjMaterialUsageCriteriaTest {

    @Test
    void newHkjMaterialUsageCriteriaHasAllFiltersNullTest() {
        var hkjMaterialUsageCriteria = new HkjMaterialUsageCriteria();
        assertThat(hkjMaterialUsageCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjMaterialUsageCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjMaterialUsageCriteria = new HkjMaterialUsageCriteria();

        setAllFilters(hkjMaterialUsageCriteria);

        assertThat(hkjMaterialUsageCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjMaterialUsageCriteriaCopyCreatesNullFilterTest() {
        var hkjMaterialUsageCriteria = new HkjMaterialUsageCriteria();
        var copy = hkjMaterialUsageCriteria.copy();

        assertThat(hkjMaterialUsageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjMaterialUsageCriteria)
        );
    }

    @Test
    void hkjMaterialUsageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjMaterialUsageCriteria = new HkjMaterialUsageCriteria();
        setAllFilters(hkjMaterialUsageCriteria);

        var copy = hkjMaterialUsageCriteria.copy();

        assertThat(hkjMaterialUsageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjMaterialUsageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjMaterialUsageCriteria = new HkjMaterialUsageCriteria();

        assertThat(hkjMaterialUsageCriteria).hasToString("HkjMaterialUsageCriteria{}");
    }

    private static void setAllFilters(HkjMaterialUsageCriteria hkjMaterialUsageCriteria) {
        hkjMaterialUsageCriteria.id();
        hkjMaterialUsageCriteria.usage();
        hkjMaterialUsageCriteria.notes();
        hkjMaterialUsageCriteria.price();
        hkjMaterialUsageCriteria.isDeleted();
        hkjMaterialUsageCriteria.createdBy();
        hkjMaterialUsageCriteria.createdDate();
        hkjMaterialUsageCriteria.lastModifiedBy();
        hkjMaterialUsageCriteria.lastModifiedDate();
        hkjMaterialUsageCriteria.materialId();
        hkjMaterialUsageCriteria.jewelryId();
        hkjMaterialUsageCriteria.taskId();
        hkjMaterialUsageCriteria.distinct();
    }

    private static Condition<HkjMaterialUsageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUsage()) &&
                condition.apply(criteria.getNotes()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getMaterialId()) &&
                condition.apply(criteria.getJewelryId()) &&
                condition.apply(criteria.getTaskId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjMaterialUsageCriteria> copyFiltersAre(
        HkjMaterialUsageCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUsage(), copy.getUsage()) &&
                condition.apply(criteria.getNotes(), copy.getNotes()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getMaterialId(), copy.getMaterialId()) &&
                condition.apply(criteria.getJewelryId(), copy.getJewelryId()) &&
                condition.apply(criteria.getTaskId(), copy.getTaskId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
