package com.server.hkj.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class HkjOrderImageAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjOrderImageAllPropertiesEquals(HkjOrderImage expected, HkjOrderImage actual) {
        assertHkjOrderImageAutoGeneratedPropertiesEquals(expected, actual);
        assertHkjOrderImageAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjOrderImageAllUpdatablePropertiesEquals(HkjOrderImage expected, HkjOrderImage actual) {
        assertHkjOrderImageUpdatableFieldsEquals(expected, actual);
        assertHkjOrderImageUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjOrderImageAutoGeneratedPropertiesEquals(HkjOrderImage expected, HkjOrderImage actual) {
        assertThat(expected)
            .as("Verify HkjOrderImage auto generated properties")
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
    public static void assertHkjOrderImageUpdatableFieldsEquals(HkjOrderImage expected, HkjOrderImage actual) {
        assertThat(expected)
            .as("Verify HkjOrderImage relevant properties")
            .satisfies(e -> assertThat(e.getUrl()).as("check url").isEqualTo(actual.getUrl()))
            .satisfies(e -> assertThat(e.getIsDeleted()).as("check isDeleted").isEqualTo(actual.getIsDeleted()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjOrderImageUpdatableRelationshipsEquals(HkjOrderImage expected, HkjOrderImage actual) {
        assertThat(expected)
            .as("Verify HkjOrderImage relationships")
            .satisfies(e -> assertThat(e.getHkjOrder()).as("check hkjOrder").isEqualTo(actual.getHkjOrder()));
    }
}