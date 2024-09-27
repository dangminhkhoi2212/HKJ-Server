package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjTaskCriteriaTest {

    @Test
    void newHkjTaskCriteriaHasAllFiltersNullTest() {
        var hkjTaskCriteria = new HkjTaskCriteria();
        assertThat(hkjTaskCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjTaskCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjTaskCriteria = new HkjTaskCriteria();

        setAllFilters(hkjTaskCriteria);

        assertThat(hkjTaskCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjTaskCriteriaCopyCreatesNullFilterTest() {
        var hkjTaskCriteria = new HkjTaskCriteria();
        var copy = hkjTaskCriteria.copy();

        assertThat(hkjTaskCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjTaskCriteria)
        );
    }

    @Test
    void hkjTaskCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjTaskCriteria = new HkjTaskCriteria();
        setAllFilters(hkjTaskCriteria);

        var copy = hkjTaskCriteria.copy();

        assertThat(hkjTaskCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjTaskCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjTaskCriteria = new HkjTaskCriteria();

        assertThat(hkjTaskCriteria).hasToString("HkjTaskCriteria{}");
    }

    private static void setAllFilters(HkjTaskCriteria hkjTaskCriteria) {
        hkjTaskCriteria.id();
        hkjTaskCriteria.name();
        hkjTaskCriteria.description();
        hkjTaskCriteria.assignedDate();
        hkjTaskCriteria.expectDate();
        hkjTaskCriteria.completedDate();
        hkjTaskCriteria.status();
        hkjTaskCriteria.priority();
        hkjTaskCriteria.point();
        hkjTaskCriteria.notes();
        hkjTaskCriteria.isDeleted();
        hkjTaskCriteria.createdBy();
        hkjTaskCriteria.createdDate();
        hkjTaskCriteria.lastModifiedBy();
        hkjTaskCriteria.lastModifiedDate();
        hkjTaskCriteria.employeeId();
        hkjTaskCriteria.imagesId();
        hkjTaskCriteria.materialsId();
        hkjTaskCriteria.projectId();
        hkjTaskCriteria.distinct();
    }

    private static Condition<HkjTaskCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getAssignedDate()) &&
                condition.apply(criteria.getExpectDate()) &&
                condition.apply(criteria.getCompletedDate()) &&
                condition.apply(criteria.getStatus()) &&
                condition.apply(criteria.getPriority()) &&
                condition.apply(criteria.getPoint()) &&
                condition.apply(criteria.getNotes()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getEmployeeId()) &&
                condition.apply(criteria.getImagesId()) &&
                condition.apply(criteria.getMaterialsId()) &&
                condition.apply(criteria.getProjectId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjTaskCriteria> copyFiltersAre(HkjTaskCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getAssignedDate(), copy.getAssignedDate()) &&
                condition.apply(criteria.getExpectDate(), copy.getExpectDate()) &&
                condition.apply(criteria.getCompletedDate(), copy.getCompletedDate()) &&
                condition.apply(criteria.getStatus(), copy.getStatus()) &&
                condition.apply(criteria.getPriority(), copy.getPriority()) &&
                condition.apply(criteria.getPoint(), copy.getPoint()) &&
                condition.apply(criteria.getNotes(), copy.getNotes()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getEmployeeId(), copy.getEmployeeId()) &&
                condition.apply(criteria.getImagesId(), copy.getImagesId()) &&
                condition.apply(criteria.getMaterialsId(), copy.getMaterialsId()) &&
                condition.apply(criteria.getProjectId(), copy.getProjectId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
