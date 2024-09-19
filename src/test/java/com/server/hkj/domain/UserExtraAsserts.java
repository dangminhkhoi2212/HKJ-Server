package com.server.hkj.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class UserExtraAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserExtraAllPropertiesEquals(UserExtra expected, UserExtra actual) {
        assertUserExtraAutoGeneratedPropertiesEquals(expected, actual);
        assertUserExtraAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserExtraAllUpdatablePropertiesEquals(UserExtra expected, UserExtra actual) {
        assertUserExtraUpdatableFieldsEquals(expected, actual);
        assertUserExtraUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserExtraAutoGeneratedPropertiesEquals(UserExtra expected, UserExtra actual) {
        assertThat(expected)
            .as("Verify UserExtra auto generated properties")
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
    public static void assertUserExtraUpdatableFieldsEquals(UserExtra expected, UserExtra actual) {
        assertThat(expected)
            .as("Verify UserExtra relevant properties")
            .satisfies(e -> assertThat(e.getPhone()).as("check phone").isEqualTo(actual.getPhone()))
            .satisfies(e -> assertThat(e.getAddress()).as("check address").isEqualTo(actual.getAddress()))
            .satisfies(e -> assertThat(e.getIsDeleted()).as("check isDeleted").isEqualTo(actual.getIsDeleted()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertUserExtraUpdatableRelationshipsEquals(UserExtra expected, UserExtra actual) {}
}
