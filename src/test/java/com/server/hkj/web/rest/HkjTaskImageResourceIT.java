package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjTaskImageAsserts.*;
import static com.server.hkj.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.hkj.IntegrationTest;
import com.server.hkj.domain.HkjTask;
import com.server.hkj.domain.HkjTaskImage;
import com.server.hkj.repository.HkjTaskImageRepository;
import com.server.hkj.service.dto.HkjTaskImageDTO;
import com.server.hkj.service.mapper.HkjTaskImageMapper;
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
 * Integration tests for the {@link HkjTaskImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjTaskImageResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/hkj-task-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjTaskImageRepository hkjTaskImageRepository;

    @Autowired
    private HkjTaskImageMapper hkjTaskImageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjTaskImageMockMvc;

    private HkjTaskImage hkjTaskImage;

    private HkjTaskImage insertedHkjTaskImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjTaskImage createEntity() {
        return new HkjTaskImage().url(DEFAULT_URL).description(DEFAULT_DESCRIPTION).isDeleted(DEFAULT_IS_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjTaskImage createUpdatedEntity() {
        return new HkjTaskImage().url(UPDATED_URL).description(UPDATED_DESCRIPTION).isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    public void initTest() {
        hkjTaskImage = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjTaskImage != null) {
            hkjTaskImageRepository.delete(insertedHkjTaskImage);
            insertedHkjTaskImage = null;
        }
    }

    @Test
    @Transactional
    void createHkjTaskImage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjTaskImage
        HkjTaskImageDTO hkjTaskImageDTO = hkjTaskImageMapper.toDto(hkjTaskImage);
        var returnedHkjTaskImageDTO = om.readValue(
            restHkjTaskImageMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTaskImageDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjTaskImageDTO.class
        );

        // Validate the HkjTaskImage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjTaskImage = hkjTaskImageMapper.toEntity(returnedHkjTaskImageDTO);
        assertHkjTaskImageUpdatableFieldsEquals(returnedHkjTaskImage, getPersistedHkjTaskImage(returnedHkjTaskImage));

        insertedHkjTaskImage = returnedHkjTaskImage;
    }

    @Test
    @Transactional
    void createHkjTaskImageWithExistingId() throws Exception {
        // Create the HkjTaskImage with an existing ID
        hkjTaskImage.setId(1L);
        HkjTaskImageDTO hkjTaskImageDTO = hkjTaskImageMapper.toDto(hkjTaskImage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjTaskImageMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTaskImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTaskImage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjTaskImage.setUrl(null);

        // Create the HkjTaskImage, which fails.
        HkjTaskImageDTO hkjTaskImageDTO = hkjTaskImageMapper.toDto(hkjTaskImage);

        restHkjTaskImageMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTaskImageDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHkjTaskImages() throws Exception {
        // Initialize the database
        insertedHkjTaskImage = hkjTaskImageRepository.saveAndFlush(hkjTaskImage);

        // Get all the hkjTaskImageList
        restHkjTaskImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjTaskImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getHkjTaskImage() throws Exception {
        // Initialize the database
        insertedHkjTaskImage = hkjTaskImageRepository.saveAndFlush(hkjTaskImage);

        // Get the hkjTaskImage
        restHkjTaskImageMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjTaskImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjTaskImage.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getHkjTaskImagesByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjTaskImage = hkjTaskImageRepository.saveAndFlush(hkjTaskImage);

        Long id = hkjTaskImage.getId();

        defaultHkjTaskImageFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjTaskImageFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjTaskImageFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjTaskImagesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTaskImage = hkjTaskImageRepository.saveAndFlush(hkjTaskImage);

        // Get all the hkjTaskImageList where url equals to
        defaultHkjTaskImageFiltering("url.equals=" + DEFAULT_URL, "url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllHkjTaskImagesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTaskImage = hkjTaskImageRepository.saveAndFlush(hkjTaskImage);

        // Get all the hkjTaskImageList where url in
        defaultHkjTaskImageFiltering("url.in=" + DEFAULT_URL + "," + UPDATED_URL, "url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllHkjTaskImagesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTaskImage = hkjTaskImageRepository.saveAndFlush(hkjTaskImage);

        // Get all the hkjTaskImageList where url is not null
        defaultHkjTaskImageFiltering("url.specified=true", "url.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjTaskImagesByUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjTaskImage = hkjTaskImageRepository.saveAndFlush(hkjTaskImage);

        // Get all the hkjTaskImageList where url contains
        defaultHkjTaskImageFiltering("url.contains=" + DEFAULT_URL, "url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllHkjTaskImagesByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjTaskImage = hkjTaskImageRepository.saveAndFlush(hkjTaskImage);

        // Get all the hkjTaskImageList where url does not contain
        defaultHkjTaskImageFiltering("url.doesNotContain=" + UPDATED_URL, "url.doesNotContain=" + DEFAULT_URL);
    }

    @Test
    @Transactional
    void getAllHkjTaskImagesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTaskImage = hkjTaskImageRepository.saveAndFlush(hkjTaskImage);

        // Get all the hkjTaskImageList where description equals to
        defaultHkjTaskImageFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHkjTaskImagesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTaskImage = hkjTaskImageRepository.saveAndFlush(hkjTaskImage);

        // Get all the hkjTaskImageList where description in
        defaultHkjTaskImageFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllHkjTaskImagesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTaskImage = hkjTaskImageRepository.saveAndFlush(hkjTaskImage);

        // Get all the hkjTaskImageList where description is not null
        defaultHkjTaskImageFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjTaskImagesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjTaskImage = hkjTaskImageRepository.saveAndFlush(hkjTaskImage);

        // Get all the hkjTaskImageList where description contains
        defaultHkjTaskImageFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHkjTaskImagesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjTaskImage = hkjTaskImageRepository.saveAndFlush(hkjTaskImage);

        // Get all the hkjTaskImageList where description does not contain
        defaultHkjTaskImageFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllHkjTaskImagesByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTaskImage = hkjTaskImageRepository.saveAndFlush(hkjTaskImage);

        // Get all the hkjTaskImageList where isDeleted equals to
        defaultHkjTaskImageFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjTaskImagesByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTaskImage = hkjTaskImageRepository.saveAndFlush(hkjTaskImage);

        // Get all the hkjTaskImageList where isDeleted in
        defaultHkjTaskImageFiltering("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED, "isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjTaskImagesByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTaskImage = hkjTaskImageRepository.saveAndFlush(hkjTaskImage);

        // Get all the hkjTaskImageList where isDeleted is not null
        defaultHkjTaskImageFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjTaskImagesByTaskIsEqualToSomething() throws Exception {
        HkjTask task;
        if (TestUtil.findAll(em, HkjTask.class).isEmpty()) {
            hkjTaskImageRepository.saveAndFlush(hkjTaskImage);
            task = HkjTaskResourceIT.createEntity();
        } else {
            task = TestUtil.findAll(em, HkjTask.class).get(0);
        }
        em.persist(task);
        em.flush();
        hkjTaskImage.setTask(task);
        hkjTaskImageRepository.saveAndFlush(hkjTaskImage);
        Long taskId = task.getId();
        // Get all the hkjTaskImageList where task equals to taskId
        defaultHkjTaskImageShouldBeFound("taskId.equals=" + taskId);

        // Get all the hkjTaskImageList where task equals to (taskId + 1)
        defaultHkjTaskImageShouldNotBeFound("taskId.equals=" + (taskId + 1));
    }

    private void defaultHkjTaskImageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjTaskImageShouldBeFound(shouldBeFound);
        defaultHkjTaskImageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjTaskImageShouldBeFound(String filter) throws Exception {
        restHkjTaskImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjTaskImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restHkjTaskImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjTaskImageShouldNotBeFound(String filter) throws Exception {
        restHkjTaskImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjTaskImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjTaskImage() throws Exception {
        // Get the hkjTaskImage
        restHkjTaskImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjTaskImage() throws Exception {
        // Initialize the database
        insertedHkjTaskImage = hkjTaskImageRepository.saveAndFlush(hkjTaskImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjTaskImage
        HkjTaskImage updatedHkjTaskImage = hkjTaskImageRepository.findById(hkjTaskImage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjTaskImage are not directly saved in db
        em.detach(updatedHkjTaskImage);
        updatedHkjTaskImage.url(UPDATED_URL).description(UPDATED_DESCRIPTION).isDeleted(UPDATED_IS_DELETED);
        HkjTaskImageDTO hkjTaskImageDTO = hkjTaskImageMapper.toDto(updatedHkjTaskImage);

        restHkjTaskImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjTaskImageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjTaskImageDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjTaskImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjTaskImageToMatchAllProperties(updatedHkjTaskImage);
    }

    @Test
    @Transactional
    void putNonExistingHkjTaskImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTaskImage.setId(longCount.incrementAndGet());

        // Create the HkjTaskImage
        HkjTaskImageDTO hkjTaskImageDTO = hkjTaskImageMapper.toDto(hkjTaskImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjTaskImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjTaskImageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjTaskImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTaskImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjTaskImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTaskImage.setId(longCount.incrementAndGet());

        // Create the HkjTaskImage
        HkjTaskImageDTO hkjTaskImageDTO = hkjTaskImageMapper.toDto(hkjTaskImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTaskImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjTaskImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTaskImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjTaskImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTaskImage.setId(longCount.incrementAndGet());

        // Create the HkjTaskImage
        HkjTaskImageDTO hkjTaskImageDTO = hkjTaskImageMapper.toDto(hkjTaskImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTaskImageMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTaskImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjTaskImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjTaskImageWithPatch() throws Exception {
        // Initialize the database
        insertedHkjTaskImage = hkjTaskImageRepository.saveAndFlush(hkjTaskImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjTaskImage using partial update
        HkjTaskImage partialUpdatedHkjTaskImage = new HkjTaskImage();
        partialUpdatedHkjTaskImage.setId(hkjTaskImage.getId());

        partialUpdatedHkjTaskImage.url(UPDATED_URL).description(UPDATED_DESCRIPTION).isDeleted(UPDATED_IS_DELETED);

        restHkjTaskImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjTaskImage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjTaskImage))
            )
            .andExpect(status().isOk());

        // Validate the HkjTaskImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjTaskImageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHkjTaskImage, hkjTaskImage),
            getPersistedHkjTaskImage(hkjTaskImage)
        );
    }

    @Test
    @Transactional
    void fullUpdateHkjTaskImageWithPatch() throws Exception {
        // Initialize the database
        insertedHkjTaskImage = hkjTaskImageRepository.saveAndFlush(hkjTaskImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjTaskImage using partial update
        HkjTaskImage partialUpdatedHkjTaskImage = new HkjTaskImage();
        partialUpdatedHkjTaskImage.setId(hkjTaskImage.getId());

        partialUpdatedHkjTaskImage.url(UPDATED_URL).description(UPDATED_DESCRIPTION).isDeleted(UPDATED_IS_DELETED);

        restHkjTaskImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjTaskImage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjTaskImage))
            )
            .andExpect(status().isOk());

        // Validate the HkjTaskImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjTaskImageUpdatableFieldsEquals(partialUpdatedHkjTaskImage, getPersistedHkjTaskImage(partialUpdatedHkjTaskImage));
    }

    @Test
    @Transactional
    void patchNonExistingHkjTaskImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTaskImage.setId(longCount.incrementAndGet());

        // Create the HkjTaskImage
        HkjTaskImageDTO hkjTaskImageDTO = hkjTaskImageMapper.toDto(hkjTaskImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjTaskImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjTaskImageDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjTaskImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTaskImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjTaskImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTaskImage.setId(longCount.incrementAndGet());

        // Create the HkjTaskImage
        HkjTaskImageDTO hkjTaskImageDTO = hkjTaskImageMapper.toDto(hkjTaskImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTaskImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjTaskImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTaskImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjTaskImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTaskImage.setId(longCount.incrementAndGet());

        // Create the HkjTaskImage
        HkjTaskImageDTO hkjTaskImageDTO = hkjTaskImageMapper.toDto(hkjTaskImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTaskImageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjTaskImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjTaskImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjTaskImage() throws Exception {
        // Initialize the database
        insertedHkjTaskImage = hkjTaskImageRepository.saveAndFlush(hkjTaskImage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjTaskImage
        restHkjTaskImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjTaskImage.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjTaskImageRepository.count();
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

    protected HkjTaskImage getPersistedHkjTaskImage(HkjTaskImage hkjTaskImage) {
        return hkjTaskImageRepository.findById(hkjTaskImage.getId()).orElseThrow();
    }

    protected void assertPersistedHkjTaskImageToMatchAllProperties(HkjTaskImage expectedHkjTaskImage) {
        assertHkjTaskImageAllPropertiesEquals(expectedHkjTaskImage, getPersistedHkjTaskImage(expectedHkjTaskImage));
    }

    protected void assertPersistedHkjTaskImageToMatchUpdatableProperties(HkjTaskImage expectedHkjTaskImage) {
        assertHkjTaskImageAllUpdatablePropertiesEquals(expectedHkjTaskImage, getPersistedHkjTaskImage(expectedHkjTaskImage));
    }
}
