package com.server.hkj.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class HkjTaskAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjTaskAllPropertiesEquals(HkjTask expected, HkjTask actual) {
        assertHkjTaskAutoGeneratedPropertiesEquals(expected, actual);
        assertHkjTaskAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjTaskAllUpdatablePropertiesEquals(HkjTask expected, HkjTask actual) {
        assertHkjTaskUpdatableFieldsEquals(expected, actual);
        assertHkjTaskUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjTaskAutoGeneratedPropertiesEquals(HkjTask expected, HkjTask actual) {
        assertThat(expected)
            .as("Verify HkjTask auto generated properties")
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
    public static void assertHkjTaskUpdatableFieldsEquals(HkjTask expected, HkjTask actual) {
        assertThat(expected)
            .as("Verify HkjTask relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getAssignedDate()).as("check assignedDate").isEqualTo(actual.getAssignedDate()))
            .satisfies(e -> assertThat(e.getExpectDate()).as("check expectDate").isEqualTo(actual.getExpectDate()))
            .satisfies(e -> assertThat(e.getCompletedDate()).as("check completedDate").isEqualTo(actual.getCompletedDate()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(e -> assertThat(e.getPriority()).as("check priority").isEqualTo(actual.getPriority()))
            .satisfies(e -> assertThat(e.getPoint()).as("check point").isEqualTo(actual.getPoint()))
            .satisfies(e -> assertThat(e.getNotes()).as("check notes").isEqualTo(actual.getNotes()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjTaskUpdatableRelationshipsEquals(HkjTask expected, HkjTask actual) {
        assertThat(expected)
            .as("Verify HkjTask relationships")
            .satisfies(e -> assertThat(e.getTemplateStep()).as("check templateStep").isEqualTo(actual.getTemplateStep()))
            .satisfies(e -> assertThat(e.getEmployee()).as("check employee").isEqualTo(actual.getEmployee()))
            .satisfies(e -> assertThat(e.getHkjProject()).as("check hkjProject").isEqualTo(actual.getHkjProject()));
    }
}
