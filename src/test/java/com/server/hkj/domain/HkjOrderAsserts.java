package com.server.hkj.domain;

import static com.server.hkj.domain.AssertUtils.bigDecimalCompareTo;
import static org.assertj.core.api.Assertions.assertThat;

public class HkjOrderAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjOrderAllPropertiesEquals(HkjOrder expected, HkjOrder actual) {
        assertHkjOrderAutoGeneratedPropertiesEquals(expected, actual);
        assertHkjOrderAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjOrderAllUpdatablePropertiesEquals(HkjOrder expected, HkjOrder actual) {
        assertHkjOrderUpdatableFieldsEquals(expected, actual);
        assertHkjOrderUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjOrderAutoGeneratedPropertiesEquals(HkjOrder expected, HkjOrder actual) {
        assertThat(expected)
            .as("Verify HkjOrder auto generated properties")
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
    public static void assertHkjOrderUpdatableFieldsEquals(HkjOrder expected, HkjOrder actual) {
        assertThat(expected)
            .as("Verify HkjOrder relevant properties")
            .satisfies(e -> assertThat(e.getOrderDate()).as("check orderDate").isEqualTo(actual.getOrderDate()))
            .satisfies(e ->
                assertThat(e.getExpectedDeliveryDate()).as("check expectedDeliveryDate").isEqualTo(actual.getExpectedDeliveryDate())
            )
            .satisfies(e -> assertThat(e.getActualDeliveryDate()).as("check actualDeliveryDate").isEqualTo(actual.getActualDeliveryDate()))
            .satisfies(e -> assertThat(e.getSpecialRequests()).as("check specialRequests").isEqualTo(actual.getSpecialRequests()))
            .satisfies(e -> assertThat(e.getStatus()).as("check status").isEqualTo(actual.getStatus()))
            .satisfies(e -> assertThat(e.getCustomerRating()).as("check customerRating").isEqualTo(actual.getCustomerRating()))
            .satisfies(e ->
                assertThat(e.getTotalPrice()).as("check totalPrice").usingComparator(bigDecimalCompareTo).isEqualTo(actual.getTotalPrice())
            )
            .satisfies(e -> assertThat(e.getIsDeleted()).as("check isDeleted").isEqualTo(actual.getIsDeleted()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertHkjOrderUpdatableRelationshipsEquals(HkjOrder expected, HkjOrder actual) {
        assertThat(expected)
            .as("Verify HkjOrder relationships")
            .satisfies(e -> assertThat(e.getCustomer()).as("check customer").isEqualTo(actual.getCustomer()))
            .satisfies(e -> assertThat(e.getMaterial()).as("check material").isEqualTo(actual.getMaterial()))
            .satisfies(e -> assertThat(e.getJewelry()).as("check jewelry").isEqualTo(actual.getJewelry()))
            .satisfies(e -> assertThat(e.getProject()).as("check project").isEqualTo(actual.getProject()))
            .satisfies(e -> assertThat(e.getCategory()).as("check category").isEqualTo(actual.getCategory()));
    }
}
