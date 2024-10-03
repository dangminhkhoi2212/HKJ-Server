package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjJewelryModelCriteriaTest {

    @Test
    void newHkjJewelryModelCriteriaHasAllFiltersNullTest() {
        var hkjJewelryModelCriteria = new HkjJewelryModelCriteria();
        assertThat(hkjJewelryModelCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjJewelryModelCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjJewelryModelCriteria = new HkjJewelryModelCriteria();

        setAllFilters(hkjJewelryModelCriteria);

        assertThat(hkjJewelryModelCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjJewelryModelCriteriaCopyCreatesNullFilterTest() {
        var hkjJewelryModelCriteria = new HkjJewelryModelCriteria();
        var copy = hkjJewelryModelCriteria.copy();

        assertThat(hkjJewelryModelCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjJewelryModelCriteria)
        );
    }

    @Test
    void hkjJewelryModelCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjJewelryModelCriteria = new HkjJewelryModelCriteria();
        setAllFilters(hkjJewelryModelCriteria);

        var copy = hkjJewelryModelCriteria.copy();

        assertThat(hkjJewelryModelCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjJewelryModelCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjJewelryModelCriteria = new HkjJewelryModelCriteria();

        assertThat(hkjJewelryModelCriteria).hasToString("HkjJewelryModelCriteria{}");
    }

    private static void setAllFilters(HkjJewelryModelCriteria hkjJewelryModelCriteria) {
        hkjJewelryModelCriteria.id();
        hkjJewelryModelCriteria.name();
        hkjJewelryModelCriteria.description();
        hkjJewelryModelCriteria.coverImage();
        hkjJewelryModelCriteria.isCustom();
        hkjJewelryModelCriteria.weight();
        hkjJewelryModelCriteria.price();
        hkjJewelryModelCriteria.color();
        hkjJewelryModelCriteria.notes();
        hkjJewelryModelCriteria.isDeleted();
        hkjJewelryModelCriteria.active();
        hkjJewelryModelCriteria.createdBy();
        hkjJewelryModelCriteria.createdDate();
        hkjJewelryModelCriteria.lastModifiedBy();
        hkjJewelryModelCriteria.lastModifiedDate();
        hkjJewelryModelCriteria.projectId();
        hkjJewelryModelCriteria.imagesId();
        hkjJewelryModelCriteria.distinct();
    }

    private static Condition<HkjJewelryModelCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getName()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getCoverImage()) &&
                condition.apply(criteria.getIsCustom()) &&
                condition.apply(criteria.getWeight()) &&
                condition.apply(criteria.getPrice()) &&
                condition.apply(criteria.getColor()) &&
                condition.apply(criteria.getNotes()) &&
                condition.apply(criteria.getIsDeleted()) &&
                condition.apply(criteria.getActive()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getProjectId()) &&
                condition.apply(criteria.getImagesId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjJewelryModelCriteria> copyFiltersAre(
        HkjJewelryModelCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getName(), copy.getName()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getCoverImage(), copy.getCoverImage()) &&
                condition.apply(criteria.getIsCustom(), copy.getIsCustom()) &&
                condition.apply(criteria.getWeight(), copy.getWeight()) &&
                condition.apply(criteria.getPrice(), copy.getPrice()) &&
                condition.apply(criteria.getColor(), copy.getColor()) &&
                condition.apply(criteria.getNotes(), copy.getNotes()) &&
                condition.apply(criteria.getIsDeleted(), copy.getIsDeleted()) &&
                condition.apply(criteria.getActive(), copy.getActive()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getProjectId(), copy.getProjectId()) &&
                condition.apply(criteria.getImagesId(), copy.getImagesId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
