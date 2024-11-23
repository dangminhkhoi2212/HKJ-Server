package com.server.hkj.domain;

import static com.server.hkj.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class HkjOrderItemAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjOrderItemAllPropertiesEquals(HkjOrderItem expected, HkjOrderItem actual) {
        assertHkjOrderItemAutoGeneratedPropertiesEquals(expected, actual);
        assertHkjOrderItemAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjOrderItemAllUpdatablePropertiesEquals(HkjOrderItem expected, HkjOrderItem actual) {
        assertHkjOrderItemUpdatableFieldsEquals(expected, actual);
        assertHkjOrderItemUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjOrderItemAutoGeneratedPropertiesEquals(HkjOrderItem expected, HkjOrderItem actual) {
        assertThat(expected)
            .as("Verify HkjOrderItem auto generated properties")
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
    public static void assertHkjOrderItemUpdatableFieldsEquals(HkjOrderItem expected, HkjOrderItem actual) {
        assertThat(expected)
            .as("Verify HkjOrderItem relevant properties")
            .satisfies(e -> assertThat(e.getQuantity()).as("check quantity").isEqualTo(actual.getQuantity()))
            .satisfies(e -> assertThat(e.getSpecialRequests()).as("check specialRequests").isEqualTo(actual.getSpecialRequests()))
            .satisfies(e -> assertThat(e.getPrice()).as("check price").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getPrice()))
            .satisfies(e -> assertThat(e.getIsDeleted()).as("check isDeleted").isEqualTo(actual.getIsDeleted()))
            .satisfies(e -> assertThat(e.getNotes()).as("check notes").isEqualTo(actual.getNotes()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjOrderItemUpdatableRelationshipsEquals(HkjOrderItem expected, HkjOrderItem actual) {
        assertThat(expected)
            .as("Verify HkjOrderItem relationships")
            .satisfies(e -> assertThat(e.getMaterial()).as("check material").isEqualTo(actual.getMaterial()))
            .satisfies(e -> assertThat(e.getOrder()).as("check order").isEqualTo(actual.getOrder()))
            .satisfies(e -> assertThat(e.getProduct()).as("check product").isEqualTo(actual.getProduct()))
            .satisfies(e -> assertThat(e.getCategory()).as("check category").isEqualTo(actual.getCategory()));
    }
}
