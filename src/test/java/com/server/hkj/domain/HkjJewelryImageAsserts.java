package com.server.hkj.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class HkjJewelryImageAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjJewelryImageAllPropertiesEquals(HkjJewelryImage expected, HkjJewelryImage actual) {
        assertHkjJewelryImageAutoGeneratedPropertiesEquals(expected, actual);
        assertHkjJewelryImageAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjJewelryImageAllUpdatablePropertiesEquals(HkjJewelryImage expected, HkjJewelryImage actual) {
        assertHkjJewelryImageUpdatableFieldsEquals(expected, actual);
        assertHkjJewelryImageUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjJewelryImageAutoGeneratedPropertiesEquals(HkjJewelryImage expected, HkjJewelryImage actual) {
        assertThat(expected)
            .as("Verify HkjJewelryImage auto generated properties")
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
    public static void assertHkjJewelryImageUpdatableFieldsEquals(HkjJewelryImage expected, HkjJewelryImage actual) {
        assertThat(expected)
            .as("Verify HkjJewelryImage relevant properties")
            .satisfies(e -> assertThat(e.getUrl()).as("check url").isEqualTo(actual.getUrl()))
            .satisfies(e -> assertThat(e.getIsSearchImage()).as("check isSearchImage").isEqualTo(actual.getIsSearchImage()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getTags()).as("check tags").isEqualTo(actual.getTags()))
            .satisfies(e -> assertThat(e.getIsDeleted()).as("check isDeleted").isEqualTo(actual.getIsDeleted()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjJewelryImageUpdatableRelationshipsEquals(HkjJewelryImage expected, HkjJewelryImage actual) {
        assertThat(expected)
            .as("Verify HkjJewelryImage relationships")
            .satisfies(e -> assertThat(e.getJewelryModel()).as("check jewelryModel").isEqualTo(actual.getJewelryModel()));
    }
}
