package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class HkjTrackSearchImageCriteriaTest {

    @Test
    void newHkjTrackSearchImageCriteriaHasAllFiltersNullTest() {
        var hkjTrackSearchImageCriteria = new HkjTrackSearchImageCriteria();
        assertThat(hkjTrackSearchImageCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void hkjTrackSearchImageCriteriaFluentMethodsCreatesFiltersTest() {
        var hkjTrackSearchImageCriteria = new HkjTrackSearchImageCriteria();

        setAllFilters(hkjTrackSearchImageCriteria);

        assertThat(hkjTrackSearchImageCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void hkjTrackSearchImageCriteriaCopyCreatesNullFilterTest() {
        var hkjTrackSearchImageCriteria = new HkjTrackSearchImageCriteria();
        var copy = hkjTrackSearchImageCriteria.copy();

        assertThat(hkjTrackSearchImageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(hkjTrackSearchImageCriteria)
        );
    }

    @Test
    void hkjTrackSearchImageCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var hkjTrackSearchImageCriteria = new HkjTrackSearchImageCriteria();
        setAllFilters(hkjTrackSearchImageCriteria);

        var copy = hkjTrackSearchImageCriteria.copy();

        assertThat(hkjTrackSearchImageCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(hkjTrackSearchImageCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var hkjTrackSearchImageCriteria = new HkjTrackSearchImageCriteria();

        assertThat(hkjTrackSearchImageCriteria).hasToString("HkjTrackSearchImageCriteria{}");
    }

    private static void setAllFilters(HkjTrackSearchImageCriteria hkjTrackSearchImageCriteria) {
        hkjTrackSearchImageCriteria.id();
        hkjTrackSearchImageCriteria.searchOrder();
        hkjTrackSearchImageCriteria.createdBy();
        hkjTrackSearchImageCriteria.createdDate();
        hkjTrackSearchImageCriteria.lastModifiedBy();
        hkjTrackSearchImageCriteria.lastModifiedDate();
        hkjTrackSearchImageCriteria.userId();
        hkjTrackSearchImageCriteria.jewelryId();
        hkjTrackSearchImageCriteria.distinct();
    }

    private static Condition<HkjTrackSearchImageCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getSearchOrder()) &&
                condition.apply(criteria.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getJewelryId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<HkjTrackSearchImageCriteria> copyFiltersAre(
        HkjTrackSearchImageCriteria copy,
        BiFunction<Object, Object, Boolean> condition
    ) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getSearchOrder(), copy.getSearchOrder()) &&
                condition.apply(criteria.getCreatedBy(), copy.getCreatedBy()) &&
                condition.apply(criteria.getCreatedDate(), copy.getCreatedDate()) &&
                condition.apply(criteria.getLastModifiedBy(), copy.getLastModifiedBy()) &&
                condition.apply(criteria.getLastModifiedDate(), copy.getLastModifiedDate()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getJewelryId(), copy.getJewelryId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
