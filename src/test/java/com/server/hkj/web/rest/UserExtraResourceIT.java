package com.server.hkj.web.rest;

import static com.server.hkj.domain.UserExtraAsserts.*;
import static com.server.hkj.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.hkj.IntegrationTest;
import com.server.hkj.domain.User;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.repository.UserExtraRepository;
import com.server.hkj.repository.UserRepository;
import com.server.hkj.service.dto.UserExtraDTO;
import com.server.hkj.service.mapper.UserExtraMapper;
import jakarta.persistence.EntityManager;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link UserExtraResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserExtraResourceIT {

    private static final String DEFAULT_PHONE = "AAAAAAAAAA";
    private static final String UPDATED_PHONE = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/user-extras";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserExtraRepository userExtraRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserExtraMapper userExtraMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserExtraMockMvc;

    private UserExtra userExtra;

    private UserExtra insertedUserExtra;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserExtra createEntity(EntityManager em) {
        UserExtra userExtra = new UserExtra().phone(DEFAULT_PHONE).address(DEFAULT_ADDRESS).isDeleted(DEFAULT_IS_DELETED);
        return userExtra;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserExtra createUpdatedEntity(EntityManager em) {
        UserExtra userExtra = new UserExtra().phone(UPDATED_PHONE).address(UPDATED_ADDRESS).isDeleted(UPDATED_IS_DELETED);
        return userExtra;
    }

    @BeforeEach
    public void initTest() {
        userExtra = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserExtra != null) {
            userExtraRepository.delete(insertedUserExtra);
            insertedUserExtra = null;
        }
        userRepository.deleteAll();
    }

    @Test
    @Transactional
    void createUserExtra() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);
        var returnedUserExtraDTO = om.readValue(
            restUserExtraMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userExtraDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserExtraDTO.class
        );

        // Validate the UserExtra in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedUserExtra = userExtraMapper.toEntity(returnedUserExtraDTO);
        assertUserExtraUpdatableFieldsEquals(returnedUserExtra, getPersistedUserExtra(returnedUserExtra));

        insertedUserExtra = returnedUserExtra;
    }

    @Test
    @Transactional
    void createUserExtraWithExistingId() throws Exception {
        // Create the UserExtra with an existing ID
        userExtra.setId(1L);
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserExtraMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userExtraDTO)))
            .andExpect(status().isBadRequest());

        // Validate the UserExtra in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkPhoneIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        userExtra.setPhone(null);

        // Create the UserExtra, which fails.
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        restUserExtraMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userExtraDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllUserExtras() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList
        restUserExtraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userExtra.getId().intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getUserExtra() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.saveAndFlush(userExtra);

        // Get the userExtra
        restUserExtraMockMvc
            .perform(get(ENTITY_API_URL_ID, userExtra.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userExtra.getId().intValue()))
            .andExpect(jsonPath("$.phone").value(DEFAULT_PHONE))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getUserExtrasByIdFiltering() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.saveAndFlush(userExtra);

        Long id = userExtra.getId();

        defaultUserExtraFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultUserExtraFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultUserExtraFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllUserExtrasByPhoneIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where phone equals to
        defaultUserExtraFiltering("phone.equals=" + DEFAULT_PHONE, "phone.equals=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByPhoneIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where phone in
        defaultUserExtraFiltering("phone.in=" + DEFAULT_PHONE + "," + UPDATED_PHONE, "phone.in=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByPhoneIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where phone is not null
        defaultUserExtraFiltering("phone.specified=true", "phone.specified=false");
    }

    @Test
    @Transactional
    void getAllUserExtrasByPhoneContainsSomething() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where phone contains
        defaultUserExtraFiltering("phone.contains=" + DEFAULT_PHONE, "phone.contains=" + UPDATED_PHONE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByPhoneNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where phone does not contain
        defaultUserExtraFiltering("phone.doesNotContain=" + UPDATED_PHONE, "phone.doesNotContain=" + DEFAULT_PHONE);
    }

    @Test
    @Transactional
    void getAllUserExtrasByAddressIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where address equals to
        defaultUserExtraFiltering("address.equals=" + DEFAULT_ADDRESS, "address.equals=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllUserExtrasByAddressIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where address in
        defaultUserExtraFiltering("address.in=" + DEFAULT_ADDRESS + "," + UPDATED_ADDRESS, "address.in=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllUserExtrasByAddressIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where address is not null
        defaultUserExtraFiltering("address.specified=true", "address.specified=false");
    }

    @Test
    @Transactional
    void getAllUserExtrasByAddressContainsSomething() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where address contains
        defaultUserExtraFiltering("address.contains=" + DEFAULT_ADDRESS, "address.contains=" + UPDATED_ADDRESS);
    }

    @Test
    @Transactional
    void getAllUserExtrasByAddressNotContainsSomething() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where address does not contain
        defaultUserExtraFiltering("address.doesNotContain=" + UPDATED_ADDRESS, "address.doesNotContain=" + DEFAULT_ADDRESS);
    }

    @Test
    @Transactional
    void getAllUserExtrasByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where isDeleted equals to
        defaultUserExtraFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllUserExtrasByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where isDeleted in
        defaultUserExtraFiltering("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED, "isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllUserExtrasByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.saveAndFlush(userExtra);

        // Get all the userExtraList where isDeleted is not null
        defaultUserExtraFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllUserExtrasByUserIsEqualToSomething() throws Exception {
        User user;
        if (TestUtil.findAll(em, User.class).isEmpty()) {
            userExtraRepository.saveAndFlush(userExtra);
            user = UserResourceIT.createEntity(em);
        } else {
            user = TestUtil.findAll(em, User.class).get(0);
        }
        em.persist(user);
        em.flush();
        userExtra.setUser(user);
        userExtraRepository.saveAndFlush(userExtra);
        String userId = user.getId();
        // Get all the userExtraList where user equals to userId
        defaultUserExtraShouldBeFound("userId.equals=" + userId);

        // Get all the userExtraList where user equals to "invalid-id"
        defaultUserExtraShouldNotBeFound("userId.equals=" + "invalid-id");
    }

    private void defaultUserExtraFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultUserExtraShouldBeFound(shouldBeFound);
        defaultUserExtraShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultUserExtraShouldBeFound(String filter) throws Exception {
        restUserExtraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userExtra.getId().intValue())))
            .andExpect(jsonPath("$.[*].phone").value(hasItem(DEFAULT_PHONE)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restUserExtraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultUserExtraShouldNotBeFound(String filter) throws Exception {
        restUserExtraMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restUserExtraMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingUserExtra() throws Exception {
        // Get the userExtra
        restUserExtraMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserExtra() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.saveAndFlush(userExtra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userExtra
        UserExtra updatedUserExtra = userExtraRepository.findById(userExtra.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedUserExtra are not directly saved in db
        em.detach(updatedUserExtra);
        updatedUserExtra.phone(UPDATED_PHONE).address(UPDATED_ADDRESS).isDeleted(UPDATED_IS_DELETED);
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(updatedUserExtra);

        restUserExtraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userExtraDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userExtraDTO))
            )
            .andExpect(status().isOk());

        // Validate the UserExtra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserExtraToMatchAllProperties(updatedUserExtra);
    }

    @Test
    @Transactional
    void putNonExistingUserExtra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userExtra.setId(longCount.incrementAndGet());

        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserExtraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userExtraDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userExtraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserExtra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserExtra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userExtra.setId(longCount.incrementAndGet());

        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserExtraMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userExtraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserExtra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserExtra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userExtra.setId(longCount.incrementAndGet());

        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserExtraMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userExtraDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserExtra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserExtraWithPatch() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.saveAndFlush(userExtra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userExtra using partial update
        UserExtra partialUpdatedUserExtra = new UserExtra();
        partialUpdatedUserExtra.setId(userExtra.getId());

        partialUpdatedUserExtra.address(UPDATED_ADDRESS).isDeleted(UPDATED_IS_DELETED);

        restUserExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserExtra.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserExtra))
            )
            .andExpect(status().isOk());

        // Validate the UserExtra in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserExtraUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserExtra, userExtra),
            getPersistedUserExtra(userExtra)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserExtraWithPatch() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.saveAndFlush(userExtra);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userExtra using partial update
        UserExtra partialUpdatedUserExtra = new UserExtra();
        partialUpdatedUserExtra.setId(userExtra.getId());

        partialUpdatedUserExtra.phone(UPDATED_PHONE).address(UPDATED_ADDRESS).isDeleted(UPDATED_IS_DELETED);

        restUserExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserExtra.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserExtra))
            )
            .andExpect(status().isOk());

        // Validate the UserExtra in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserExtraUpdatableFieldsEquals(partialUpdatedUserExtra, getPersistedUserExtra(partialUpdatedUserExtra));
    }

    @Test
    @Transactional
    void patchNonExistingUserExtra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userExtra.setId(longCount.incrementAndGet());

        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userExtraDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userExtraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserExtra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserExtra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userExtra.setId(longCount.incrementAndGet());

        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserExtraMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userExtraDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserExtra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserExtra() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userExtra.setId(longCount.incrementAndGet());

        // Create the UserExtra
        UserExtraDTO userExtraDTO = userExtraMapper.toDto(userExtra);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserExtraMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userExtraDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserExtra in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserExtra() throws Exception {
        // Initialize the database
        insertedUserExtra = userExtraRepository.saveAndFlush(userExtra);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userExtra
        restUserExtraMockMvc
            .perform(delete(ENTITY_API_URL_ID, userExtra.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userExtraRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected UserExtra getPersistedUserExtra(UserExtra userExtra) {
        return userExtraRepository.findById(userExtra.getId()).orElseThrow();
    }

    protected void assertPersistedUserExtraToMatchAllProperties(UserExtra expectedUserExtra) {
        assertUserExtraAllPropertiesEquals(expectedUserExtra, getPersistedUserExtra(expectedUserExtra));
    }

    protected void assertPersistedUserExtraToMatchUpdatableProperties(UserExtra expectedUserExtra) {
        assertUserExtraAllUpdatablePropertiesEquals(expectedUserExtra, getPersistedUserExtra(expectedUserExtra));
    }
}
