package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjJewelryModelAsserts.*;
import static com.server.hkj.web.rest.TestUtil.createUpdateProxyForBean;
import static com.server.hkj.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.hkj.IntegrationTest;
import com.server.hkj.domain.HkjJewelryModel;
import com.server.hkj.domain.HkjProject;
import com.server.hkj.repository.HkjJewelryModelRepository;
import com.server.hkj.service.dto.HkjJewelryModelDTO;
import com.server.hkj.service.mapper.HkjJewelryModelMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
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
 * Integration tests for the {@link HkjJewelryModelResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjJewelryModelResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final String DEFAULT_COVER_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_COVER_IMAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_CUSTOM = false;
    private static final Boolean UPDATED_IS_CUSTOM = true;

    private static final Double DEFAULT_WEIGHT = 1D;
    private static final Double UPDATED_WEIGHT = 2D;
    private static final Double SMALLER_WEIGHT = 1D - 1D;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(1 - 1);

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final Boolean DEFAULT_ACTIVE = false;
    private static final Boolean UPDATED_ACTIVE = true;

    private static final String ENTITY_API_URL = "/api/hkj-jewelry-models";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjJewelryModelRepository hkjJewelryModelRepository;

    @Autowired
    private HkjJewelryModelMapper hkjJewelryModelMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjJewelryModelMockMvc;

    private HkjJewelryModel hkjJewelryModel;

    private HkjJewelryModel insertedHkjJewelryModel;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjJewelryModel createEntity() {
        return new HkjJewelryModel()
            .name(DEFAULT_NAME)
            .description(DEFAULT_DESCRIPTION)
            .coverImage(DEFAULT_COVER_IMAGE)
            .isCustom(DEFAULT_IS_CUSTOM)
            .weight(DEFAULT_WEIGHT)
            .price(DEFAULT_PRICE)
            .color(DEFAULT_COLOR)
            .notes(DEFAULT_NOTES)
            .isDeleted(DEFAULT_IS_DELETED)
            .active(DEFAULT_ACTIVE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjJewelryModel createUpdatedEntity() {
        return new HkjJewelryModel()
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .coverImage(UPDATED_COVER_IMAGE)
            .isCustom(UPDATED_IS_CUSTOM)
            .weight(UPDATED_WEIGHT)
            .price(UPDATED_PRICE)
            .color(UPDATED_COLOR)
            .notes(UPDATED_NOTES)
            .isDeleted(UPDATED_IS_DELETED)
            .active(UPDATED_ACTIVE);
    }

    @BeforeEach
    public void initTest() {
        hkjJewelryModel = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjJewelryModel != null) {
            hkjJewelryModelRepository.delete(insertedHkjJewelryModel);
            insertedHkjJewelryModel = null;
        }
    }

    @Test
    @Transactional
    void createHkjJewelryModel() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjJewelryModel
        HkjJewelryModelDTO hkjJewelryModelDTO = hkjJewelryModelMapper.toDto(hkjJewelryModel);
        var returnedHkjJewelryModelDTO = om.readValue(
            restHkjJewelryModelMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(hkjJewelryModelDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjJewelryModelDTO.class
        );

        // Validate the HkjJewelryModel in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjJewelryModel = hkjJewelryModelMapper.toEntity(returnedHkjJewelryModelDTO);
        assertHkjJewelryModelUpdatableFieldsEquals(returnedHkjJewelryModel, getPersistedHkjJewelryModel(returnedHkjJewelryModel));

        insertedHkjJewelryModel = returnedHkjJewelryModel;
    }

    @Test
    @Transactional
    void createHkjJewelryModelWithExistingId() throws Exception {
        // Create the HkjJewelryModel with an existing ID
        hkjJewelryModel.setId(1L);
        HkjJewelryModelDTO hkjJewelryModelDTO = hkjJewelryModelMapper.toDto(hkjJewelryModel);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjJewelryModelMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjJewelryModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjJewelryModel in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjJewelryModel.setName(null);

        // Create the HkjJewelryModel, which fails.
        HkjJewelryModelDTO hkjJewelryModelDTO = hkjJewelryModelMapper.toDto(hkjJewelryModel);

        restHkjJewelryModelMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjJewelryModelDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModels() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList
        restHkjJewelryModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjJewelryModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].coverImage").value(hasItem(DEFAULT_COVER_IMAGE)))
            .andExpect(jsonPath("$.[*].isCustom").value(hasItem(DEFAULT_IS_CUSTOM.booleanValue())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));
    }

    @Test
    @Transactional
    void getHkjJewelryModel() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get the hkjJewelryModel
        restHkjJewelryModelMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjJewelryModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjJewelryModel.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.coverImage").value(DEFAULT_COVER_IMAGE))
            .andExpect(jsonPath("$.isCustom").value(DEFAULT_IS_CUSTOM.booleanValue()))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.doubleValue()))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.active").value(DEFAULT_ACTIVE.booleanValue()));
    }

    @Test
    @Transactional
    void getHkjJewelryModelsByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        Long id = hkjJewelryModel.getId();

        defaultHkjJewelryModelFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjJewelryModelFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjJewelryModelFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where name equals to
        defaultHkjJewelryModelFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where name in
        defaultHkjJewelryModelFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where name is not null
        defaultHkjJewelryModelFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where name contains
        defaultHkjJewelryModelFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where name does not contain
        defaultHkjJewelryModelFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where description equals to
        defaultHkjJewelryModelFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where description in
        defaultHkjJewelryModelFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where description is not null
        defaultHkjJewelryModelFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where description contains
        defaultHkjJewelryModelFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where description does not contain
        defaultHkjJewelryModelFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByCoverImageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where coverImage equals to
        defaultHkjJewelryModelFiltering("coverImage.equals=" + DEFAULT_COVER_IMAGE, "coverImage.equals=" + UPDATED_COVER_IMAGE);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByCoverImageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where coverImage in
        defaultHkjJewelryModelFiltering(
            "coverImage.in=" + DEFAULT_COVER_IMAGE + "," + UPDATED_COVER_IMAGE,
            "coverImage.in=" + UPDATED_COVER_IMAGE
        );
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByCoverImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where coverImage is not null
        defaultHkjJewelryModelFiltering("coverImage.specified=true", "coverImage.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByCoverImageContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where coverImage contains
        defaultHkjJewelryModelFiltering("coverImage.contains=" + DEFAULT_COVER_IMAGE, "coverImage.contains=" + UPDATED_COVER_IMAGE);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByCoverImageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where coverImage does not contain
        defaultHkjJewelryModelFiltering(
            "coverImage.doesNotContain=" + UPDATED_COVER_IMAGE,
            "coverImage.doesNotContain=" + DEFAULT_COVER_IMAGE
        );
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByIsCustomIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where isCustom equals to
        defaultHkjJewelryModelFiltering("isCustom.equals=" + DEFAULT_IS_CUSTOM, "isCustom.equals=" + UPDATED_IS_CUSTOM);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByIsCustomIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where isCustom in
        defaultHkjJewelryModelFiltering("isCustom.in=" + DEFAULT_IS_CUSTOM + "," + UPDATED_IS_CUSTOM, "isCustom.in=" + UPDATED_IS_CUSTOM);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByIsCustomIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where isCustom is not null
        defaultHkjJewelryModelFiltering("isCustom.specified=true", "isCustom.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where weight equals to
        defaultHkjJewelryModelFiltering("weight.equals=" + DEFAULT_WEIGHT, "weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where weight in
        defaultHkjJewelryModelFiltering("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT, "weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where weight is not null
        defaultHkjJewelryModelFiltering("weight.specified=true", "weight.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where weight is greater than or equal to
        defaultHkjJewelryModelFiltering("weight.greaterThanOrEqual=" + DEFAULT_WEIGHT, "weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where weight is less than or equal to
        defaultHkjJewelryModelFiltering("weight.lessThanOrEqual=" + DEFAULT_WEIGHT, "weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where weight is less than
        defaultHkjJewelryModelFiltering("weight.lessThan=" + UPDATED_WEIGHT, "weight.lessThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where weight is greater than
        defaultHkjJewelryModelFiltering("weight.greaterThan=" + SMALLER_WEIGHT, "weight.greaterThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where price equals to
        defaultHkjJewelryModelFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where price in
        defaultHkjJewelryModelFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where price is not null
        defaultHkjJewelryModelFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where price is greater than or equal to
        defaultHkjJewelryModelFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where price is less than or equal to
        defaultHkjJewelryModelFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where price is less than
        defaultHkjJewelryModelFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where price is greater than
        defaultHkjJewelryModelFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByColorIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where color equals to
        defaultHkjJewelryModelFiltering("color.equals=" + DEFAULT_COLOR, "color.equals=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByColorIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where color in
        defaultHkjJewelryModelFiltering("color.in=" + DEFAULT_COLOR + "," + UPDATED_COLOR, "color.in=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByColorIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where color is not null
        defaultHkjJewelryModelFiltering("color.specified=true", "color.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByColorContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where color contains
        defaultHkjJewelryModelFiltering("color.contains=" + DEFAULT_COLOR, "color.contains=" + UPDATED_COLOR);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByColorNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where color does not contain
        defaultHkjJewelryModelFiltering("color.doesNotContain=" + UPDATED_COLOR, "color.doesNotContain=" + DEFAULT_COLOR);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where notes equals to
        defaultHkjJewelryModelFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where notes in
        defaultHkjJewelryModelFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where notes is not null
        defaultHkjJewelryModelFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where notes contains
        defaultHkjJewelryModelFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where notes does not contain
        defaultHkjJewelryModelFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where isDeleted equals to
        defaultHkjJewelryModelFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where isDeleted in
        defaultHkjJewelryModelFiltering(
            "isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED,
            "isDeleted.in=" + UPDATED_IS_DELETED
        );
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where isDeleted is not null
        defaultHkjJewelryModelFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByActiveIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where active equals to
        defaultHkjJewelryModelFiltering("active.equals=" + DEFAULT_ACTIVE, "active.equals=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByActiveIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where active in
        defaultHkjJewelryModelFiltering("active.in=" + DEFAULT_ACTIVE + "," + UPDATED_ACTIVE, "active.in=" + UPDATED_ACTIVE);
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByActiveIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        // Get all the hkjJewelryModelList where active is not null
        defaultHkjJewelryModelFiltering("active.specified=true", "active.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjJewelryModelsByProjectIsEqualToSomething() throws Exception {
        HkjProject project;
        if (TestUtil.findAll(em, HkjProject.class).isEmpty()) {
            hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);
            project = HkjProjectResourceIT.createEntity();
        } else {
            project = TestUtil.findAll(em, HkjProject.class).get(0);
        }
        em.persist(project);
        em.flush();
        hkjJewelryModel.setProject(project);
        hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);
        Long projectId = project.getId();
        // Get all the hkjJewelryModelList where project equals to projectId
        defaultHkjJewelryModelShouldBeFound("projectId.equals=" + projectId);

        // Get all the hkjJewelryModelList where project equals to (projectId + 1)
        defaultHkjJewelryModelShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    private void defaultHkjJewelryModelFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjJewelryModelShouldBeFound(shouldBeFound);
        defaultHkjJewelryModelShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjJewelryModelShouldBeFound(String filter) throws Exception {
        restHkjJewelryModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjJewelryModel.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].coverImage").value(hasItem(DEFAULT_COVER_IMAGE)))
            .andExpect(jsonPath("$.[*].isCustom").value(hasItem(DEFAULT_IS_CUSTOM.booleanValue())))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].active").value(hasItem(DEFAULT_ACTIVE.booleanValue())));

        // Check, that the count call also returns 1
        restHkjJewelryModelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjJewelryModelShouldNotBeFound(String filter) throws Exception {
        restHkjJewelryModelMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjJewelryModelMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjJewelryModel() throws Exception {
        // Get the hkjJewelryModel
        restHkjJewelryModelMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjJewelryModel() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjJewelryModel
        HkjJewelryModel updatedHkjJewelryModel = hkjJewelryModelRepository.findById(hkjJewelryModel.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjJewelryModel are not directly saved in db
        em.detach(updatedHkjJewelryModel);
        updatedHkjJewelryModel
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .coverImage(UPDATED_COVER_IMAGE)
            .isCustom(UPDATED_IS_CUSTOM)
            .weight(UPDATED_WEIGHT)
            .price(UPDATED_PRICE)
            .color(UPDATED_COLOR)
            .notes(UPDATED_NOTES)
            .isDeleted(UPDATED_IS_DELETED)
            .active(UPDATED_ACTIVE);
        HkjJewelryModelDTO hkjJewelryModelDTO = hkjJewelryModelMapper.toDto(updatedHkjJewelryModel);

        restHkjJewelryModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjJewelryModelDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjJewelryModelDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjJewelryModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjJewelryModelToMatchAllProperties(updatedHkjJewelryModel);
    }

    @Test
    @Transactional
    void putNonExistingHkjJewelryModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjJewelryModel.setId(longCount.incrementAndGet());

        // Create the HkjJewelryModel
        HkjJewelryModelDTO hkjJewelryModelDTO = hkjJewelryModelMapper.toDto(hkjJewelryModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjJewelryModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjJewelryModelDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjJewelryModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjJewelryModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjJewelryModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjJewelryModel.setId(longCount.incrementAndGet());

        // Create the HkjJewelryModel
        HkjJewelryModelDTO hkjJewelryModelDTO = hkjJewelryModelMapper.toDto(hkjJewelryModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjJewelryModelMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjJewelryModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjJewelryModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjJewelryModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjJewelryModel.setId(longCount.incrementAndGet());

        // Create the HkjJewelryModel
        HkjJewelryModelDTO hkjJewelryModelDTO = hkjJewelryModelMapper.toDto(hkjJewelryModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjJewelryModelMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjJewelryModelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjJewelryModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjJewelryModelWithPatch() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjJewelryModel using partial update
        HkjJewelryModel partialUpdatedHkjJewelryModel = new HkjJewelryModel();
        partialUpdatedHkjJewelryModel.setId(hkjJewelryModel.getId());

        partialUpdatedHkjJewelryModel
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .coverImage(UPDATED_COVER_IMAGE)
            .isCustom(UPDATED_IS_CUSTOM)
            .price(UPDATED_PRICE)
            .notes(UPDATED_NOTES)
            .isDeleted(UPDATED_IS_DELETED)
            .active(UPDATED_ACTIVE);

        restHkjJewelryModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjJewelryModel.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjJewelryModel))
            )
            .andExpect(status().isOk());

        // Validate the HkjJewelryModel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjJewelryModelUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHkjJewelryModel, hkjJewelryModel),
            getPersistedHkjJewelryModel(hkjJewelryModel)
        );
    }

    @Test
    @Transactional
    void fullUpdateHkjJewelryModelWithPatch() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjJewelryModel using partial update
        HkjJewelryModel partialUpdatedHkjJewelryModel = new HkjJewelryModel();
        partialUpdatedHkjJewelryModel.setId(hkjJewelryModel.getId());

        partialUpdatedHkjJewelryModel
            .name(UPDATED_NAME)
            .description(UPDATED_DESCRIPTION)
            .coverImage(UPDATED_COVER_IMAGE)
            .isCustom(UPDATED_IS_CUSTOM)
            .weight(UPDATED_WEIGHT)
            .price(UPDATED_PRICE)
            .color(UPDATED_COLOR)
            .notes(UPDATED_NOTES)
            .isDeleted(UPDATED_IS_DELETED)
            .active(UPDATED_ACTIVE);

        restHkjJewelryModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjJewelryModel.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjJewelryModel))
            )
            .andExpect(status().isOk());

        // Validate the HkjJewelryModel in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjJewelryModelUpdatableFieldsEquals(
            partialUpdatedHkjJewelryModel,
            getPersistedHkjJewelryModel(partialUpdatedHkjJewelryModel)
        );
    }

    @Test
    @Transactional
    void patchNonExistingHkjJewelryModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjJewelryModel.setId(longCount.incrementAndGet());

        // Create the HkjJewelryModel
        HkjJewelryModelDTO hkjJewelryModelDTO = hkjJewelryModelMapper.toDto(hkjJewelryModel);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjJewelryModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjJewelryModelDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjJewelryModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjJewelryModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjJewelryModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjJewelryModel.setId(longCount.incrementAndGet());

        // Create the HkjJewelryModel
        HkjJewelryModelDTO hkjJewelryModelDTO = hkjJewelryModelMapper.toDto(hkjJewelryModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjJewelryModelMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjJewelryModelDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjJewelryModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjJewelryModel() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjJewelryModel.setId(longCount.incrementAndGet());

        // Create the HkjJewelryModel
        HkjJewelryModelDTO hkjJewelryModelDTO = hkjJewelryModelMapper.toDto(hkjJewelryModel);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjJewelryModelMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjJewelryModelDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjJewelryModel in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjJewelryModel() throws Exception {
        // Initialize the database
        insertedHkjJewelryModel = hkjJewelryModelRepository.saveAndFlush(hkjJewelryModel);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjJewelryModel
        restHkjJewelryModelMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjJewelryModel.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjJewelryModelRepository.count();
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

    protected HkjJewelryModel getPersistedHkjJewelryModel(HkjJewelryModel hkjJewelryModel) {
        return hkjJewelryModelRepository.findById(hkjJewelryModel.getId()).orElseThrow();
    }

    protected void assertPersistedHkjJewelryModelToMatchAllProperties(HkjJewelryModel expectedHkjJewelryModel) {
        assertHkjJewelryModelAllPropertiesEquals(expectedHkjJewelryModel, getPersistedHkjJewelryModel(expectedHkjJewelryModel));
    }

    protected void assertPersistedHkjJewelryModelToMatchUpdatableProperties(HkjJewelryModel expectedHkjJewelryModel) {
        assertHkjJewelryModelAllUpdatablePropertiesEquals(expectedHkjJewelryModel, getPersistedHkjJewelryModel(expectedHkjJewelryModel));
    }
}
