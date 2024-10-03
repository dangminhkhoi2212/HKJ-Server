package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjMaterialImageAsserts.*;
import static com.server.hkj.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.hkj.IntegrationTest;
import com.server.hkj.domain.HkjMaterial;
import com.server.hkj.domain.HkjMaterialImage;
import com.server.hkj.repository.HkjMaterialImageRepository;
import com.server.hkj.service.dto.HkjMaterialImageDTO;
import com.server.hkj.service.mapper.HkjMaterialImageMapper;
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
 * Integration tests for the {@link HkjMaterialImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjMaterialImageResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/hkj-material-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjMaterialImageRepository hkjMaterialImageRepository;

    @Autowired
    private HkjMaterialImageMapper hkjMaterialImageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjMaterialImageMockMvc;

    private HkjMaterialImage hkjMaterialImage;

    private HkjMaterialImage insertedHkjMaterialImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjMaterialImage createEntity() {
        return new HkjMaterialImage().url(DEFAULT_URL).isDeleted(DEFAULT_IS_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjMaterialImage createUpdatedEntity() {
        return new HkjMaterialImage().url(UPDATED_URL).isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    public void initTest() {
        hkjMaterialImage = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjMaterialImage != null) {
            hkjMaterialImageRepository.delete(insertedHkjMaterialImage);
            insertedHkjMaterialImage = null;
        }
    }

    @Test
    @Transactional
    void createHkjMaterialImage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjMaterialImage
        HkjMaterialImageDTO hkjMaterialImageDTO = hkjMaterialImageMapper.toDto(hkjMaterialImage);
        var returnedHkjMaterialImageDTO = om.readValue(
            restHkjMaterialImageMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(hkjMaterialImageDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjMaterialImageDTO.class
        );

        // Validate the HkjMaterialImage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjMaterialImage = hkjMaterialImageMapper.toEntity(returnedHkjMaterialImageDTO);
        assertHkjMaterialImageUpdatableFieldsEquals(returnedHkjMaterialImage, getPersistedHkjMaterialImage(returnedHkjMaterialImage));

        insertedHkjMaterialImage = returnedHkjMaterialImage;
    }

    @Test
    @Transactional
    void createHkjMaterialImageWithExistingId() throws Exception {
        // Create the HkjMaterialImage with an existing ID
        hkjMaterialImage.setId(1L);
        HkjMaterialImageDTO hkjMaterialImageDTO = hkjMaterialImageMapper.toDto(hkjMaterialImage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjMaterialImageMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjMaterialImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjMaterialImage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHkjMaterialImages() throws Exception {
        // Initialize the database
        insertedHkjMaterialImage = hkjMaterialImageRepository.saveAndFlush(hkjMaterialImage);

        // Get all the hkjMaterialImageList
        restHkjMaterialImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjMaterialImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getHkjMaterialImage() throws Exception {
        // Initialize the database
        insertedHkjMaterialImage = hkjMaterialImageRepository.saveAndFlush(hkjMaterialImage);

        // Get the hkjMaterialImage
        restHkjMaterialImageMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjMaterialImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjMaterialImage.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getHkjMaterialImagesByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjMaterialImage = hkjMaterialImageRepository.saveAndFlush(hkjMaterialImage);

        Long id = hkjMaterialImage.getId();

        defaultHkjMaterialImageFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjMaterialImageFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjMaterialImageFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjMaterialImagesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialImage = hkjMaterialImageRepository.saveAndFlush(hkjMaterialImage);

        // Get all the hkjMaterialImageList where url equals to
        defaultHkjMaterialImageFiltering("url.equals=" + DEFAULT_URL, "url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllHkjMaterialImagesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjMaterialImage = hkjMaterialImageRepository.saveAndFlush(hkjMaterialImage);

        // Get all the hkjMaterialImageList where url in
        defaultHkjMaterialImageFiltering("url.in=" + DEFAULT_URL + "," + UPDATED_URL, "url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllHkjMaterialImagesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjMaterialImage = hkjMaterialImageRepository.saveAndFlush(hkjMaterialImage);

        // Get all the hkjMaterialImageList where url is not null
        defaultHkjMaterialImageFiltering("url.specified=true", "url.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjMaterialImagesByUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialImage = hkjMaterialImageRepository.saveAndFlush(hkjMaterialImage);

        // Get all the hkjMaterialImageList where url contains
        defaultHkjMaterialImageFiltering("url.contains=" + DEFAULT_URL, "url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllHkjMaterialImagesByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialImage = hkjMaterialImageRepository.saveAndFlush(hkjMaterialImage);

        // Get all the hkjMaterialImageList where url does not contain
        defaultHkjMaterialImageFiltering("url.doesNotContain=" + UPDATED_URL, "url.doesNotContain=" + DEFAULT_URL);
    }

    @Test
    @Transactional
    void getAllHkjMaterialImagesByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialImage = hkjMaterialImageRepository.saveAndFlush(hkjMaterialImage);

        // Get all the hkjMaterialImageList where isDeleted equals to
        defaultHkjMaterialImageFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjMaterialImagesByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjMaterialImage = hkjMaterialImageRepository.saveAndFlush(hkjMaterialImage);

        // Get all the hkjMaterialImageList where isDeleted in
        defaultHkjMaterialImageFiltering(
            "isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED,
            "isDeleted.in=" + UPDATED_IS_DELETED
        );
    }

    @Test
    @Transactional
    void getAllHkjMaterialImagesByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjMaterialImage = hkjMaterialImageRepository.saveAndFlush(hkjMaterialImage);

        // Get all the hkjMaterialImageList where isDeleted is not null
        defaultHkjMaterialImageFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjMaterialImagesByMaterialIsEqualToSomething() throws Exception {
        HkjMaterial material;
        if (TestUtil.findAll(em, HkjMaterial.class).isEmpty()) {
            hkjMaterialImageRepository.saveAndFlush(hkjMaterialImage);
            material = HkjMaterialResourceIT.createEntity();
        } else {
            material = TestUtil.findAll(em, HkjMaterial.class).get(0);
        }
        em.persist(material);
        em.flush();
        hkjMaterialImage.setMaterial(material);
        hkjMaterialImageRepository.saveAndFlush(hkjMaterialImage);
        Long materialId = material.getId();
        // Get all the hkjMaterialImageList where material equals to materialId
        defaultHkjMaterialImageShouldBeFound("materialId.equals=" + materialId);

        // Get all the hkjMaterialImageList where material equals to (materialId + 1)
        defaultHkjMaterialImageShouldNotBeFound("materialId.equals=" + (materialId + 1));
    }

    private void defaultHkjMaterialImageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjMaterialImageShouldBeFound(shouldBeFound);
        defaultHkjMaterialImageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjMaterialImageShouldBeFound(String filter) throws Exception {
        restHkjMaterialImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjMaterialImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restHkjMaterialImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjMaterialImageShouldNotBeFound(String filter) throws Exception {
        restHkjMaterialImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjMaterialImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjMaterialImage() throws Exception {
        // Get the hkjMaterialImage
        restHkjMaterialImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjMaterialImage() throws Exception {
        // Initialize the database
        insertedHkjMaterialImage = hkjMaterialImageRepository.saveAndFlush(hkjMaterialImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjMaterialImage
        HkjMaterialImage updatedHkjMaterialImage = hkjMaterialImageRepository.findById(hkjMaterialImage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjMaterialImage are not directly saved in db
        em.detach(updatedHkjMaterialImage);
        updatedHkjMaterialImage.url(UPDATED_URL).isDeleted(UPDATED_IS_DELETED);
        HkjMaterialImageDTO hkjMaterialImageDTO = hkjMaterialImageMapper.toDto(updatedHkjMaterialImage);

        restHkjMaterialImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjMaterialImageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjMaterialImageDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjMaterialImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjMaterialImageToMatchAllProperties(updatedHkjMaterialImage);
    }

    @Test
    @Transactional
    void putNonExistingHkjMaterialImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjMaterialImage.setId(longCount.incrementAndGet());

        // Create the HkjMaterialImage
        HkjMaterialImageDTO hkjMaterialImageDTO = hkjMaterialImageMapper.toDto(hkjMaterialImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjMaterialImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjMaterialImageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjMaterialImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjMaterialImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjMaterialImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjMaterialImage.setId(longCount.incrementAndGet());

        // Create the HkjMaterialImage
        HkjMaterialImageDTO hkjMaterialImageDTO = hkjMaterialImageMapper.toDto(hkjMaterialImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjMaterialImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjMaterialImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjMaterialImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjMaterialImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjMaterialImage.setId(longCount.incrementAndGet());

        // Create the HkjMaterialImage
        HkjMaterialImageDTO hkjMaterialImageDTO = hkjMaterialImageMapper.toDto(hkjMaterialImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjMaterialImageMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjMaterialImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjMaterialImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjMaterialImageWithPatch() throws Exception {
        // Initialize the database
        insertedHkjMaterialImage = hkjMaterialImageRepository.saveAndFlush(hkjMaterialImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjMaterialImage using partial update
        HkjMaterialImage partialUpdatedHkjMaterialImage = new HkjMaterialImage();
        partialUpdatedHkjMaterialImage.setId(hkjMaterialImage.getId());

        partialUpdatedHkjMaterialImage.url(UPDATED_URL);

        restHkjMaterialImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjMaterialImage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjMaterialImage))
            )
            .andExpect(status().isOk());

        // Validate the HkjMaterialImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjMaterialImageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHkjMaterialImage, hkjMaterialImage),
            getPersistedHkjMaterialImage(hkjMaterialImage)
        );
    }

    @Test
    @Transactional
    void fullUpdateHkjMaterialImageWithPatch() throws Exception {
        // Initialize the database
        insertedHkjMaterialImage = hkjMaterialImageRepository.saveAndFlush(hkjMaterialImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjMaterialImage using partial update
        HkjMaterialImage partialUpdatedHkjMaterialImage = new HkjMaterialImage();
        partialUpdatedHkjMaterialImage.setId(hkjMaterialImage.getId());

        partialUpdatedHkjMaterialImage.url(UPDATED_URL).isDeleted(UPDATED_IS_DELETED);

        restHkjMaterialImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjMaterialImage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjMaterialImage))
            )
            .andExpect(status().isOk());

        // Validate the HkjMaterialImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjMaterialImageUpdatableFieldsEquals(
            partialUpdatedHkjMaterialImage,
            getPersistedHkjMaterialImage(partialUpdatedHkjMaterialImage)
        );
    }

    @Test
    @Transactional
    void patchNonExistingHkjMaterialImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjMaterialImage.setId(longCount.incrementAndGet());

        // Create the HkjMaterialImage
        HkjMaterialImageDTO hkjMaterialImageDTO = hkjMaterialImageMapper.toDto(hkjMaterialImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjMaterialImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjMaterialImageDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjMaterialImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjMaterialImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjMaterialImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjMaterialImage.setId(longCount.incrementAndGet());

        // Create the HkjMaterialImage
        HkjMaterialImageDTO hkjMaterialImageDTO = hkjMaterialImageMapper.toDto(hkjMaterialImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjMaterialImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjMaterialImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjMaterialImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjMaterialImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjMaterialImage.setId(longCount.incrementAndGet());

        // Create the HkjMaterialImage
        HkjMaterialImageDTO hkjMaterialImageDTO = hkjMaterialImageMapper.toDto(hkjMaterialImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjMaterialImageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjMaterialImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjMaterialImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjMaterialImage() throws Exception {
        // Initialize the database
        insertedHkjMaterialImage = hkjMaterialImageRepository.saveAndFlush(hkjMaterialImage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjMaterialImage
        restHkjMaterialImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjMaterialImage.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjMaterialImageRepository.count();
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

    protected HkjMaterialImage getPersistedHkjMaterialImage(HkjMaterialImage hkjMaterialImage) {
        return hkjMaterialImageRepository.findById(hkjMaterialImage.getId()).orElseThrow();
    }

    protected void assertPersistedHkjMaterialImageToMatchAllProperties(HkjMaterialImage expectedHkjMaterialImage) {
        assertHkjMaterialImageAllPropertiesEquals(expectedHkjMaterialImage, getPersistedHkjMaterialImage(expectedHkjMaterialImage));
    }

    protected void assertPersistedHkjMaterialImageToMatchUpdatableProperties(HkjMaterialImage expectedHkjMaterialImage) {
        assertHkjMaterialImageAllUpdatablePropertiesEquals(
            expectedHkjMaterialImage,
            getPersistedHkjMaterialImage(expectedHkjMaterialImage)
        );
    }
}
