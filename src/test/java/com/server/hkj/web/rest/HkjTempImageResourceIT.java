package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjTempImageAsserts.*;
import static com.server.hkj.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.hkj.IntegrationTest;
import com.server.hkj.domain.HkjTempImage;
import com.server.hkj.repository.HkjTempImageRepository;
import com.server.hkj.service.dto.HkjTempImageDTO;
import com.server.hkj.service.mapper.HkjTempImageMapper;
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
 * Integration tests for the {@link HkjTempImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjTempImageResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_USED = false;
    private static final Boolean UPDATED_IS_USED = true;

    private static final String ENTITY_API_URL = "/api/hkj-temp-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjTempImageRepository hkjTempImageRepository;

    @Autowired
    private HkjTempImageMapper hkjTempImageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjTempImageMockMvc;

    private HkjTempImage hkjTempImage;

    private HkjTempImage insertedHkjTempImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjTempImage createEntity(EntityManager em) {
        HkjTempImage hkjTempImage = new HkjTempImage().url(DEFAULT_URL).isUsed(DEFAULT_IS_USED);
        return hkjTempImage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjTempImage createUpdatedEntity(EntityManager em) {
        HkjTempImage hkjTempImage = new HkjTempImage().url(UPDATED_URL).isUsed(UPDATED_IS_USED);
        return hkjTempImage;
    }

    @BeforeEach
    public void initTest() {
        hkjTempImage = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjTempImage != null) {
            hkjTempImageRepository.delete(insertedHkjTempImage);
            insertedHkjTempImage = null;
        }
    }

    @Test
    @Transactional
    void createHkjTempImage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjTempImage
        HkjTempImageDTO hkjTempImageDTO = hkjTempImageMapper.toDto(hkjTempImage);
        var returnedHkjTempImageDTO = om.readValue(
            restHkjTempImageMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTempImageDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjTempImageDTO.class
        );

        // Validate the HkjTempImage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjTempImage = hkjTempImageMapper.toEntity(returnedHkjTempImageDTO);
        assertHkjTempImageUpdatableFieldsEquals(returnedHkjTempImage, getPersistedHkjTempImage(returnedHkjTempImage));

        insertedHkjTempImage = returnedHkjTempImage;
    }

    @Test
    @Transactional
    void createHkjTempImageWithExistingId() throws Exception {
        // Create the HkjTempImage with an existing ID
        hkjTempImage.setId(1L);
        HkjTempImageDTO hkjTempImageDTO = hkjTempImageMapper.toDto(hkjTempImage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjTempImageMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTempImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTempImage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkIsUsedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjTempImage.setIsUsed(null);

        // Create the HkjTempImage, which fails.
        HkjTempImageDTO hkjTempImageDTO = hkjTempImageMapper.toDto(hkjTempImage);

        restHkjTempImageMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTempImageDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHkjTempImages() throws Exception {
        // Initialize the database
        insertedHkjTempImage = hkjTempImageRepository.saveAndFlush(hkjTempImage);

        // Get all the hkjTempImageList
        restHkjTempImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjTempImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].isUsed").value(hasItem(DEFAULT_IS_USED.booleanValue())));
    }

    @Test
    @Transactional
    void getHkjTempImage() throws Exception {
        // Initialize the database
        insertedHkjTempImage = hkjTempImageRepository.saveAndFlush(hkjTempImage);

        // Get the hkjTempImage
        restHkjTempImageMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjTempImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjTempImage.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.isUsed").value(DEFAULT_IS_USED.booleanValue()));
    }

    @Test
    @Transactional
    void getHkjTempImagesByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjTempImage = hkjTempImageRepository.saveAndFlush(hkjTempImage);

        Long id = hkjTempImage.getId();

        defaultHkjTempImageFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjTempImageFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjTempImageFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjTempImagesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTempImage = hkjTempImageRepository.saveAndFlush(hkjTempImage);

        // Get all the hkjTempImageList where url equals to
        defaultHkjTempImageFiltering("url.equals=" + DEFAULT_URL, "url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllHkjTempImagesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTempImage = hkjTempImageRepository.saveAndFlush(hkjTempImage);

        // Get all the hkjTempImageList where url in
        defaultHkjTempImageFiltering("url.in=" + DEFAULT_URL + "," + UPDATED_URL, "url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllHkjTempImagesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTempImage = hkjTempImageRepository.saveAndFlush(hkjTempImage);

        // Get all the hkjTempImageList where url is not null
        defaultHkjTempImageFiltering("url.specified=true", "url.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjTempImagesByUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjTempImage = hkjTempImageRepository.saveAndFlush(hkjTempImage);

        // Get all the hkjTempImageList where url contains
        defaultHkjTempImageFiltering("url.contains=" + DEFAULT_URL, "url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllHkjTempImagesByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjTempImage = hkjTempImageRepository.saveAndFlush(hkjTempImage);

        // Get all the hkjTempImageList where url does not contain
        defaultHkjTempImageFiltering("url.doesNotContain=" + UPDATED_URL, "url.doesNotContain=" + DEFAULT_URL);
    }

    @Test
    @Transactional
    void getAllHkjTempImagesByIsUsedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTempImage = hkjTempImageRepository.saveAndFlush(hkjTempImage);

        // Get all the hkjTempImageList where isUsed equals to
        defaultHkjTempImageFiltering("isUsed.equals=" + DEFAULT_IS_USED, "isUsed.equals=" + UPDATED_IS_USED);
    }

    @Test
    @Transactional
    void getAllHkjTempImagesByIsUsedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTempImage = hkjTempImageRepository.saveAndFlush(hkjTempImage);

        // Get all the hkjTempImageList where isUsed in
        defaultHkjTempImageFiltering("isUsed.in=" + DEFAULT_IS_USED + "," + UPDATED_IS_USED, "isUsed.in=" + UPDATED_IS_USED);
    }

    @Test
    @Transactional
    void getAllHkjTempImagesByIsUsedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTempImage = hkjTempImageRepository.saveAndFlush(hkjTempImage);

        // Get all the hkjTempImageList where isUsed is not null
        defaultHkjTempImageFiltering("isUsed.specified=true", "isUsed.specified=false");
    }

    private void defaultHkjTempImageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjTempImageShouldBeFound(shouldBeFound);
        defaultHkjTempImageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjTempImageShouldBeFound(String filter) throws Exception {
        restHkjTempImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjTempImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].isUsed").value(hasItem(DEFAULT_IS_USED.booleanValue())));

        // Check, that the count call also returns 1
        restHkjTempImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjTempImageShouldNotBeFound(String filter) throws Exception {
        restHkjTempImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjTempImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjTempImage() throws Exception {
        // Get the hkjTempImage
        restHkjTempImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjTempImage() throws Exception {
        // Initialize the database
        insertedHkjTempImage = hkjTempImageRepository.saveAndFlush(hkjTempImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjTempImage
        HkjTempImage updatedHkjTempImage = hkjTempImageRepository.findById(hkjTempImage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjTempImage are not directly saved in db
        em.detach(updatedHkjTempImage);
        updatedHkjTempImage.url(UPDATED_URL).isUsed(UPDATED_IS_USED);
        HkjTempImageDTO hkjTempImageDTO = hkjTempImageMapper.toDto(updatedHkjTempImage);

        restHkjTempImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjTempImageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjTempImageDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjTempImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjTempImageToMatchAllProperties(updatedHkjTempImage);
    }

    @Test
    @Transactional
    void putNonExistingHkjTempImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTempImage.setId(longCount.incrementAndGet());

        // Create the HkjTempImage
        HkjTempImageDTO hkjTempImageDTO = hkjTempImageMapper.toDto(hkjTempImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjTempImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjTempImageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjTempImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTempImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjTempImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTempImage.setId(longCount.incrementAndGet());

        // Create the HkjTempImage
        HkjTempImageDTO hkjTempImageDTO = hkjTempImageMapper.toDto(hkjTempImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTempImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjTempImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTempImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjTempImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTempImage.setId(longCount.incrementAndGet());

        // Create the HkjTempImage
        HkjTempImageDTO hkjTempImageDTO = hkjTempImageMapper.toDto(hkjTempImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTempImageMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTempImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjTempImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjTempImageWithPatch() throws Exception {
        // Initialize the database
        insertedHkjTempImage = hkjTempImageRepository.saveAndFlush(hkjTempImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjTempImage using partial update
        HkjTempImage partialUpdatedHkjTempImage = new HkjTempImage();
        partialUpdatedHkjTempImage.setId(hkjTempImage.getId());

        partialUpdatedHkjTempImage.isUsed(UPDATED_IS_USED);

        restHkjTempImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjTempImage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjTempImage))
            )
            .andExpect(status().isOk());

        // Validate the HkjTempImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjTempImageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHkjTempImage, hkjTempImage),
            getPersistedHkjTempImage(hkjTempImage)
        );
    }

    @Test
    @Transactional
    void fullUpdateHkjTempImageWithPatch() throws Exception {
        // Initialize the database
        insertedHkjTempImage = hkjTempImageRepository.saveAndFlush(hkjTempImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjTempImage using partial update
        HkjTempImage partialUpdatedHkjTempImage = new HkjTempImage();
        partialUpdatedHkjTempImage.setId(hkjTempImage.getId());

        partialUpdatedHkjTempImage.url(UPDATED_URL).isUsed(UPDATED_IS_USED);

        restHkjTempImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjTempImage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjTempImage))
            )
            .andExpect(status().isOk());

        // Validate the HkjTempImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjTempImageUpdatableFieldsEquals(partialUpdatedHkjTempImage, getPersistedHkjTempImage(partialUpdatedHkjTempImage));
    }

    @Test
    @Transactional
    void patchNonExistingHkjTempImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTempImage.setId(longCount.incrementAndGet());

        // Create the HkjTempImage
        HkjTempImageDTO hkjTempImageDTO = hkjTempImageMapper.toDto(hkjTempImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjTempImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjTempImageDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjTempImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTempImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjTempImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTempImage.setId(longCount.incrementAndGet());

        // Create the HkjTempImage
        HkjTempImageDTO hkjTempImageDTO = hkjTempImageMapper.toDto(hkjTempImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTempImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjTempImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTempImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjTempImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTempImage.setId(longCount.incrementAndGet());

        // Create the HkjTempImage
        HkjTempImageDTO hkjTempImageDTO = hkjTempImageMapper.toDto(hkjTempImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTempImageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjTempImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjTempImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjTempImage() throws Exception {
        // Initialize the database
        insertedHkjTempImage = hkjTempImageRepository.saveAndFlush(hkjTempImage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjTempImage
        restHkjTempImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjTempImage.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjTempImageRepository.count();
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

    protected HkjTempImage getPersistedHkjTempImage(HkjTempImage hkjTempImage) {
        return hkjTempImageRepository.findById(hkjTempImage.getId()).orElseThrow();
    }

    protected void assertPersistedHkjTempImageToMatchAllProperties(HkjTempImage expectedHkjTempImage) {
        assertHkjTempImageAllPropertiesEquals(expectedHkjTempImage, getPersistedHkjTempImage(expectedHkjTempImage));
    }

    protected void assertPersistedHkjTempImageToMatchUpdatableProperties(HkjTempImage expectedHkjTempImage) {
        assertHkjTempImageAllUpdatablePropertiesEquals(expectedHkjTempImage, getPersistedHkjTempImage(expectedHkjTempImage));
    }
}
