package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjTemplateAsserts.*;
import static com.server.hkj.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.hkj.IntegrationTest;
import com.server.hkj.domain.HkjCategory;
import com.server.hkj.domain.HkjTemplate;
import com.server.hkj.repository.HkjTemplateRepository;
import com.server.hkj.service.dto.HkjTemplateDTO;
import com.server.hkj.service.mapper.HkjTemplateMapper;
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
 * Integration tests for the {@link HkjTemplateResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjTemplateResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/hkj-templates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjTemplateRepository hkjTemplateRepository;

    @Autowired
    private HkjTemplateMapper hkjTemplateMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjTemplateMockMvc;

    private HkjTemplate hkjTemplate;

    private HkjTemplate insertedHkjTemplate;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjTemplate createEntity() {
        return new HkjTemplate().name(DEFAULT_NAME).isDeleted(DEFAULT_IS_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjTemplate createUpdatedEntity() {
        return new HkjTemplate().name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    public void initTest() {
        hkjTemplate = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjTemplate != null) {
            hkjTemplateRepository.delete(insertedHkjTemplate);
            insertedHkjTemplate = null;
        }
    }

    @Test
    @Transactional
    void createHkjTemplate() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjTemplate
        HkjTemplateDTO hkjTemplateDTO = hkjTemplateMapper.toDto(hkjTemplate);
        var returnedHkjTemplateDTO = om.readValue(
            restHkjTemplateMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTemplateDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjTemplateDTO.class
        );

        // Validate the HkjTemplate in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjTemplate = hkjTemplateMapper.toEntity(returnedHkjTemplateDTO);
        assertHkjTemplateUpdatableFieldsEquals(returnedHkjTemplate, getPersistedHkjTemplate(returnedHkjTemplate));

        insertedHkjTemplate = returnedHkjTemplate;
    }

    @Test
    @Transactional
    void createHkjTemplateWithExistingId() throws Exception {
        // Create the HkjTemplate with an existing ID
        hkjTemplate.setId(1L);
        HkjTemplateDTO hkjTemplateDTO = hkjTemplateMapper.toDto(hkjTemplate);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjTemplateMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHkjTemplates() throws Exception {
        // Initialize the database
        insertedHkjTemplate = hkjTemplateRepository.saveAndFlush(hkjTemplate);

        // Get all the hkjTemplateList
        restHkjTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getHkjTemplate() throws Exception {
        // Initialize the database
        insertedHkjTemplate = hkjTemplateRepository.saveAndFlush(hkjTemplate);

        // Get the hkjTemplate
        restHkjTemplateMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjTemplate.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjTemplate.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getHkjTemplatesByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjTemplate = hkjTemplateRepository.saveAndFlush(hkjTemplate);

        Long id = hkjTemplate.getId();

        defaultHkjTemplateFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjTemplateFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjTemplateFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjTemplatesByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTemplate = hkjTemplateRepository.saveAndFlush(hkjTemplate);

        // Get all the hkjTemplateList where name equals to
        defaultHkjTemplateFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjTemplatesByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTemplate = hkjTemplateRepository.saveAndFlush(hkjTemplate);

        // Get all the hkjTemplateList where name in
        defaultHkjTemplateFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjTemplatesByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTemplate = hkjTemplateRepository.saveAndFlush(hkjTemplate);

        // Get all the hkjTemplateList where name is not null
        defaultHkjTemplateFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjTemplatesByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjTemplate = hkjTemplateRepository.saveAndFlush(hkjTemplate);

        // Get all the hkjTemplateList where name contains
        defaultHkjTemplateFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjTemplatesByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjTemplate = hkjTemplateRepository.saveAndFlush(hkjTemplate);

        // Get all the hkjTemplateList where name does not contain
        defaultHkjTemplateFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllHkjTemplatesByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTemplate = hkjTemplateRepository.saveAndFlush(hkjTemplate);

        // Get all the hkjTemplateList where isDeleted equals to
        defaultHkjTemplateFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjTemplatesByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTemplate = hkjTemplateRepository.saveAndFlush(hkjTemplate);

        // Get all the hkjTemplateList where isDeleted in
        defaultHkjTemplateFiltering("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED, "isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjTemplatesByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTemplate = hkjTemplateRepository.saveAndFlush(hkjTemplate);

        // Get all the hkjTemplateList where isDeleted is not null
        defaultHkjTemplateFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjTemplatesByCategoryIsEqualToSomething() throws Exception {
        HkjCategory category;
        if (TestUtil.findAll(em, HkjCategory.class).isEmpty()) {
            hkjTemplateRepository.saveAndFlush(hkjTemplate);
            category = HkjCategoryResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, HkjCategory.class).get(0);
        }
        em.persist(category);
        em.flush();
        hkjTemplate.setCategory(category);
        hkjTemplateRepository.saveAndFlush(hkjTemplate);
        Long categoryId = category.getId();
        // Get all the hkjTemplateList where category equals to categoryId
        defaultHkjTemplateShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the hkjTemplateList where category equals to (categoryId + 1)
        defaultHkjTemplateShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    private void defaultHkjTemplateFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjTemplateShouldBeFound(shouldBeFound);
        defaultHkjTemplateShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjTemplateShouldBeFound(String filter) throws Exception {
        restHkjTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjTemplate.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restHkjTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjTemplateShouldNotBeFound(String filter) throws Exception {
        restHkjTemplateMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjTemplateMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjTemplate() throws Exception {
        // Get the hkjTemplate
        restHkjTemplateMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjTemplate() throws Exception {
        // Initialize the database
        insertedHkjTemplate = hkjTemplateRepository.saveAndFlush(hkjTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjTemplate
        HkjTemplate updatedHkjTemplate = hkjTemplateRepository.findById(hkjTemplate.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjTemplate are not directly saved in db
        em.detach(updatedHkjTemplate);
        updatedHkjTemplate.name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);
        HkjTemplateDTO hkjTemplateDTO = hkjTemplateMapper.toDto(updatedHkjTemplate);

        restHkjTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjTemplateDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjTemplateDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjTemplateToMatchAllProperties(updatedHkjTemplate);
    }

    @Test
    @Transactional
    void putNonExistingHkjTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTemplate.setId(longCount.incrementAndGet());

        // Create the HkjTemplate
        HkjTemplateDTO hkjTemplateDTO = hkjTemplateMapper.toDto(hkjTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjTemplateDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTemplate.setId(longCount.incrementAndGet());

        // Create the HkjTemplate
        HkjTemplateDTO hkjTemplateDTO = hkjTemplateMapper.toDto(hkjTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTemplateMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTemplate.setId(longCount.incrementAndGet());

        // Create the HkjTemplate
        HkjTemplateDTO hkjTemplateDTO = hkjTemplateMapper.toDto(hkjTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTemplateMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTemplateDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedHkjTemplate = hkjTemplateRepository.saveAndFlush(hkjTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjTemplate using partial update
        HkjTemplate partialUpdatedHkjTemplate = new HkjTemplate();
        partialUpdatedHkjTemplate.setId(hkjTemplate.getId());

        partialUpdatedHkjTemplate.name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);

        restHkjTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjTemplate.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjTemplate))
            )
            .andExpect(status().isOk());

        // Validate the HkjTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjTemplateUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHkjTemplate, hkjTemplate),
            getPersistedHkjTemplate(hkjTemplate)
        );
    }

    @Test
    @Transactional
    void fullUpdateHkjTemplateWithPatch() throws Exception {
        // Initialize the database
        insertedHkjTemplate = hkjTemplateRepository.saveAndFlush(hkjTemplate);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjTemplate using partial update
        HkjTemplate partialUpdatedHkjTemplate = new HkjTemplate();
        partialUpdatedHkjTemplate.setId(hkjTemplate.getId());

        partialUpdatedHkjTemplate.name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);

        restHkjTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjTemplate.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjTemplate))
            )
            .andExpect(status().isOk());

        // Validate the HkjTemplate in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjTemplateUpdatableFieldsEquals(partialUpdatedHkjTemplate, getPersistedHkjTemplate(partialUpdatedHkjTemplate));
    }

    @Test
    @Transactional
    void patchNonExistingHkjTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTemplate.setId(longCount.incrementAndGet());

        // Create the HkjTemplate
        HkjTemplateDTO hkjTemplateDTO = hkjTemplateMapper.toDto(hkjTemplate);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjTemplateDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTemplate.setId(longCount.incrementAndGet());

        // Create the HkjTemplate
        HkjTemplateDTO hkjTemplateDTO = hkjTemplateMapper.toDto(hkjTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjTemplateDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjTemplate() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTemplate.setId(longCount.incrementAndGet());

        // Create the HkjTemplate
        HkjTemplateDTO hkjTemplateDTO = hkjTemplateMapper.toDto(hkjTemplate);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTemplateMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hkjTemplateDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjTemplate in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjTemplate() throws Exception {
        // Initialize the database
        insertedHkjTemplate = hkjTemplateRepository.saveAndFlush(hkjTemplate);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjTemplate
        restHkjTemplateMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjTemplate.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjTemplateRepository.count();
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

    protected HkjTemplate getPersistedHkjTemplate(HkjTemplate hkjTemplate) {
        return hkjTemplateRepository.findById(hkjTemplate.getId()).orElseThrow();
    }

    protected void assertPersistedHkjTemplateToMatchAllProperties(HkjTemplate expectedHkjTemplate) {
        assertHkjTemplateAllPropertiesEquals(expectedHkjTemplate, getPersistedHkjTemplate(expectedHkjTemplate));
    }

    protected void assertPersistedHkjTemplateToMatchUpdatableProperties(HkjTemplate expectedHkjTemplate) {
        assertHkjTemplateAllUpdatablePropertiesEquals(expectedHkjTemplate, getPersistedHkjTemplate(expectedHkjTemplate));
    }
}
