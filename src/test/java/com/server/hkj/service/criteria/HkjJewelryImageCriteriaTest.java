package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjJewelryImageCriteriaTest {

    @Test
    void newHkjJewelryImageCriteriaHasAllFiltersNullTest() {
        var hkjJewelryImageCriteria = new HkjJewelryImageCriteria();
        assertThat(hkjJewelryImageCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjJewelryImageCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjJewelryImageCriteria = new HkjJewelryImageCriteria();

        setAllFilters(hkjJewelryImageCriteria);

        assertThat(hkjJewelryImageCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjJewelryImageCriteriaCopyCreatesNullFilterTest() {
        var hkjJewelryImageCriteria = new HkjJewelryImageCriteria();
        var copy = hkjJewelryImageCriteria.copy();

        assertThat(hkjJewelryImageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjJewelryImageCriteria)
        );
    }

    @Test
    void hkjJewelryImageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjJewelryImageCriteria = new HkjJewelryImageCriteria();
        setAllFilters(hkjJewelryImageCriteria);

        var copy = hkjJewelryImageCriteria.copy();

        assertThat(hkjJewelryImageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjJewelryImageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjJewelryImageCriteria = new HkjJewelryImageCriteria();

        assertThat(hkjJewelryImageCriteria).hasToString("HkjJewelryImageCriteria{}");
    }

    private static void setAllFilters(HkjJewelryImageCriteria hkjJewelryImageCriteria) {
        hkjJewelryImageCriteria.id();
        hkjJewelryImageCriteria.url();
        hkjJewelryImageCriteria.isSearchImage();
        hkjJewelryImageCriteria.description();
        hkjJewelryImageCriteria.tags();
        hkjJewelryImageCriteria.createdBy();
        hkjJewelryImageCriteria.createdDate();
        hkjJewelryImageCriteria.lastModifiedBy();
        hkjJewelryImageCriteria.lastModifiedDate();
        hkjJewelryImageCriteria.uploadedById();
        hkjJewelryImageCriteria.jewelryModelId();
        hkjJewelryImageCriteria.distinct();
    }

    private static Condition<HkjJewelryImageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getUrl()) &&
                condition.apply(criteria.getIsSearchImage()) &&
                condition.apply(criteria.getDescription()) &&
                condition.apply(criteria.getTags()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getUploadedById()) &&
                condition.apply(criteria.getJewelryModelId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjJewelryImageCriteria> copyFiltersAre(
        HkjJewelryImageCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getUrl(), copy.getUrl()) &&
                condition.apply(criteria.getIsSearchImage(), copy.getIsSearchImage()) &&
                condition.apply(criteria.getDescription(), copy.getDescription()) &&
                condition.apply(criteria.getTags(), copy.getTags()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getUploadedById(), copy.getUploadedById()) &&
                condition.apply(criteria.getJewelryModelId(), copy.getJewelryModelId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
