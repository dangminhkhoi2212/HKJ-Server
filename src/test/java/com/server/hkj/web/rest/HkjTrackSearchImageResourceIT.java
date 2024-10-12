package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjTrackSearchImageAsserts.*;
import static com.server.hkj.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.hkj.IntegrationTest;
import com.server.hkj.domain.HkjJewelryModel;
import com.server.hkj.domain.HkjTrackSearchImage;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.repository.HkjTrackSearchImageRepository;
import com.server.hkj.service.dto.HkjTrackSearchImageDTO;
import com.server.hkj.service.mapper.HkjTrackSearchImageMapper;
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
 * Integration tests for the {@link HkjTrackSearchImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjTrackSearchImageResourceIT {

    private static final Integer DEFAULT_SEARCH_ORDER = 1;
    private static final Integer UPDATED_SEARCH_ORDER = 2;
    private static final Integer SMALLER_SEARCH_ORDER = 1 - 1;

    private static final String ENTITY_API_URL = "/api/hkj-track-search-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjTrackSearchImageRepository hkjTrackSearchImageRepository;

    @Autowired
    private HkjTrackSearchImageMapper hkjTrackSearchImageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjTrackSearchImageMockMvc;

    private HkjTrackSearchImage hkjTrackSearchImage;

    private HkjTrackSearchImage insertedHkjTrackSearchImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjTrackSearchImage createEntity() {
        return new HkjTrackSearchImage().searchOrder(DEFAULT_SEARCH_ORDER);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjTrackSearchImage createUpdatedEntity() {
        return new HkjTrackSearchImage().searchOrder(UPDATED_SEARCH_ORDER);
    }

    @BeforeEach
    public void initTest() {
        hkjTrackSearchImage = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjTrackSearchImage != null) {
            hkjTrackSearchImageRepository.delete(insertedHkjTrackSearchImage);
            insertedHkjTrackSearchImage = null;
        }
    }

    @Test
    @Transactional
    void createHkjTrackSearchImage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjTrackSearchImage
        HkjTrackSearchImageDTO hkjTrackSearchImageDTO = hkjTrackSearchImageMapper.toDto(hkjTrackSearchImage);
        var returnedHkjTrackSearchImageDTO = om.readValue(
            restHkjTrackSearchImageMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(hkjTrackSearchImageDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjTrackSearchImageDTO.class
        );

        // Validate the HkjTrackSearchImage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjTrackSearchImage = hkjTrackSearchImageMapper.toEntity(returnedHkjTrackSearchImageDTO);
        assertHkjTrackSearchImageUpdatableFieldsEquals(
            returnedHkjTrackSearchImage,
            getPersistedHkjTrackSearchImage(returnedHkjTrackSearchImage)
        );

        insertedHkjTrackSearchImage = returnedHkjTrackSearchImage;
    }

    @Test
    @Transactional
    void createHkjTrackSearchImageWithExistingId() throws Exception {
        // Create the HkjTrackSearchImage with an existing ID
        hkjTrackSearchImage.setId(1L);
        HkjTrackSearchImageDTO hkjTrackSearchImageDTO = hkjTrackSearchImageMapper.toDto(hkjTrackSearchImage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjTrackSearchImageMockMvc
            .perform(
                post(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjTrackSearchImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTrackSearchImage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHkjTrackSearchImages() throws Exception {
        // Initialize the database
        insertedHkjTrackSearchImage = hkjTrackSearchImageRepository.saveAndFlush(hkjTrackSearchImage);

        // Get all the hkjTrackSearchImageList
        restHkjTrackSearchImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjTrackSearchImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].searchOrder").value(hasItem(DEFAULT_SEARCH_ORDER)));
    }

    @Test
    @Transactional
    void getHkjTrackSearchImage() throws Exception {
        // Initialize the database
        insertedHkjTrackSearchImage = hkjTrackSearchImageRepository.saveAndFlush(hkjTrackSearchImage);

        // Get the hkjTrackSearchImage
        restHkjTrackSearchImageMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjTrackSearchImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjTrackSearchImage.getId().intValue()))
            .andExpect(jsonPath("$.searchOrder").value(DEFAULT_SEARCH_ORDER));
    }

    @Test
    @Transactional
    void getHkjTrackSearchImagesByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjTrackSearchImage = hkjTrackSearchImageRepository.saveAndFlush(hkjTrackSearchImage);

        Long id = hkjTrackSearchImage.getId();

        defaultHkjTrackSearchImageFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjTrackSearchImageFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjTrackSearchImageFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjTrackSearchImagesBySearchOrderIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTrackSearchImage = hkjTrackSearchImageRepository.saveAndFlush(hkjTrackSearchImage);

        // Get all the hkjTrackSearchImageList where searchOrder equals to
        defaultHkjTrackSearchImageFiltering("searchOrder.equals=" + DEFAULT_SEARCH_ORDER, "searchOrder.equals=" + UPDATED_SEARCH_ORDER);
    }

    @Test
    @Transactional
    void getAllHkjTrackSearchImagesBySearchOrderIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTrackSearchImage = hkjTrackSearchImageRepository.saveAndFlush(hkjTrackSearchImage);

        // Get all the hkjTrackSearchImageList where searchOrder in
        defaultHkjTrackSearchImageFiltering(
            "searchOrder.in=" + DEFAULT_SEARCH_ORDER + "," + UPDATED_SEARCH_ORDER,
            "searchOrder.in=" + UPDATED_SEARCH_ORDER
        );
    }

    @Test
    @Transactional
    void getAllHkjTrackSearchImagesBySearchOrderIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTrackSearchImage = hkjTrackSearchImageRepository.saveAndFlush(hkjTrackSearchImage);

        // Get all the hkjTrackSearchImageList where searchOrder is not null
        defaultHkjTrackSearchImageFiltering("searchOrder.specified=true", "searchOrder.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjTrackSearchImagesBySearchOrderIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTrackSearchImage = hkjTrackSearchImageRepository.saveAndFlush(hkjTrackSearchImage);

        // Get all the hkjTrackSearchImageList where searchOrder is greater than or equal to
        defaultHkjTrackSearchImageFiltering(
            "searchOrder.greaterThanOrEqual=" + DEFAULT_SEARCH_ORDER,
            "searchOrder.greaterThanOrEqual=" + UPDATED_SEARCH_ORDER
        );
    }

    @Test
    @Transactional
    void getAllHkjTrackSearchImagesBySearchOrderIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTrackSearchImage = hkjTrackSearchImageRepository.saveAndFlush(hkjTrackSearchImage);

        // Get all the hkjTrackSearchImageList where searchOrder is less than or equal to
        defaultHkjTrackSearchImageFiltering(
            "searchOrder.lessThanOrEqual=" + DEFAULT_SEARCH_ORDER,
            "searchOrder.lessThanOrEqual=" + SMALLER_SEARCH_ORDER
        );
    }

    @Test
    @Transactional
    void getAllHkjTrackSearchImagesBySearchOrderIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjTrackSearchImage = hkjTrackSearchImageRepository.saveAndFlush(hkjTrackSearchImage);

        // Get all the hkjTrackSearchImageList where searchOrder is less than
        defaultHkjTrackSearchImageFiltering("searchOrder.lessThan=" + UPDATED_SEARCH_ORDER, "searchOrder.lessThan=" + DEFAULT_SEARCH_ORDER);
    }

    @Test
    @Transactional
    void getAllHkjTrackSearchImagesBySearchOrderIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjTrackSearchImage = hkjTrackSearchImageRepository.saveAndFlush(hkjTrackSearchImage);

        // Get all the hkjTrackSearchImageList where searchOrder is greater than
        defaultHkjTrackSearchImageFiltering(
            "searchOrder.greaterThan=" + SMALLER_SEARCH_ORDER,
            "searchOrder.greaterThan=" + DEFAULT_SEARCH_ORDER
        );
    }

    @Test
    @Transactional
    void getAllHkjTrackSearchImagesByUserIsEqualToSomething() throws Exception {
        UserExtra user;
        if (TestUtil.findAll(em, UserExtra.class).isEmpty()) {
            hkjTrackSearchImageRepository.saveAndFlush(hkjTrackSearchImage);
            user = UserExtraResourceIT.createEntity();
        } else {
            user = TestUtil.findAll(em, UserExtra.class).get(0);
        }
        em.persist(user);
        em.flush();
        hkjTrackSearchImage.setUser(user);
        hkjTrackSearchImageRepository.saveAndFlush(hkjTrackSearchImage);
        Long userId = user.getId();
        // Get all the hkjTrackSearchImageList where user equals to userId
        defaultHkjTrackSearchImageShouldBeFound("userId.equals=" + userId);

        // Get all the hkjTrackSearchImageList where user equals to (userId + 1)
        defaultHkjTrackSearchImageShouldNotBeFound("userId.equals=" + (userId + 1));
    }

    @Test
    @Transactional
    void getAllHkjTrackSearchImagesByJewelryIsEqualToSomething() throws Exception {
        HkjJewelryModel jewelry;
        if (TestUtil.findAll(em, HkjJewelryModel.class).isEmpty()) {
            hkjTrackSearchImageRepository.saveAndFlush(hkjTrackSearchImage);
            jewelry = HkjJewelryModelResourceIT.createEntity();
        } else {
            jewelry = TestUtil.findAll(em, HkjJewelryModel.class).get(0);
        }
        em.persist(jewelry);
        em.flush();
        hkjTrackSearchImage.setJewelry(jewelry);
        hkjTrackSearchImageRepository.saveAndFlush(hkjTrackSearchImage);
        Long jewelryId = jewelry.getId();
        // Get all the hkjTrackSearchImageList where jewelry equals to jewelryId
        defaultHkjTrackSearchImageShouldBeFound("jewelryId.equals=" + jewelryId);

        // Get all the hkjTrackSearchImageList where jewelry equals to (jewelryId + 1)
        defaultHkjTrackSearchImageShouldNotBeFound("jewelryId.equals=" + (jewelryId + 1));
    }

    private void defaultHkjTrackSearchImageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjTrackSearchImageShouldBeFound(shouldBeFound);
        defaultHkjTrackSearchImageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjTrackSearchImageShouldBeFound(String filter) throws Exception {
        restHkjTrackSearchImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjTrackSearchImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].searchOrder").value(hasItem(DEFAULT_SEARCH_ORDER)));

        // Check, that the count call also returns 1
        restHkjTrackSearchImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjTrackSearchImageShouldNotBeFound(String filter) throws Exception {
        restHkjTrackSearchImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjTrackSearchImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjTrackSearchImage() throws Exception {
        // Get the hkjTrackSearchImage
        restHkjTrackSearchImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjTrackSearchImage() throws Exception {
        // Initialize the database
        insertedHkjTrackSearchImage = hkjTrackSearchImageRepository.saveAndFlush(hkjTrackSearchImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjTrackSearchImage
        HkjTrackSearchImage updatedHkjTrackSearchImage = hkjTrackSearchImageRepository.findById(hkjTrackSearchImage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjTrackSearchImage are not directly saved in db
        em.detach(updatedHkjTrackSearchImage);
        updatedHkjTrackSearchImage.searchOrder(UPDATED_SEARCH_ORDER);
        HkjTrackSearchImageDTO hkjTrackSearchImageDTO = hkjTrackSearchImageMapper.toDto(updatedHkjTrackSearchImage);

        restHkjTrackSearchImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjTrackSearchImageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjTrackSearchImageDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjTrackSearchImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjTrackSearchImageToMatchAllProperties(updatedHkjTrackSearchImage);
    }

    @Test
    @Transactional
    void putNonExistingHkjTrackSearchImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTrackSearchImage.setId(longCount.incrementAndGet());

        // Create the HkjTrackSearchImage
        HkjTrackSearchImageDTO hkjTrackSearchImageDTO = hkjTrackSearchImageMapper.toDto(hkjTrackSearchImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjTrackSearchImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjTrackSearchImageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjTrackSearchImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTrackSearchImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjTrackSearchImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTrackSearchImage.setId(longCount.incrementAndGet());

        // Create the HkjTrackSearchImage
        HkjTrackSearchImageDTO hkjTrackSearchImageDTO = hkjTrackSearchImageMapper.toDto(hkjTrackSearchImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTrackSearchImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjTrackSearchImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTrackSearchImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjTrackSearchImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTrackSearchImage.setId(longCount.incrementAndGet());

        // Create the HkjTrackSearchImage
        HkjTrackSearchImageDTO hkjTrackSearchImageDTO = hkjTrackSearchImageMapper.toDto(hkjTrackSearchImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTrackSearchImageMockMvc
            .perform(
                put(ENTITY_API_URL)
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjTrackSearchImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjTrackSearchImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjTrackSearchImageWithPatch() throws Exception {
        // Initialize the database
        insertedHkjTrackSearchImage = hkjTrackSearchImageRepository.saveAndFlush(hkjTrackSearchImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjTrackSearchImage using partial update
        HkjTrackSearchImage partialUpdatedHkjTrackSearchImage = new HkjTrackSearchImage();
        partialUpdatedHkjTrackSearchImage.setId(hkjTrackSearchImage.getId());

        partialUpdatedHkjTrackSearchImage.searchOrder(UPDATED_SEARCH_ORDER);

        restHkjTrackSearchImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjTrackSearchImage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjTrackSearchImage))
            )
            .andExpect(status().isOk());

        // Validate the HkjTrackSearchImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjTrackSearchImageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHkjTrackSearchImage, hkjTrackSearchImage),
            getPersistedHkjTrackSearchImage(hkjTrackSearchImage)
        );
    }

    @Test
    @Transactional
    void fullUpdateHkjTrackSearchImageWithPatch() throws Exception {
        // Initialize the database
        insertedHkjTrackSearchImage = hkjTrackSearchImageRepository.saveAndFlush(hkjTrackSearchImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjTrackSearchImage using partial update
        HkjTrackSearchImage partialUpdatedHkjTrackSearchImage = new HkjTrackSearchImage();
        partialUpdatedHkjTrackSearchImage.setId(hkjTrackSearchImage.getId());

        partialUpdatedHkjTrackSearchImage.searchOrder(UPDATED_SEARCH_ORDER);

        restHkjTrackSearchImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjTrackSearchImage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjTrackSearchImage))
            )
            .andExpect(status().isOk());

        // Validate the HkjTrackSearchImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjTrackSearchImageUpdatableFieldsEquals(
            partialUpdatedHkjTrackSearchImage,
            getPersistedHkjTrackSearchImage(partialUpdatedHkjTrackSearchImage)
        );
    }

    @Test
    @Transactional
    void patchNonExistingHkjTrackSearchImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTrackSearchImage.setId(longCount.incrementAndGet());

        // Create the HkjTrackSearchImage
        HkjTrackSearchImageDTO hkjTrackSearchImageDTO = hkjTrackSearchImageMapper.toDto(hkjTrackSearchImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjTrackSearchImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjTrackSearchImageDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjTrackSearchImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTrackSearchImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjTrackSearchImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTrackSearchImage.setId(longCount.incrementAndGet());

        // Create the HkjTrackSearchImage
        HkjTrackSearchImageDTO hkjTrackSearchImageDTO = hkjTrackSearchImageMapper.toDto(hkjTrackSearchImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTrackSearchImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjTrackSearchImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTrackSearchImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjTrackSearchImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTrackSearchImage.setId(longCount.incrementAndGet());

        // Create the HkjTrackSearchImage
        HkjTrackSearchImageDTO hkjTrackSearchImageDTO = hkjTrackSearchImageMapper.toDto(hkjTrackSearchImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTrackSearchImageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjTrackSearchImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjTrackSearchImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjTrackSearchImage() throws Exception {
        // Initialize the database
        insertedHkjTrackSearchImage = hkjTrackSearchImageRepository.saveAndFlush(hkjTrackSearchImage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjTrackSearchImage
        restHkjTrackSearchImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjTrackSearchImage.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjTrackSearchImageRepository.count();
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

    protected HkjTrackSearchImage getPersistedHkjTrackSearchImage(HkjTrackSearchImage hkjTrackSearchImage) {
        return hkjTrackSearchImageRepository.findById(hkjTrackSearchImage.getId()).orElseThrow();
    }

    protected void assertPersistedHkjTrackSearchImageToMatchAllProperties(HkjTrackSearchImage expectedHkjTrackSearchImage) {
        assertHkjTrackSearchImageAllPropertiesEquals(
            expectedHkjTrackSearchImage,
            getPersistedHkjTrackSearchImage(expectedHkjTrackSearchImage)
        );
    }

    protected void assertPersistedHkjTrackSearchImageToMatchUpdatableProperties(HkjTrackSearchImage expectedHkjTrackSearchImage) {
        assertHkjTrackSearchImageAllUpdatablePropertiesEquals(
            expectedHkjTrackSearchImage,
            getPersistedHkjTrackSearchImage(expectedHkjTrackSearchImage)
        );
    }
}
