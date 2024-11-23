package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjOrderImageAsserts.*;
import static com.server.hkj.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.hkj.IntegrationTest;
import com.server.hkj.domain.HkjOrderImage;
import com.server.hkj.domain.HkjOrderItem;
import com.server.hkj.repository.HkjOrderImageRepository;
import com.server.hkj.service.dto.HkjOrderImageDTO;
import com.server.hkj.service.mapper.HkjOrderImageMapper;
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
 * Integration tests for the {@link HkjOrderImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjOrderImageResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/hkj-order-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjOrderImageRepository hkjOrderImageRepository;

    @Autowired
    private HkjOrderImageMapper hkjOrderImageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjOrderImageMockMvc;

    private HkjOrderImage hkjOrderImage;

    private HkjOrderImage insertedHkjOrderImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjOrderImage createEntity() {
        return new HkjOrderImage().url(DEFAULT_URL).isDeleted(DEFAULT_IS_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjOrderImage createUpdatedEntity() {
        return new HkjOrderImage().url(UPDATED_URL).isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    public void initTest() {
        hkjOrderImage = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjOrderImage != null) {
            hkjOrderImageRepository.delete(insertedHkjOrderImage);
            insertedHkjOrderImage = null;
        }
    }

    @Test
    @Transactional
    void createHkjOrderImage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjOrderImage
        HkjOrderImageDTO hkjOrderImageDTO = hkjOrderImageMapper.toDto(hkjOrderImage);
        var returnedHkjOrderImageDTO = om.readValue(
            restHkjOrderImageMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(hkjOrderImageDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjOrderImageDTO.class
        );

        // Validate the HkjOrderImage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjOrderImage = hkjOrderImageMapper.toEntity(returnedHkjOrderImageDTO);
        assertHkjOrderImageUpdatableFieldsEquals(returnedHkjOrderImage, getPersistedHkjOrderImage(returnedHkjOrderImage));

        insertedHkjOrderImage = returnedHkjOrderImage;
    }

    @Test
    @Transactional
    void createHkjOrderImageWithExistingId() throws Exception {
        // Create the HkjOrderImage with an existing ID
        hkjOrderImage.setId(1L);
        HkjOrderImageDTO hkjOrderImageDTO = hkjOrderImageMapper.toDto(hkjOrderImage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjOrderImageMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjOrderImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjOrderImage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHkjOrderImages() throws Exception {
        // Initialize the database
        insertedHkjOrderImage = hkjOrderImageRepository.saveAndFlush(hkjOrderImage);

        // Get all the hkjOrderImageList
        restHkjOrderImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjOrderImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getHkjOrderImage() throws Exception {
        // Initialize the database
        insertedHkjOrderImage = hkjOrderImageRepository.saveAndFlush(hkjOrderImage);

        // Get the hkjOrderImage
        restHkjOrderImageMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjOrderImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjOrderImage.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getHkjOrderImagesByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjOrderImage = hkjOrderImageRepository.saveAndFlush(hkjOrderImage);

        Long id = hkjOrderImage.getId();

        defaultHkjOrderImageFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjOrderImageFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjOrderImageFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjOrderImagesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderImage = hkjOrderImageRepository.saveAndFlush(hkjOrderImage);

        // Get all the hkjOrderImageList where url equals to
        defaultHkjOrderImageFiltering("url.equals=" + DEFAULT_URL, "url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllHkjOrderImagesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjOrderImage = hkjOrderImageRepository.saveAndFlush(hkjOrderImage);

        // Get all the hkjOrderImageList where url in
        defaultHkjOrderImageFiltering("url.in=" + DEFAULT_URL + "," + UPDATED_URL, "url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllHkjOrderImagesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjOrderImage = hkjOrderImageRepository.saveAndFlush(hkjOrderImage);

        // Get all the hkjOrderImageList where url is not null
        defaultHkjOrderImageFiltering("url.specified=true", "url.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjOrderImagesByUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderImage = hkjOrderImageRepository.saveAndFlush(hkjOrderImage);

        // Get all the hkjOrderImageList where url contains
        defaultHkjOrderImageFiltering("url.contains=" + DEFAULT_URL, "url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllHkjOrderImagesByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderImage = hkjOrderImageRepository.saveAndFlush(hkjOrderImage);

        // Get all the hkjOrderImageList where url does not contain
        defaultHkjOrderImageFiltering("url.doesNotContain=" + UPDATED_URL, "url.doesNotContain=" + DEFAULT_URL);
    }

    @Test
    @Transactional
    void getAllHkjOrderImagesByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderImage = hkjOrderImageRepository.saveAndFlush(hkjOrderImage);

        // Get all the hkjOrderImageList where isDeleted equals to
        defaultHkjOrderImageFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjOrderImagesByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjOrderImage = hkjOrderImageRepository.saveAndFlush(hkjOrderImage);

        // Get all the hkjOrderImageList where isDeleted in
        defaultHkjOrderImageFiltering(
            "isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED,
            "isDeleted.in=" + UPDATED_IS_DELETED
        );
    }

    @Test
    @Transactional
    void getAllHkjOrderImagesByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjOrderImage = hkjOrderImageRepository.saveAndFlush(hkjOrderImage);

        // Get all the hkjOrderImageList where isDeleted is not null
        defaultHkjOrderImageFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjOrderImagesByOrderItemIsEqualToSomething() throws Exception {
        HkjOrderItem orderItem;
        if (TestUtil.findAll(em, HkjOrderItem.class).isEmpty()) {
            hkjOrderImageRepository.saveAndFlush(hkjOrderImage);
            orderItem = HkjOrderItemResourceIT.createEntity();
        } else {
            orderItem = TestUtil.findAll(em, HkjOrderItem.class).get(0);
        }
        em.persist(orderItem);
        em.flush();
        hkjOrderImage.setOrderItem(orderItem);
        hkjOrderImageRepository.saveAndFlush(hkjOrderImage);
        Long orderItemId = orderItem.getId();
        // Get all the hkjOrderImageList where orderItem equals to orderItemId
        defaultHkjOrderImageShouldBeFound("orderItemId.equals=" + orderItemId);

        // Get all the hkjOrderImageList where orderItem equals to (orderItemId + 1)
        defaultHkjOrderImageShouldNotBeFound("orderItemId.equals=" + (orderItemId + 1));
    }

    private void defaultHkjOrderImageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjOrderImageShouldBeFound(shouldBeFound);
        defaultHkjOrderImageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjOrderImageShouldBeFound(String filter) throws Exception {
        restHkjOrderImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjOrderImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restHkjOrderImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjOrderImageShouldNotBeFound(String filter) throws Exception {
        restHkjOrderImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjOrderImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjOrderImage() throws Exception {
        // Get the hkjOrderImage
        restHkjOrderImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjOrderImage() throws Exception {
        // Initialize the database
        insertedHkjOrderImage = hkjOrderImageRepository.saveAndFlush(hkjOrderImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjOrderImage
        HkjOrderImage updatedHkjOrderImage = hkjOrderImageRepository.findById(hkjOrderImage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjOrderImage are not directly saved in db
        em.detach(updatedHkjOrderImage);
        updatedHkjOrderImage.url(UPDATED_URL).isDeleted(UPDATED_IS_DELETED);
        HkjOrderImageDTO hkjOrderImageDTO = hkjOrderImageMapper.toDto(updatedHkjOrderImage);

        restHkjOrderImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjOrderImageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjOrderImageDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjOrderImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjOrderImageToMatchAllProperties(updatedHkjOrderImage);
    }

    @Test
    @Transactional
    void putNonExistingHkjOrderImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjOrderImage.setId(longCount.incrementAndGet());

        // Create the HkjOrderImage
        HkjOrderImageDTO hkjOrderImageDTO = hkjOrderImageMapper.toDto(hkjOrderImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjOrderImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjOrderImageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjOrderImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjOrderImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjOrderImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjOrderImage.setId(longCount.incrementAndGet());

        // Create the HkjOrderImage
        HkjOrderImageDTO hkjOrderImageDTO = hkjOrderImageMapper.toDto(hkjOrderImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjOrderImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjOrderImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjOrderImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjOrderImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjOrderImage.setId(longCount.incrementAndGet());

        // Create the HkjOrderImage
        HkjOrderImageDTO hkjOrderImageDTO = hkjOrderImageMapper.toDto(hkjOrderImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjOrderImageMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjOrderImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjOrderImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjOrderImageWithPatch() throws Exception {
        // Initialize the database
        insertedHkjOrderImage = hkjOrderImageRepository.saveAndFlush(hkjOrderImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjOrderImage using partial update
        HkjOrderImage partialUpdatedHkjOrderImage = new HkjOrderImage();
        partialUpdatedHkjOrderImage.setId(hkjOrderImage.getId());

        partialUpdatedHkjOrderImage.url(UPDATED_URL).isDeleted(UPDATED_IS_DELETED);

        restHkjOrderImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjOrderImage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjOrderImage))
            )
            .andExpect(status().isOk());

        // Validate the HkjOrderImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjOrderImageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHkjOrderImage, hkjOrderImage),
            getPersistedHkjOrderImage(hkjOrderImage)
        );
    }

    @Test
    @Transactional
    void fullUpdateHkjOrderImageWithPatch() throws Exception {
        // Initialize the database
        insertedHkjOrderImage = hkjOrderImageRepository.saveAndFlush(hkjOrderImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjOrderImage using partial update
        HkjOrderImage partialUpdatedHkjOrderImage = new HkjOrderImage();
        partialUpdatedHkjOrderImage.setId(hkjOrderImage.getId());

        partialUpdatedHkjOrderImage.url(UPDATED_URL).isDeleted(UPDATED_IS_DELETED);

        restHkjOrderImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjOrderImage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjOrderImage))
            )
            .andExpect(status().isOk());

        // Validate the HkjOrderImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjOrderImageUpdatableFieldsEquals(partialUpdatedHkjOrderImage, getPersistedHkjOrderImage(partialUpdatedHkjOrderImage));
    }

    @Test
    @Transactional
    void patchNonExistingHkjOrderImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjOrderImage.setId(longCount.incrementAndGet());

        // Create the HkjOrderImage
        HkjOrderImageDTO hkjOrderImageDTO = hkjOrderImageMapper.toDto(hkjOrderImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjOrderImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjOrderImageDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjOrderImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjOrderImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjOrderImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjOrderImage.setId(longCount.incrementAndGet());

        // Create the HkjOrderImage
        HkjOrderImageDTO hkjOrderImageDTO = hkjOrderImageMapper.toDto(hkjOrderImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjOrderImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjOrderImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjOrderImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjOrderImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjOrderImage.setId(longCount.incrementAndGet());

        // Create the HkjOrderImage
        HkjOrderImageDTO hkjOrderImageDTO = hkjOrderImageMapper.toDto(hkjOrderImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjOrderImageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjOrderImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjOrderImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjOrderImage() throws Exception {
        // Initialize the database
        insertedHkjOrderImage = hkjOrderImageRepository.saveAndFlush(hkjOrderImage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjOrderImage
        restHkjOrderImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjOrderImage.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjOrderImageRepository.count();
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

    protected HkjOrderImage getPersistedHkjOrderImage(HkjOrderImage hkjOrderImage) {
        return hkjOrderImageRepository.findById(hkjOrderImage.getId()).orElseThrow();
    }

    protected void assertPersistedHkjOrderImageToMatchAllProperties(HkjOrderImage expectedHkjOrderImage) {
        assertHkjOrderImageAllPropertiesEquals(expectedHkjOrderImage, getPersistedHkjOrderImage(expectedHkjOrderImage));
    }

    protected void assertPersistedHkjOrderImageToMatchUpdatableProperties(HkjOrderImage expectedHkjOrderImage) {
        assertHkjOrderImageAllUpdatablePropertiesEquals(expectedHkjOrderImage, getPersistedHkjOrderImage(expectedHkjOrderImage));
    }
}
