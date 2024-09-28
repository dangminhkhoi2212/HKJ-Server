package com.server.hkj.domain;

import static com.server.hkj.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class HkjProjectAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjProjectAllPropertiesEquals(HkjProject expected, HkjProject actual) {
        assertHkjProjectAutoGeneratedPropertiesEquals(expected, actual);
        assertHkjProjectAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjProjectAllUpdatablePropertiesEquals(HkjProject expected, HkjProject actual) {
        assertHkjProjectUpdatableFieldsEquals(expected, actual);
        assertHkjProjectUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjProjectAutoGeneratedPropertiesEquals(HkjProject expected, HkjProject actual) {
        assertThat(expected)
            .as("Verify HkjProject auto generated properties")
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
    public static void assertHkjProjectUpdatableFieldsEquals(HkjProject expected, HkjProject actual) {
        assertThat(expected)
            .as("Verify HkjProject relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getStartDate()).as("check startDate").isEqualTo(actual.getStartDate()))
            .satisfies(e -> assertThat(e.getExpectDate()).as("check expectDate").isEqualTo(actual.getExpectDate()))
            .satisfies(e -> assertThat(e.getEndDate()).as("check endDate").isEqualTo(actual.getEndDate()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(e -> assertThat(e.getPriority()).as("check priority").isEqualTo(actual.getPriority()))
            .satisfies(e -> assertThat(e.getBudget()).as("check budget").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getBudget()))
            .satisfies(e ->
                assertThat(e.getActualCost()).as("check actualCost").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getActualCost())
            )
            .satisfies(e -> assertThat(e.getQualityCheck()).as("check qualityCheck").isEqualTo(actual.getQualityCheck()))
            .satisfies(e -> assertThat(e.getNotes()).as("check notes").isEqualTo(actual.getNotes()))
            .satisfies(e -> assertThat(e.getIsDeleted()).as("check isDeleted").isEqualTo(actual.getIsDeleted()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjProjectUpdatableRelationshipsEquals(HkjProject expected, HkjProject actual) {
        assertThat(expected)
            .as("Verify HkjProject relationships")
            .satisfies(e -> assertThat(e.getCategory()).as("check category").isEqualTo(actual.getCategory()))
            .satisfies(e -> assertThat(e.getManager()).as("check manager").isEqualTo(actual.getManager()));
    }
}
