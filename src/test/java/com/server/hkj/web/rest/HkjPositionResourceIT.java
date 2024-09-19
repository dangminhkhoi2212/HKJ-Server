package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjPositionAsserts.*;
import static com.server.hkj.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.hkj.IntegrationTest;
import com.server.hkj.domain.HkjPosition;
import com.server.hkj.repository.HkjPositionRepository;
import com.server.hkj.service.dto.HkjPositionDTO;
import com.server.hkj.service.mapper.HkjPositionMapper;
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
 * Integration tests for the {@link HkjPositionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjPositionResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/hkj-positions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjPositionRepository hkjPositionRepository;

    @Autowired
    private HkjPositionMapper hkjPositionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjPositionMockMvc;

    private HkjPosition hkjPosition;

    private HkjPosition insertedHkjPosition;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjPosition createEntity(EntityManager em) {
        HkjPosition hkjPosition = new HkjPosition().name(DEFAULT_NAME).isDeleted(DEFAULT_IS_DELETED);
        return hkjPosition;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjPosition createUpdatedEntity(EntityManager em) {
        HkjPosition hkjPosition = new HkjPosition().name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);
        return hkjPosition;
    }

    @BeforeEach
    public void initTest() {
        hkjPosition = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjPosition != null) {
            hkjPositionRepository.delete(insertedHkjPosition);
            insertedHkjPosition = null;
        }
    }

    @Test
    @Transactional
    void createHkjPosition() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjPosition
        HkjPositionDTO hkjPositionDTO = hkjPositionMapper.toDto(hkjPosition);
        var returnedHkjPositionDTO = om.readValue(
            restHkjPositionMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjPositionDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjPositionDTO.class
        );

        // Validate the HkjPosition in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjPosition = hkjPositionMapper.toEntity(returnedHkjPositionDTO);
        assertHkjPositionUpdatableFieldsEquals(returnedHkjPosition, getPersistedHkjPosition(returnedHkjPosition));

        insertedHkjPosition = returnedHkjPosition;
    }

    @Test
    @Transactional
    void createHkjPositionWithExistingId() throws Exception {
        // Create the HkjPosition with an existing ID
        hkjPosition.setId(1L);
        HkjPositionDTO hkjPositionDTO = hkjPositionMapper.toDto(hkjPosition);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjPositionMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjPositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjPosition.setName(null);

        // Create the HkjPosition, which fails.
        HkjPositionDTO hkjPositionDTO = hkjPositionMapper.toDto(hkjPosition);

        restHkjPositionMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjPositionDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHkjPositions() throws Exception {
        // Initialize the database
        insertedHkjPosition = hkjPositionRepository.saveAndFlush(hkjPosition);

        // Get all the hkjPositionList
        restHkjPositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjPosition.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getHkjPosition() throws Exception {
        // Initialize the database
        insertedHkjPosition = hkjPositionRepository.saveAndFlush(hkjPosition);

        // Get the hkjPosition
        restHkjPositionMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjPosition.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjPosition.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getHkjPositionsByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjPosition = hkjPositionRepository.saveAndFlush(hkjPosition);

        Long id = hkjPosition.getId();

        defaultHkjPositionFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjPositionFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjPositionFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjPositionsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjPosition = hkjPositionRepository.saveAndFlush(hkjPosition);

        // Get all the hkjPositionList where name equals to
        defaultHkjPositionFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjPositionsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjPosition = hkjPositionRepository.saveAndFlush(hkjPosition);

        // Get all the hkjPositionList where name in
        defaultHkjPositionFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjPositionsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjPosition = hkjPositionRepository.saveAndFlush(hkjPosition);

        // Get all the hkjPositionList where name is not null
        defaultHkjPositionFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjPositionsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjPosition = hkjPositionRepository.saveAndFlush(hkjPosition);

        // Get all the hkjPositionList where name contains
        defaultHkjPositionFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjPositionsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjPosition = hkjPositionRepository.saveAndFlush(hkjPosition);

        // Get all the hkjPositionList where name does not contain
        defaultHkjPositionFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllHkjPositionsByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjPosition = hkjPositionRepository.saveAndFlush(hkjPosition);

        // Get all the hkjPositionList where isDeleted equals to
        defaultHkjPositionFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjPositionsByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjPosition = hkjPositionRepository.saveAndFlush(hkjPosition);

        // Get all the hkjPositionList where isDeleted in
        defaultHkjPositionFiltering("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED, "isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjPositionsByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjPosition = hkjPositionRepository.saveAndFlush(hkjPosition);

        // Get all the hkjPositionList where isDeleted is not null
        defaultHkjPositionFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    private void defaultHkjPositionFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjPositionShouldBeFound(shouldBeFound);
        defaultHkjPositionShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjPositionShouldBeFound(String filter) throws Exception {
        restHkjPositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjPosition.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restHkjPositionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjPositionShouldNotBeFound(String filter) throws Exception {
        restHkjPositionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjPositionMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjPosition() throws Exception {
        // Get the hkjPosition
        restHkjPositionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjPosition() throws Exception {
        // Initialize the database
        insertedHkjPosition = hkjPositionRepository.saveAndFlush(hkjPosition);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjPosition
        HkjPosition updatedHkjPosition = hkjPositionRepository.findById(hkjPosition.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjPosition are not directly saved in db
        em.detach(updatedHkjPosition);
        updatedHkjPosition.name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);
        HkjPositionDTO hkjPositionDTO = hkjPositionMapper.toDto(updatedHkjPosition);

        restHkjPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjPositionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjPositionDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjPositionToMatchAllProperties(updatedHkjPosition);
    }

    @Test
    @Transactional
    void putNonExistingHkjPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjPosition.setId(longCount.incrementAndGet());

        // Create the HkjPosition
        HkjPositionDTO hkjPositionDTO = hkjPositionMapper.toDto(hkjPosition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjPositionDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjPositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjPosition.setId(longCount.incrementAndGet());

        // Create the HkjPosition
        HkjPositionDTO hkjPositionDTO = hkjPositionMapper.toDto(hkjPosition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjPositionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjPositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjPosition.setId(longCount.incrementAndGet());

        // Create the HkjPosition
        HkjPositionDTO hkjPositionDTO = hkjPositionMapper.toDto(hkjPosition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjPositionMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjPositionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjPositionWithPatch() throws Exception {
        // Initialize the database
        insertedHkjPosition = hkjPositionRepository.saveAndFlush(hkjPosition);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjPosition using partial update
        HkjPosition partialUpdatedHkjPosition = new HkjPosition();
        partialUpdatedHkjPosition.setId(hkjPosition.getId());

        partialUpdatedHkjPosition.isDeleted(UPDATED_IS_DELETED);

        restHkjPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjPosition.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjPosition))
            )
            .andExpect(status().isOk());

        // Validate the HkjPosition in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjPositionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHkjPosition, hkjPosition),
            getPersistedHkjPosition(hkjPosition)
        );
    }

    @Test
    @Transactional
    void fullUpdateHkjPositionWithPatch() throws Exception {
        // Initialize the database
        insertedHkjPosition = hkjPositionRepository.saveAndFlush(hkjPosition);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjPosition using partial update
        HkjPosition partialUpdatedHkjPosition = new HkjPosition();
        partialUpdatedHkjPosition.setId(hkjPosition.getId());

        partialUpdatedHkjPosition.name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);

        restHkjPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjPosition.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjPosition))
            )
            .andExpect(status().isOk());

        // Validate the HkjPosition in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjPositionUpdatableFieldsEquals(partialUpdatedHkjPosition, getPersistedHkjPosition(partialUpdatedHkjPosition));
    }

    @Test
    @Transactional
    void patchNonExistingHkjPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjPosition.setId(longCount.incrementAndGet());

        // Create the HkjPosition
        HkjPositionDTO hkjPositionDTO = hkjPositionMapper.toDto(hkjPosition);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjPositionDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjPositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjPosition.setId(longCount.incrementAndGet());

        // Create the HkjPosition
        HkjPositionDTO hkjPositionDTO = hkjPositionMapper.toDto(hkjPosition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjPositionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjPositionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjPosition() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjPosition.setId(longCount.incrementAndGet());

        // Create the HkjPosition
        HkjPositionDTO hkjPositionDTO = hkjPositionMapper.toDto(hkjPosition);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjPositionMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hkjPositionDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjPosition in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjPosition() throws Exception {
        // Initialize the database
        insertedHkjPosition = hkjPositionRepository.saveAndFlush(hkjPosition);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjPosition
        restHkjPositionMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjPosition.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjPositionRepository.count();
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

    protected HkjPosition getPersistedHkjPosition(HkjPosition hkjPosition) {
        return hkjPositionRepository.findById(hkjPosition.getId()).orElseThrow();
    }

    protected void assertPersistedHkjPositionToMatchAllProperties(HkjPosition expectedHkjPosition) {
        assertHkjPositionAllPropertiesEquals(expectedHkjPosition, getPersistedHkjPosition(expectedHkjPosition));
    }

    protected void assertPersistedHkjPositionToMatchUpdatableProperties(HkjPosition expectedHkjPosition) {
        assertHkjPositionAllUpdatablePropertiesEquals(expectedHkjPosition, getPersistedHkjPosition(expectedHkjPosition));
    }
}
