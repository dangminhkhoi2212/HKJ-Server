package com.server.hkj.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class HkjCategoryAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjCategoryAllPropertiesEquals(HkjCategory expected, HkjCategory actual) {
        assertHkjCategoryAutoGeneratedPropertiesEquals(expected, actual);
        assertHkjCategoryAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjCategoryAllUpdatablePropertiesEquals(HkjCategory expected, HkjCategory actual) {
        assertHkjCategoryUpdatableFieldsEquals(expected, actual);
        assertHkjCategoryUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjCategoryAutoGeneratedPropertiesEquals(HkjCategory expected, HkjCategory actual) {
        assertThat(expected)
            .as("Verify HkjCategory auto generated properties")
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
    public static void assertHkjCategoryUpdatableFieldsEquals(HkjCategory expected, HkjCategory actual) {
        assertThat(expected)
            .as("Verify HkjCategory relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getIsDeleted()).as("check isDeleted").isEqualTo(actual.getIsDeleted()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjCategoryUpdatableRelationshipsEquals(HkjCategory expected, HkjCategory actual) {}
}