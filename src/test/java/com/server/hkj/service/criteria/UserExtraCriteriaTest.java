package com.server.hkj.service.criteria;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.function.BiFunction;
import java.util.function.Function;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.Test;

class UserExtraCriteriaTest {

    @Test
    void newUserExtraCriteriaHasAllFiltersNullTest() {
        var userExtraCriteria = new UserExtraCriteria();
        assertThat(userExtraCriteria).is(criteriaFiltersAre(filter -> filter == null));
    }

    @Test
    void userExtraCriteriaFluentMethodsCreatesFiltersTest() {
        var userExtraCriteria = new UserExtraCriteria();

        setAllFilters(userExtraCriteria);

        assertThat(userExtraCriteria).is(criteriaFiltersAre(filter -> filter != null));
    }

    @Test
    void userExtraCriteriaCopyCreatesNullFilterTest() {
        var userExtraCriteria = new UserExtraCriteria();
        var copy = userExtraCriteria.copy();

        assertThat(userExtraCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter == null)),
            criteria -> assertThat(criteria).isEqualTo(userExtraCriteria)
        );
    }

    @Test
    void userExtraCriteriaCopyDuplicatesEveryExistingFilterTest() {
        var userExtraCriteria = new UserExtraCriteria();
        setAllFilters(userExtraCriteria);

        var copy = userExtraCriteria.copy();

        assertThat(userExtraCriteria).satisfies(
            criteria ->
                assertThat(criteria).is(
                    copyFiltersAre(copy, (a, b) -> (a == null || a instanceof Boolean) ? a == b : (a != b && a.equals(b)))
                ),
            criteria -> assertThat(criteria).isEqualTo(copy),
            criteria -> assertThat(criteria).hasSameHashCodeAs(copy)
        );

        assertThat(copy).satisfies(
            criteria -> assertThat(criteria).is(criteriaFiltersAre(filter -> filter != null)),
            criteria -> assertThat(criteria).isEqualTo(userExtraCriteria)
        );
    }

    @Test
    void toStringVerifier() {
        var userExtraCriteria = new UserExtraCriteria();

        assertThat(userExtraCriteria).hasToString("UserExtraCriteria{}");
    }

    private static void setAllFilters(UserExtraCriteria userExtraCriteria) {
        userExtraCriteria.id();
        userExtraCriteria.phone();
        userExtraCriteria.userId();
        userExtraCriteria.distinct();
    }

    private static Condition<UserExtraCriteria> criteriaFiltersAre(Function<Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId()) &&
                condition.apply(criteria.getPhone()) &&
                condition.apply(criteria.getUserId()) &&
                condition.apply(criteria.getDistinct()),
            "every filter matches"
        );
    }

    private static Condition<UserExtraCriteria> copyFiltersAre(UserExtraCriteria copy, BiFunction<Object, Object, Boolean> condition) {
        return new Condition<>(
            criteria ->
                condition.apply(criteria.getId(), copy.getId()) &&
                condition.apply(criteria.getPhone(), copy.getPhone()) &&
                condition.apply(criteria.getUserId(), copy.getUserId()) &&
                condition.apply(criteria.getDistinct(), copy.getDistinct()),
            "every filter matches"
        );
    }
}
