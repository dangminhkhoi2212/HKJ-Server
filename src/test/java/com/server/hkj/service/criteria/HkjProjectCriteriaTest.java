package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjProjectCriteriaTest {

    @Test
    void newHkjProjectCriteriaHasAllFiltersNullTest() {
        var hkjProjectCriteria = new HkjProjectCriteria();
        assertThat(hkjProjectCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjProjectCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjProjectCriteria = new HkjProjectCriteria();

        setAllFilters(hkjProjectCriteria);

        assertThat(hkjProjectCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjProjectCriteriaCopyCreatesNullFilterTest() {
        var hkjProjectCriteria = new HkjProjectCriteria();
        var copy = hkjProjectCriteria.copy();

        assertThat(hkjProjectCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjProjectCriteria)
        );
    }

    @Test
    void hkjProjectCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjProjectCriteria = new HkjProjectCriteria();
        setAllFilters(hkjProjectCriteria);

        var copy = hkjProjectCriteria.copy();

        assertThat(hkjProjectCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjProjectCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjProjectCriteria = new HkjProjectCriteria();

        assertThat(hkjProjectCriteria).hasToString("HkjProjectCriteria{}");
    }

    private static void setAllFilters(HkjProjectCriteria hkjProjectCriteria) {
        hkjProjectCriteria.id();
        hkjProjectCriteria.name();
        hkjProjectCriteria.coverImage();
        hkjProjectCriteria.description();
        hkjProjectCriteria.startDate();
        hkjProjectCriteria.expectDate();
        hkjProjectCriteria.endDate();
        hkjProjectCriteria.status();
        hkjProjectCriteria.priority();
        hkjProjectCriteria.actualCost();
        hkjProjectCriteria.qualityCheck();
        hkjProjectCriteria.isDeleted();
        hkjProjectCriteria.createdBy();
        hkjProjectCriteria.createdDate();
        hkjProjectCriteria.lastModifiedBy();
        hkjProjectCriteria.lastModifiedDate();
        hkjProjectCriteria.tasksId();
        hkjProjectCriteria.managerId();
        hkjProjectCriteria.categoryId();
        hkjProjectCriteria.materialId();
        hkjProjectCriteria.distinct();
    }

    private static Condition<HkjProjectCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getCoverImage()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getStartDate()) &&
                condition.apply(criteria.getExpectDate()) &&
                condition.apply(criteria.getEndDate()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getActualCost()) &&
                condition.apply(criteria.getQualityCheck()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getTasksId()) &&
                condition.apply(criteria.getManagerId()) &&
                condition.apply(criteria.getCategoryId()) &&
                condition.apply(criteria.getMaterialId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjProjectCriteria> copyFiltersAre(HkjProjectCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getCoverImage(), copy.getCoverImage()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getStartDate(), copy.getStartDate()) &&
                condition.apply(criteria.getExpectDate(), copy.getExpectDate()) &&
                condition.apply(criteria.getEndDate(), copy.getEndDate()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getActualCost(), copy.getActualCost()) &&
                condition.apply(criteria.getQualityCheck(), copy.getQualityCheck()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getTasksId(), copy.getTasksId()) &&
                condition.apply(criteria.getManagerId(), copy.getManagerId()) &&
                condition.apply(criteria.getCategoryId(), copy.getCategoryId()) &&
                condition.apply(criteria.getMaterialId(), copy.getMaterialId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
