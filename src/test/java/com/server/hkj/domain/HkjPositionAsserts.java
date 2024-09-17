package com.server.hkj.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class HkjPositionAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjPositionAllPropertiesEquals(HkjPosition expected, HkjPosition actual) {
        assertHkjPositionAutoGeneratedPropertiesEquals(expected, actual);
        assertHkjPositionAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjPositionAllUpdatablePropertiesEquals(HkjPosition expected, HkjPosition actual) {
        assertHkjPositionUpdatableFieldsEquals(expected, actual);
        assertHkjPositionUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjPositionAutoGeneratedPropertiesEquals(HkjPosition expected, HkjPosition actual) {
        assertThat(expected)
            .as("Verify HkjPosition auto generated properties")
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
    public static void assertHkjPositionUpdatableFieldsEquals(HkjPosition expected, HkjPosition actual) {
        assertThat(expected)
            .as("Verify HkjPosition relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjPositionUpdatableRelationshipsEquals(HkjPosition expected, HkjPosition actual) {}
}
