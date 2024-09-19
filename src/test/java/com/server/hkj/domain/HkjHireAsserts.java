package com.server.hkj.domain;

import static com.server.hkj.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class HkjHireAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjHireAllPropertiesEquals(HkjHire expected, HkjHire actual) {
        assertHkjHireAutoGeneratedPropertiesEquals(expected, actual);
        assertHkjHireAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjHireAllUpdatablePropertiesEquals(HkjHire expected, HkjHire actual) {
        assertHkjHireUpdatableFieldsEquals(expected, actual);
        assertHkjHireUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjHireAutoGeneratedPropertiesEquals(HkjHire expected, HkjHire actual) {
        assertThat(expected)
            .as("Verify HkjHire auto generated properties")
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
    public static void assertHkjHireUpdatableFieldsEquals(HkjHire expected, HkjHire actual) {
        assertThat(expected)
            .as("Verify HkjHire relevant properties")
            .satisfies(e -> assertThat(e.getBeginDate()).as("check beginDate").isEqualTo(actual.getBeginDate()))
            .satisfies(e -> assertThat(e.getEndDate()).as("check endDate").isEqualTo(actual.getEndDate()))
            .satisfies(
                e ->
                    assertThat(e.getBeginSalary())
                        .as("check beginSalary")
                        .usingComparator(bigDecimalCompareTo)
                        .isEqualTo(actual.getBeginSalary())
            )
            .satisfies(e -> assertThat(e.getIsDeleted()).as("check isDeleted").isEqualTo(actual.getIsDeleted()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjHireUpdatableRelationshipsEquals(HkjHire expected, HkjHire actual) {
        assertThat(expected)
            .as("Verify HkjHire relationships")
            .satisfies(e -> assertThat(e.getPosition()).as("check position").isEqualTo(actual.getPosition()))
            .satisfies(e -> assertThat(e.getEmployee()).as("check employee").isEqualTo(actual.getEmployee()));
    }
}
