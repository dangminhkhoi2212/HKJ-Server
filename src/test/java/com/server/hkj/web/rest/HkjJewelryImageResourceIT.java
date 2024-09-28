package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjJewelryImageAsserts.*;
import static com.server.hkj.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.hkj.IntegrationTest;
import com.server.hkj.domain.HkjJewelryImage;
import com.server.hkj.domain.HkjJewelryModel;
import com.server.hkj.repository.HkjJewelryImageRepository;
import com.server.hkj.service.dto.HkjJewelryImageDTO;
import com.server.hkj.service.mapper.HkjJewelryImageMapper;
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
 * Integration tests for the {@link HkjJewelryImageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjJewelryImageResourceIT {

    private static final String DEFAULT_URL = "AAAAAAAAAA";
    private static final String UPDATED_URL = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_SEARCH_IMAGE = false;
    private static final Boolean UPDATED_IS_SEARCH_IMAGE = true;

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_TAGS = "AAAAAAAAAA";
    private static final String UPDATED_TAGS = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/hkj-jewelry-images";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjJewelryImageRepository hkjJewelryImageRepository;

    @Autowired
    private HkjJewelryImageMapper hkjJewelryImageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjJewelryImageMockMvc;

    private HkjJewelryImage hkjJewelryImage;

    private HkjJewelryImage insertedHkjJewelryImage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjJewelryImage createEntity() {
        return new HkjJewelryImage()
            .url(DEFAULT_URL)
            .isSearchImage(DEFAULT_IS_SEARCH_IMAGE)
            .description(DEFAULT_DESCRIPTION)
            .tags(DEFAULT_TAGS)
            .isDeleted(DEFAULT_IS_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjJewelryImage createUpdatedEntity() {
        return new HkjJewelryImage()
            .url(UPDATED_URL)
            .isSearchImage(UPDATED_IS_SEARCH_IMAGE)
            .description(UPDATED_DESCRIPTION)
            .tags(UPDATED_TAGS)
            .isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    public void initTest() {
        hkjJewelryImage = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjJewelryImage != null) {
            hkjJewelryImageRepository.delete(insertedHkjJewelryImage);
            insertedHkjJewelryImage = null;
        }
    }

    @Test
    @Transactional
    void createHkjJewelryImage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjJewelryImage
        HkjJewelryImageDTO hkjJewelryImageDTO = hkjJewelryImageMapper.toDto(hkjJewelryImage);
        var returnedHkjJewelryImageDTO = om.readValue(
            restHkjJewelryImageMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(hkjJewelryImageDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjJewelryImageDTO.class
        );

        // Validate the HkjJewelryImage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjJewelryImage = hkjJewelryImageMapper.toEntity(returnedHkjJewelryImageDTO);
        assertHkjJewelryImageUpdatableFieldsEquals(returnedHkjJewelryImage, getPersistedHkjJewelryImage(returnedHkjJewelryImage));

        insertedHkjJewelryImage = returnedHkjJewelryImage;
    }

    @Test
    @Transactional
    void createHkjJewelryImageWithExistingId() throws Exception {
        // Create the HkjJewelryImage with an existing ID
        hkjJewelryImage.setId(1L);
        HkjJewelryImageDTO hkjJewelryImageDTO = hkjJewelryImageMapper.toDto(hkjJewelryImage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjJewelryImageMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjJewelryImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjJewelryImage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkUrlIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjJewelryImage.setUrl(null);

        // Create the HkjJewelryImage, which fails.
        HkjJewelryImageDTO hkjJewelryImageDTO = hkjJewelryImageMapper.toDto(hkjJewelryImage);

        restHkjJewelryImageMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjJewelryImageDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsSearchImageIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjJewelryImage.setIsSearchImage(null);

        // Create the HkjJewelryImage, which fails.
        HkjJewelryImageDTO hkjJewelryImageDTO = hkjJewelryImageMapper.toDto(hkjJewelryImage);

        restHkjJewelryImageMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjJewelryImageDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHkjJewelryImages() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList
        restHkjJewelryImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjJewelryImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].isSearchImage").value(hasItem(DEFAULT_IS_SEARCH_IMAGE.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getHkjJewelryImage() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get the hkjJewelryImage
        restHkjJewelryImageMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjJewelryImage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjJewelryImage.getId().intValue()))
            .andExpect(jsonPath("$.url").value(DEFAULT_URL))
            .andExpect(jsonPath("$.isSearchImage").value(DEFAULT_IS_SEARCH_IMAGE.booleanValue()))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.tags").value(DEFAULT_TAGS))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getHkjJewelryImagesByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        Long id = hkjJewelryImage.getId();

        defaultHkjJewelryImageFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjJewelryImageFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjJewelryImageFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByUrlIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where url equals to
        defaultHkjJewelryImageFiltering("url.equals=" + DEFAULT_URL, "url.equals=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByUrlIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where url in
        defaultHkjJewelryImageFiltering("url.in=" + DEFAULT_URL + "," + UPDATED_URL, "url.in=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByUrlIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where url is not null
        defaultHkjJewelryImageFiltering("url.specified=true", "url.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByUrlContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where url contains
        defaultHkjJewelryImageFiltering("url.contains=" + DEFAULT_URL, "url.contains=" + UPDATED_URL);
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByUrlNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where url does not contain
        defaultHkjJewelryImageFiltering("url.doesNotContain=" + UPDATED_URL, "url.doesNotContain=" + DEFAULT_URL);
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByIsSearchImageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where isSearchImage equals to
        defaultHkjJewelryImageFiltering(
            "isSearchImage.equals=" + DEFAULT_IS_SEARCH_IMAGE,
            "isSearchImage.equals=" + UPDATED_IS_SEARCH_IMAGE
        );
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByIsSearchImageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where isSearchImage in
        defaultHkjJewelryImageFiltering(
            "isSearchImage.in=" + DEFAULT_IS_SEARCH_IMAGE + "," + UPDATED_IS_SEARCH_IMAGE,
            "isSearchImage.in=" + UPDATED_IS_SEARCH_IMAGE
        );
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByIsSearchImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where isSearchImage is not null
        defaultHkjJewelryImageFiltering("isSearchImage.specified=true", "isSearchImage.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where description equals to
        defaultHkjJewelryImageFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where description in
        defaultHkjJewelryImageFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where description is not null
        defaultHkjJewelryImageFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where description contains
        defaultHkjJewelryImageFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where description does not contain
        defaultHkjJewelryImageFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByTagsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where tags equals to
        defaultHkjJewelryImageFiltering("tags.equals=" + DEFAULT_TAGS, "tags.equals=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByTagsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where tags in
        defaultHkjJewelryImageFiltering("tags.in=" + DEFAULT_TAGS + "," + UPDATED_TAGS, "tags.in=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByTagsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where tags is not null
        defaultHkjJewelryImageFiltering("tags.specified=true", "tags.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByTagsContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where tags contains
        defaultHkjJewelryImageFiltering("tags.contains=" + DEFAULT_TAGS, "tags.contains=" + UPDATED_TAGS);
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByTagsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where tags does not contain
        defaultHkjJewelryImageFiltering("tags.doesNotContain=" + UPDATED_TAGS, "tags.doesNotContain=" + DEFAULT_TAGS);
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where isDeleted equals to
        defaultHkjJewelryImageFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where isDeleted in
        defaultHkjJewelryImageFiltering(
            "isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED,
            "isDeleted.in=" + UPDATED_IS_DELETED
        );
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        // Get all the hkjJewelryImageList where isDeleted is not null
        defaultHkjJewelryImageFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjJewelryImagesByJewelryModelIsEqualToSomething() throws Exception {
        HkjJewelryModel jewelryModel;
        if (TestUtil.findAll(em, HkjJewelryModel.class).isEmpty()) {
            hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);
            jewelryModel = HkjJewelryModelResourceIT.createEntity();
        } else {
            jewelryModel = TestUtil.findAll(em, HkjJewelryModel.class).get(0);
        }
        em.persist(jewelryModel);
        em.flush();
        hkjJewelryImage.setJewelryModel(jewelryModel);
        hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);
        Long jewelryModelId = jewelryModel.getId();
        // Get all the hkjJewelryImageList where jewelryModel equals to jewelryModelId
        defaultHkjJewelryImageShouldBeFound("jewelryModelId.equals=" + jewelryModelId);

        // Get all the hkjJewelryImageList where jewelryModel equals to (jewelryModelId + 1)
        defaultHkjJewelryImageShouldNotBeFound("jewelryModelId.equals=" + (jewelryModelId + 1));
    }

    private void defaultHkjJewelryImageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjJewelryImageShouldBeFound(shouldBeFound);
        defaultHkjJewelryImageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjJewelryImageShouldBeFound(String filter) throws Exception {
        restHkjJewelryImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjJewelryImage.getId().intValue())))
            .andExpect(jsonPath("$.[*].url").value(hasItem(DEFAULT_URL)))
            .andExpect(jsonPath("$.[*].isSearchImage").value(hasItem(DEFAULT_IS_SEARCH_IMAGE.booleanValue())))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].tags").value(hasItem(DEFAULT_TAGS)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restHkjJewelryImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjJewelryImageShouldNotBeFound(String filter) throws Exception {
        restHkjJewelryImageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjJewelryImageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjJewelryImage() throws Exception {
        // Get the hkjJewelryImage
        restHkjJewelryImageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjJewelryImage() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjJewelryImage
        HkjJewelryImage updatedHkjJewelryImage = hkjJewelryImageRepository.findById(hkjJewelryImage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjJewelryImage are not directly saved in db
        em.detach(updatedHkjJewelryImage);
        updatedHkjJewelryImage
            .url(UPDATED_URL)
            .isSearchImage(UPDATED_IS_SEARCH_IMAGE)
            .description(UPDATED_DESCRIPTION)
            .tags(UPDATED_TAGS)
            .isDeleted(UPDATED_IS_DELETED);
        HkjJewelryImageDTO hkjJewelryImageDTO = hkjJewelryImageMapper.toDto(updatedHkjJewelryImage);

        restHkjJewelryImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjJewelryImageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjJewelryImageDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjJewelryImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjJewelryImageToMatchAllProperties(updatedHkjJewelryImage);
    }

    @Test
    @Transactional
    void putNonExistingHkjJewelryImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjJewelryImage.setId(longCount.incrementAndGet());

        // Create the HkjJewelryImage
        HkjJewelryImageDTO hkjJewelryImageDTO = hkjJewelryImageMapper.toDto(hkjJewelryImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjJewelryImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjJewelryImageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjJewelryImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjJewelryImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjJewelryImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjJewelryImage.setId(longCount.incrementAndGet());

        // Create the HkjJewelryImage
        HkjJewelryImageDTO hkjJewelryImageDTO = hkjJewelryImageMapper.toDto(hkjJewelryImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjJewelryImageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjJewelryImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjJewelryImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjJewelryImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjJewelryImage.setId(longCount.incrementAndGet());

        // Create the HkjJewelryImage
        HkjJewelryImageDTO hkjJewelryImageDTO = hkjJewelryImageMapper.toDto(hkjJewelryImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjJewelryImageMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjJewelryImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjJewelryImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjJewelryImageWithPatch() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjJewelryImage using partial update
        HkjJewelryImage partialUpdatedHkjJewelryImage = new HkjJewelryImage();
        partialUpdatedHkjJewelryImage.setId(hkjJewelryImage.getId());

        partialUpdatedHkjJewelryImage.isDeleted(UPDATED_IS_DELETED);

        restHkjJewelryImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjJewelryImage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjJewelryImage))
            )
            .andExpect(status().isOk());

        // Validate the HkjJewelryImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjJewelryImageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHkjJewelryImage, hkjJewelryImage),
            getPersistedHkjJewelryImage(hkjJewelryImage)
        );
    }

    @Test
    @Transactional
    void fullUpdateHkjJewelryImageWithPatch() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjJewelryImage using partial update
        HkjJewelryImage partialUpdatedHkjJewelryImage = new HkjJewelryImage();
        partialUpdatedHkjJewelryImage.setId(hkjJewelryImage.getId());

        partialUpdatedHkjJewelryImage
            .url(UPDATED_URL)
            .isSearchImage(UPDATED_IS_SEARCH_IMAGE)
            .description(UPDATED_DESCRIPTION)
            .tags(UPDATED_TAGS)
            .isDeleted(UPDATED_IS_DELETED);

        restHkjJewelryImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjJewelryImage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjJewelryImage))
            )
            .andExpect(status().isOk());

        // Validate the HkjJewelryImage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjJewelryImageUpdatableFieldsEquals(
            partialUpdatedHkjJewelryImage,
            getPersistedHkjJewelryImage(partialUpdatedHkjJewelryImage)
        );
    }

    @Test
    @Transactional
    void patchNonExistingHkjJewelryImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjJewelryImage.setId(longCount.incrementAndGet());

        // Create the HkjJewelryImage
        HkjJewelryImageDTO hkjJewelryImageDTO = hkjJewelryImageMapper.toDto(hkjJewelryImage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjJewelryImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjJewelryImageDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjJewelryImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjJewelryImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjJewelryImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjJewelryImage.setId(longCount.incrementAndGet());

        // Create the HkjJewelryImage
        HkjJewelryImageDTO hkjJewelryImageDTO = hkjJewelryImageMapper.toDto(hkjJewelryImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjJewelryImageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjJewelryImageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjJewelryImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjJewelryImage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjJewelryImage.setId(longCount.incrementAndGet());

        // Create the HkjJewelryImage
        HkjJewelryImageDTO hkjJewelryImageDTO = hkjJewelryImageMapper.toDto(hkjJewelryImage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjJewelryImageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjJewelryImageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjJewelryImage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjJewelryImage() throws Exception {
        // Initialize the database
        insertedHkjJewelryImage = hkjJewelryImageRepository.saveAndFlush(hkjJewelryImage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjJewelryImage
        restHkjJewelryImageMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjJewelryImage.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjJewelryImageRepository.count();
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

    protected HkjJewelryImage getPersistedHkjJewelryImage(HkjJewelryImage hkjJewelryImage) {
        return hkjJewelryImageRepository.findById(hkjJewelryImage.getId()).orElseThrow();
    }

    protected void assertPersistedHkjJewelryImageToMatchAllProperties(HkjJewelryImage expectedHkjJewelryImage) {
        assertHkjJewelryImageAllPropertiesEquals(expectedHkjJewelryImage, getPersistedHkjJewelryImage(expectedHkjJewelryImage));
    }

    protected void assertPersistedHkjJewelryImageToMatchUpdatableProperties(HkjJewelryImage expectedHkjJewelryImage) {
        assertHkjJewelryImageAllUpdatablePropertiesEquals(expectedHkjJewelryImage, getPersistedHkjJewelryImage(expectedHkjJewelryImage));
    }
}
