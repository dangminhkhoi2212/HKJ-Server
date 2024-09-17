package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjCategoryAsserts.*;
import static com.server.hkj.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.hkj.IntegrationTest;
import com.server.hkj.domain.HkjCategory;
import com.server.hkj.repository.HkjCategoryRepository;
import com.server.hkj.service.dto.HkjCategoryDTO;
import com.server.hkj.service.mapper.HkjCategoryMapper;
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
 * Integration tests for the {@link HkjCategoryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjCategoryResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/hkj-categories";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjCategoryRepository hkjCategoryRepository;

    @Autowired
    private HkjCategoryMapper hkjCategoryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjCategoryMockMvc;

    private HkjCategory hkjCategory;

    private HkjCategory insertedHkjCategory;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjCategory createEntity(EntityManager em) {
        HkjCategory hkjCategory = new HkjCategory().name(DEFAULT_NAME);
        return hkjCategory;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjCategory createUpdatedEntity(EntityManager em) {
        HkjCategory hkjCategory = new HkjCategory().name(UPDATED_NAME);
        return hkjCategory;
    }

    @BeforeEach
    public void initTest() {
        hkjCategory = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjCategory != null) {
            hkjCategoryRepository.delete(insertedHkjCategory);
            insertedHkjCategory = null;
        }
    }

    @Test
    @Transactional
    void createHkjCategory() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjCategory
        HkjCategoryDTO hkjCategoryDTO = hkjCategoryMapper.toDto(hkjCategory);
        var returnedHkjCategoryDTO = om.readValue(
            restHkjCategoryMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjCategoryDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjCategoryDTO.class
        );

        // Validate the HkjCategory in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjCategory = hkjCategoryMapper.toEntity(returnedHkjCategoryDTO);
        assertHkjCategoryUpdatableFieldsEquals(returnedHkjCategory, getPersistedHkjCategory(returnedHkjCategory));

        insertedHkjCategory = returnedHkjCategory;
    }

    @Test
    @Transactional
    void createHkjCategoryWithExistingId() throws Exception {
        // Create the HkjCategory with an existing ID
        hkjCategory.setId(1L);
        HkjCategoryDTO hkjCategoryDTO = hkjCategoryMapper.toDto(hkjCategory);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjCategoryMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHkjCategories() throws Exception {
        // Initialize the database
        insertedHkjCategory = hkjCategoryRepository.saveAndFlush(hkjCategory);

        // Get all the hkjCategoryList
        restHkjCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));
    }

    @Test
    @Transactional
    void getHkjCategory() throws Exception {
        // Initialize the database
        insertedHkjCategory = hkjCategoryRepository.saveAndFlush(hkjCategory);

        // Get the hkjCategory
        restHkjCategoryMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjCategory.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjCategory.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME));
    }

    @Test
    @Transactional
    void getHkjCategoriesByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjCategory = hkjCategoryRepository.saveAndFlush(hkjCategory);

        Long id = hkjCategory.getId();

        defaultHkjCategoryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjCategoryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjCategoryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjCategoriesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjCategory = hkjCategoryRepository.saveAndFlush(hkjCategory);

        // Get all the hkjCategoryList where name equals to
        defaultHkjCategoryFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjCategoriesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjCategory = hkjCategoryRepository.saveAndFlush(hkjCategory);

        // Get all the hkjCategoryList where name in
        defaultHkjCategoryFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjCategoriesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjCategory = hkjCategoryRepository.saveAndFlush(hkjCategory);

        // Get all the hkjCategoryList where name is not null
        defaultHkjCategoryFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjCategoriesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjCategory = hkjCategoryRepository.saveAndFlush(hkjCategory);

        // Get all the hkjCategoryList where name contains
        defaultHkjCategoryFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjCategoriesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjCategory = hkjCategoryRepository.saveAndFlush(hkjCategory);

        // Get all the hkjCategoryList where name does not contain
        defaultHkjCategoryFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    private void defaultHkjCategoryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjCategoryShouldBeFound(shouldBeFound);
        defaultHkjCategoryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjCategoryShouldBeFound(String filter) throws Exception {
        restHkjCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjCategory.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)));

        // Check, that the count call also returns 1
        restHkjCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjCategoryShouldNotBeFound(String filter) throws Exception {
        restHkjCategoryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjCategoryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjCategory() throws Exception {
        // Get the hkjCategory
        restHkjCategoryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjCategory() throws Exception {
        // Initialize the database
        insertedHkjCategory = hkjCategoryRepository.saveAndFlush(hkjCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjCategory
        HkjCategory updatedHkjCategory = hkjCategoryRepository.findById(hkjCategory.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjCategory are not directly saved in db
        em.detach(updatedHkjCategory);
        updatedHkjCategory.name(UPDATED_NAME);
        HkjCategoryDTO hkjCategoryDTO = hkjCategoryMapper.toDto(updatedHkjCategory);

        restHkjCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjCategoryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjCategoryDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjCategoryToMatchAllProperties(updatedHkjCategory);
    }

    @Test
    @Transactional
    void putNonExistingHkjCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjCategory.setId(longCount.incrementAndGet());

        // Create the HkjCategory
        HkjCategoryDTO hkjCategoryDTO = hkjCategoryMapper.toDto(hkjCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjCategoryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjCategory.setId(longCount.incrementAndGet());

        // Create the HkjCategory
        HkjCategoryDTO hkjCategoryDTO = hkjCategoryMapper.toDto(hkjCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjCategoryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjCategory.setId(longCount.incrementAndGet());

        // Create the HkjCategory
        HkjCategoryDTO hkjCategoryDTO = hkjCategoryMapper.toDto(hkjCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjCategoryMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjCategoryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedHkjCategory = hkjCategoryRepository.saveAndFlush(hkjCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjCategory using partial update
        HkjCategory partialUpdatedHkjCategory = new HkjCategory();
        partialUpdatedHkjCategory.setId(hkjCategory.getId());

        restHkjCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjCategory))
            )
            .andExpect(status().isOk());

        // Validate the HkjCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjCategoryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHkjCategory, hkjCategory),
            getPersistedHkjCategory(hkjCategory)
        );
    }

    @Test
    @Transactional
    void fullUpdateHkjCategoryWithPatch() throws Exception {
        // Initialize the database
        insertedHkjCategory = hkjCategoryRepository.saveAndFlush(hkjCategory);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjCategory using partial update
        HkjCategory partialUpdatedHkjCategory = new HkjCategory();
        partialUpdatedHkjCategory.setId(hkjCategory.getId());

        partialUpdatedHkjCategory.name(UPDATED_NAME);

        restHkjCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjCategory.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjCategory))
            )
            .andExpect(status().isOk());

        // Validate the HkjCategory in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjCategoryUpdatableFieldsEquals(partialUpdatedHkjCategory, getPersistedHkjCategory(partialUpdatedHkjCategory));
    }

    @Test
    @Transactional
    void patchNonExistingHkjCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjCategory.setId(longCount.incrementAndGet());

        // Create the HkjCategory
        HkjCategoryDTO hkjCategoryDTO = hkjCategoryMapper.toDto(hkjCategory);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjCategoryDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjCategory.setId(longCount.incrementAndGet());

        // Create the HkjCategory
        HkjCategoryDTO hkjCategoryDTO = hkjCategoryMapper.toDto(hkjCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjCategoryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjCategory() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjCategory.setId(longCount.incrementAndGet());

        // Create the HkjCategory
        HkjCategoryDTO hkjCategoryDTO = hkjCategoryMapper.toDto(hkjCategory);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjCategoryMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hkjCategoryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjCategory in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjCategory() throws Exception {
        // Initialize the database
        insertedHkjCategory = hkjCategoryRepository.saveAndFlush(hkjCategory);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjCategory
        restHkjCategoryMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjCategory.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjCategoryRepository.count();
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

    protected HkjCategory getPersistedHkjCategory(HkjCategory hkjCategory) {
        return hkjCategoryRepository.findById(hkjCategory.getId()).orElseThrow();
    }

    protected void assertPersistedHkjCategoryToMatchAllProperties(HkjCategory expectedHkjCategory) {
        assertHkjCategoryAllPropertiesEquals(expectedHkjCategory, getPersistedHkjCategory(expectedHkjCategory));
    }

    protected void assertPersistedHkjCategoryToMatchUpdatableProperties(HkjCategory expectedHkjCategory) {
        assertHkjCategoryAllUpdatablePropertiesEquals(expectedHkjCategory, getPersistedHkjCategory(expectedHkjCategory));
    }
}
