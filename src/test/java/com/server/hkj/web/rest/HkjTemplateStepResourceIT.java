package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjTemplateStepAsserts.*;
import static com.server.hkj.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.hkj.IntegrationTest;
import com.server.hkj.domain.HkjTemplate;
import com.server.hkj.domain.HkjTemplateStep;
import com.server.hkj.repository.HkjTemplateStepRepository;
import com.server.hkj.service.dto.HkjTemplateStepDTO;
import com.server.hkj.service.mapper.HkjTemplateStepMapper;
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
 * Integration tests for the {@link HkjTemplateStepResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjTemplateStepResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/hkj-template-steps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjTemplateStepRepository hkjTemplateStepRepository;

    @Autowired
    private HkjTemplateStepMapper hkjTemplateStepMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjTemplateStepMockMvc;

    private HkjTemplateStep hkjTemplateStep;

    private HkjTemplateStep insertedHkjTemplateStep;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjTemplateStep createEntity(EntityManager em) {
        HkjTemplateStep hkjTemplateStep = new HkjTemplateStep().name(DEFAULT_NAME).isDeleted(DEFAULT_IS_DELETED);
        return hkjTemplateStep;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjTemplateStep createUpdatedEntity(EntityManager em) {
        HkjTemplateStep hkjTemplateStep = new HkjTemplateStep().name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);
        return hkjTemplateStep;
    }

    @BeforeEach
    public void initTest() {
        hkjTemplateStep = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjTemplateStep != null) {
            hkjTemplateStepRepository.delete(insertedHkjTemplateStep);
            insertedHkjTemplateStep = null;
        }
    }

    @Test
    @Transactional
    void createHkjTemplateStep() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjTemplateStep
        HkjTemplateStepDTO hkjTemplateStepDTO = hkjTemplateStepMapper.toDto(hkjTemplateStep);
        var returnedHkjTemplateStepDTO = om.readValue(
            restHkjTemplateStepMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(hkjTemplateStepDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjTemplateStepDTO.class
        );

        // Validate the HkjTemplateStep in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjTemplateStep = hkjTemplateStepMapper.toEntity(returnedHkjTemplateStepDTO);
        assertHkjTemplateStepUpdatableFieldsEquals(returnedHkjTemplateStep, getPersistedHkjTemplateStep(returnedHkjTemplateStep));

        insertedHkjTemplateStep = returnedHkjTemplateStep;
    }

    @Test
    @Transactional
    void createHkjTemplateStepWithExistingId() throws Exception {
        // Create the HkjTemplateStep with an existing ID
        hkjTemplateStep.setId(1L);
        HkjTemplateStepDTO hkjTemplateStepDTO = hkjTemplateStepMapper.toDto(hkjTemplateStep);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjTemplateStepMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTemplateStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTemplateStep in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHkjTemplateSteps() throws Exception {
        // Initialize the database
        insertedHkjTemplateStep = hkjTemplateStepRepository.saveAndFlush(hkjTemplateStep);

        // Get all the hkjTemplateStepList
        restHkjTemplateStepMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjTemplateStep.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getHkjTemplateStep() throws Exception {
        // Initialize the database
        insertedHkjTemplateStep = hkjTemplateStepRepository.saveAndFlush(hkjTemplateStep);

        // Get the hkjTemplateStep
        restHkjTemplateStepMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjTemplateStep.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjTemplateStep.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getHkjTemplateStepsByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjTemplateStep = hkjTemplateStepRepository.saveAndFlush(hkjTemplateStep);

        Long id = hkjTemplateStep.getId();

        defaultHkjTemplateStepFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjTemplateStepFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjTemplateStepFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjTemplateStepsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTemplateStep = hkjTemplateStepRepository.saveAndFlush(hkjTemplateStep);

        // Get all the hkjTemplateStepList where name equals to
        defaultHkjTemplateStepFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjTemplateStepsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTemplateStep = hkjTemplateStepRepository.saveAndFlush(hkjTemplateStep);

        // Get all the hkjTemplateStepList where name in
        defaultHkjTemplateStepFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjTemplateStepsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTemplateStep = hkjTemplateStepRepository.saveAndFlush(hkjTemplateStep);

        // Get all the hkjTemplateStepList where name is not null
        defaultHkjTemplateStepFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjTemplateStepsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjTemplateStep = hkjTemplateStepRepository.saveAndFlush(hkjTemplateStep);

        // Get all the hkjTemplateStepList where name contains
        defaultHkjTemplateStepFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjTemplateStepsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjTemplateStep = hkjTemplateStepRepository.saveAndFlush(hkjTemplateStep);

        // Get all the hkjTemplateStepList where name does not contain
        defaultHkjTemplateStepFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllHkjTemplateStepsByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTemplateStep = hkjTemplateStepRepository.saveAndFlush(hkjTemplateStep);

        // Get all the hkjTemplateStepList where isDeleted equals to
        defaultHkjTemplateStepFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjTemplateStepsByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTemplateStep = hkjTemplateStepRepository.saveAndFlush(hkjTemplateStep);

        // Get all the hkjTemplateStepList where isDeleted in
        defaultHkjTemplateStepFiltering(
            "isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED,
            "isDeleted.in=" + UPDATED_IS_DELETED
        );
    }

    @Test
    @Transactional
    void getAllHkjTemplateStepsByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTemplateStep = hkjTemplateStepRepository.saveAndFlush(hkjTemplateStep);

        // Get all the hkjTemplateStepList where isDeleted is not null
        defaultHkjTemplateStepFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjTemplateStepsByTemplateIsEqualToSomething() throws Exception {
        HkjTemplate template;
        if (TestUtil.findAll(em, HkjTemplate.class).isEmpty()) {
            hkjTemplateStepRepository.saveAndFlush(hkjTemplateStep);
            template = HkjTemplateResourceIT.createEntity(em);
        } else {
            template = TestUtil.findAll(em, HkjTemplate.class).get(0);
        }
        em.persist(template);
        em.flush();
        hkjTemplateStep.setTemplate(template);
        hkjTemplateStepRepository.saveAndFlush(hkjTemplateStep);
        Long templateId = template.getId();
        // Get all the hkjTemplateStepList where template equals to templateId
        defaultHkjTemplateStepShouldBeFound("templateId.equals=" + templateId);

        // Get all the hkjTemplateStepList where template equals to (templateId + 1)
        defaultHkjTemplateStepShouldNotBeFound("templateId.equals=" + (templateId + 1));
    }

    private void defaultHkjTemplateStepFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjTemplateStepShouldBeFound(shouldBeFound);
        defaultHkjTemplateStepShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjTemplateStepShouldBeFound(String filter) throws Exception {
        restHkjTemplateStepMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjTemplateStep.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restHkjTemplateStepMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjTemplateStepShouldNotBeFound(String filter) throws Exception {
        restHkjTemplateStepMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjTemplateStepMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjTemplateStep() throws Exception {
        // Get the hkjTemplateStep
        restHkjTemplateStepMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjTemplateStep() throws Exception {
        // Initialize the database
        insertedHkjTemplateStep = hkjTemplateStepRepository.saveAndFlush(hkjTemplateStep);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjTemplateStep
        HkjTemplateStep updatedHkjTemplateStep = hkjTemplateStepRepository.findById(hkjTemplateStep.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjTemplateStep are not directly saved in db
        em.detach(updatedHkjTemplateStep);
        updatedHkjTemplateStep.name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);
        HkjTemplateStepDTO hkjTemplateStepDTO = hkjTemplateStepMapper.toDto(updatedHkjTemplateStep);

        restHkjTemplateStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjTemplateStepDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjTemplateStepDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjTemplateStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjTemplateStepToMatchAllProperties(updatedHkjTemplateStep);
    }

    @Test
    @Transactional
    void putNonExistingHkjTemplateStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTemplateStep.setId(longCount.incrementAndGet());

        // Create the HkjTemplateStep
        HkjTemplateStepDTO hkjTemplateStepDTO = hkjTemplateStepMapper.toDto(hkjTemplateStep);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjTemplateStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjTemplateStepDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjTemplateStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTemplateStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjTemplateStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTemplateStep.setId(longCount.incrementAndGet());

        // Create the HkjTemplateStep
        HkjTemplateStepDTO hkjTemplateStepDTO = hkjTemplateStepMapper.toDto(hkjTemplateStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTemplateStepMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjTemplateStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTemplateStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjTemplateStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTemplateStep.setId(longCount.incrementAndGet());

        // Create the HkjTemplateStep
        HkjTemplateStepDTO hkjTemplateStepDTO = hkjTemplateStepMapper.toDto(hkjTemplateStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTemplateStepMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTemplateStepDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjTemplateStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjTemplateStepWithPatch() throws Exception {
        // Initialize the database
        insertedHkjTemplateStep = hkjTemplateStepRepository.saveAndFlush(hkjTemplateStep);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjTemplateStep using partial update
        HkjTemplateStep partialUpdatedHkjTemplateStep = new HkjTemplateStep();
        partialUpdatedHkjTemplateStep.setId(hkjTemplateStep.getId());

        partialUpdatedHkjTemplateStep.name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);

        restHkjTemplateStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjTemplateStep.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjTemplateStep))
            )
            .andExpect(status().isOk());

        // Validate the HkjTemplateStep in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjTemplateStepUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHkjTemplateStep, hkjTemplateStep),
            getPersistedHkjTemplateStep(hkjTemplateStep)
        );
    }

    @Test
    @Transactional
    void fullUpdateHkjTemplateStepWithPatch() throws Exception {
        // Initialize the database
        insertedHkjTemplateStep = hkjTemplateStepRepository.saveAndFlush(hkjTemplateStep);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjTemplateStep using partial update
        HkjTemplateStep partialUpdatedHkjTemplateStep = new HkjTemplateStep();
        partialUpdatedHkjTemplateStep.setId(hkjTemplateStep.getId());

        partialUpdatedHkjTemplateStep.name(UPDATED_NAME).isDeleted(UPDATED_IS_DELETED);

        restHkjTemplateStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjTemplateStep.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjTemplateStep))
            )
            .andExpect(status().isOk());

        // Validate the HkjTemplateStep in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjTemplateStepUpdatableFieldsEquals(
            partialUpdatedHkjTemplateStep,
            getPersistedHkjTemplateStep(partialUpdatedHkjTemplateStep)
        );
    }

    @Test
    @Transactional
    void patchNonExistingHkjTemplateStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTemplateStep.setId(longCount.incrementAndGet());

        // Create the HkjTemplateStep
        HkjTemplateStepDTO hkjTemplateStepDTO = hkjTemplateStepMapper.toDto(hkjTemplateStep);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjTemplateStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjTemplateStepDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjTemplateStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTemplateStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjTemplateStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTemplateStep.setId(longCount.incrementAndGet());

        // Create the HkjTemplateStep
        HkjTemplateStepDTO hkjTemplateStepDTO = hkjTemplateStepMapper.toDto(hkjTemplateStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTemplateStepMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjTemplateStepDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTemplateStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjTemplateStep() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTemplateStep.setId(longCount.incrementAndGet());

        // Create the HkjTemplateStep
        HkjTemplateStepDTO hkjTemplateStepDTO = hkjTemplateStepMapper.toDto(hkjTemplateStep);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTemplateStepMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjTemplateStepDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjTemplateStep in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjTemplateStep() throws Exception {
        // Initialize the database
        insertedHkjTemplateStep = hkjTemplateStepRepository.saveAndFlush(hkjTemplateStep);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjTemplateStep
        restHkjTemplateStepMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjTemplateStep.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjTemplateStepRepository.count();
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

    protected HkjTemplateStep getPersistedHkjTemplateStep(HkjTemplateStep hkjTemplateStep) {
        return hkjTemplateStepRepository.findById(hkjTemplateStep.getId()).orElseThrow();
    }

    protected void assertPersistedHkjTemplateStepToMatchAllProperties(HkjTemplateStep expectedHkjTemplateStep) {
        assertHkjTemplateStepAllPropertiesEquals(expectedHkjTemplateStep, getPersistedHkjTemplateStep(expectedHkjTemplateStep));
    }

    protected void assertPersistedHkjTemplateStepToMatchUpdatableProperties(HkjTemplateStep expectedHkjTemplateStep) {
        assertHkjTemplateStepAllUpdatablePropertiesEquals(expectedHkjTemplateStep, getPersistedHkjTemplateStep(expectedHkjTemplateStep));
    }
}
