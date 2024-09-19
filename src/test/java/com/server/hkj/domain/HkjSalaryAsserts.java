package com.server.hkj.domain;

import static com.server.hkj.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class HkjSalaryAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjSalaryAllPropertiesEquals(HkjSalary expected, HkjSalary actual) {
        assertHkjSalaryAutoGeneratedPropertiesEquals(expected, actual);
        assertHkjSalaryAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjSalaryAllUpdatablePropertiesEquals(HkjSalary expected, HkjSalary actual) {
        assertHkjSalaryUpdatableFieldsEquals(expected, actual);
        assertHkjSalaryUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjSalaryAutoGeneratedPropertiesEquals(HkjSalary expected, HkjSalary actual) {
        assertThat(expected)
            .as("Verify HkjSalary auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()))
            .satisfies(e -> assertThat(e.getCreatedBy()).as("check createdBy").isEqualTo(actual.getCreatedBy()))
            .satisfies(e -> assertThat(e.getCreatedDate()).as("check createdDate").isEqualTo(actual.getCreatedDate()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjSalaryUpdatableFieldsEquals(HkjSalary expected, HkjSalary actual) {
        assertThat(expected)
            .as("Verify HkjSalary relevant properties")
            .satisfies(e -> assertThat(e.getSalary()).as("check salary").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getSalary()))
            .satisfies(e -> assertThat(e.getIsDeleted()).as("check isDeleted").isEqualTo(actual.getIsDeleted()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjSalaryUpdatableRelationshipsEquals(HkjSalary expected, HkjSalary actual) {
        assertThat(expected)
            .as("Verify HkjSalary relationships")
            .satisfies(e -> assertThat(e.getHkjEmployee()).as("check hkjEmployee").isEqualTo(actual.getHkjEmployee()));
    }
}
