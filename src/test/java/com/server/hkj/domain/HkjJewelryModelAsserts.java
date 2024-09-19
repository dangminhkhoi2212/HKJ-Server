package com.server.hkj.domain;

import static com.server.hkj.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class HkjJewelryModelAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjJewelryModelAllPropertiesEquals(HkjJewelryModel expected, HkjJewelryModel actual) {
        assertHkjJewelryModelAutoGeneratedPropertiesEquals(expected, actual);
        assertHkjJewelryModelAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjJewelryModelAllUpdatablePropertiesEquals(HkjJewelryModel expected, HkjJewelryModel actual) {
        assertHkjJewelryModelUpdatableFieldsEquals(expected, actual);
        assertHkjJewelryModelUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjJewelryModelAutoGeneratedPropertiesEquals(HkjJewelryModel expected, HkjJewelryModel actual) {
        assertThat(expected)
            .as("Verify HkjJewelryModel auto generated properties")
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
    public static void assertHkjJewelryModelUpdatableFieldsEquals(HkjJewelryModel expected, HkjJewelryModel actual) {
        assertThat(expected)
            .as("Verify HkjJewelryModel relevant properties")
            .satisfies(e -> assertThat(e.getName()).as("check name").isEqualTo(actual.getName()))
            .satisfies(e -> assertThat(e.getDescription()).as("check description").isEqualTo(actual.getDescription()))
            .satisfies(e -> assertThat(e.getIsCustom()).as("check isCustom").isEqualTo(actual.getIsCustom()))
            .satisfies(e -> assertThat(e.getWeight()).as("check weight").isEqualTo(actual.getWeight()))
            .satisfies(e -> assertThat(e.getPrice()).as("check price").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getPrice()))
            .satisfies(e -> assertThat(e.getColor()).as("check color").isEqualTo(actual.getColor()))
            .satisfies(e -> assertThat(e.getNotes()).as("check notes").isEqualTo(actual.getNotes()))
            .satisfies(e -> assertThat(e.getIsDeleted()).as("check isDeleted").isEqualTo(actual.getIsDeleted()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjJewelryModelUpdatableRelationshipsEquals(HkjJewelryModel expected, HkjJewelryModel actual) {
        assertThat(expected)
            .as("Verify HkjJewelryModel relationships")
            .satisfies(e -> assertThat(e.getProject()).as("check project").isEqualTo(actual.getProject()));
    }
}
