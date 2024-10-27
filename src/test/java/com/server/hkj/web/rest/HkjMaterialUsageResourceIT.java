package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjMaterialUsageAsserts.*;
import static com.server.hkj.web.rest.TestUtil.createUpdateProxyForBean;
import static com.server.hkj.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.hkj.IntegrationTest;
import com.server.hkj.domain.HkjMaterial;
import com.server.hkj.domain.HkjMaterialUsage;
import com.server.hkj.domain.HkjTask;
import com.server.hkj.repository.HkjMaterialUsageRepository;
import com.server.hkj.service.dto.HkjMaterialUsageDTO;
import com.server.hkj.service.mapper.HkjMaterialUsageMapper;
import jakarta.persistence.EntityManager;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
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
 * Integration tests for the {@link HkjMaterialUsageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjMaterialUsageResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final Integer DEFAULT_LOSS_QUANTITY = 1;
    private static final Integer UPDATED_LOSS_QUANTITY = 2;
    private static final Integer SMALLER_LOSS_QUANTITY = 1 - 1;

    private static final Instant DEFAULT_USAGE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_USAGE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Float DEFAULT_WEIGHT = 1F;
    private static final Float UPDATED_WEIGHT = 2F;
    private static final Float SMALLER_WEIGHT = 1F - 1F;

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/hkj-material-usages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjMaterialUsageRepository hkjMaterialUsageRepository;

    @Autowired
    private HkjMaterialUsageMapper hkjMaterialUsageMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjMaterialUsageMockMvc;

    private HkjMaterialUsage hkjMaterialUsage;

    private HkjMaterialUsage insertedHkjMaterialUsage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjMaterialUsage createEntity() {
        return new HkjMaterialUsage()
            .quantity(DEFAULT_QUANTITY)
            .lossQuantity(DEFAULT_LOSS_QUANTITY)
            .usageDate(DEFAULT_USAGE_DATE)
            .notes(DEFAULT_NOTES)
            .weight(DEFAULT_WEIGHT)
            .price(DEFAULT_PRICE)
            .isDeleted(DEFAULT_IS_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjMaterialUsage createUpdatedEntity() {
        return new HkjMaterialUsage()
            .quantity(UPDATED_QUANTITY)
            .lossQuantity(UPDATED_LOSS_QUANTITY)
            .usageDate(UPDATED_USAGE_DATE)
            .notes(UPDATED_NOTES)
            .weight(UPDATED_WEIGHT)
            .price(UPDATED_PRICE)
            .isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    public void initTest() {
        hkjMaterialUsage = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjMaterialUsage != null) {
            hkjMaterialUsageRepository.delete(insertedHkjMaterialUsage);
            insertedHkjMaterialUsage = null;
        }
    }

    @Test
    @Transactional
    void createHkjMaterialUsage() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjMaterialUsage
        HkjMaterialUsageDTO hkjMaterialUsageDTO = hkjMaterialUsageMapper.toDto(hkjMaterialUsage);
        var returnedHkjMaterialUsageDTO = om.readValue(
            restHkjMaterialUsageMockMvc
                .perform(
                    post(ENTITY_API_URL)
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(om.writeValueAsBytes(hkjMaterialUsageDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjMaterialUsageDTO.class
        );

        // Validate the HkjMaterialUsage in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjMaterialUsage = hkjMaterialUsageMapper.toEntity(returnedHkjMaterialUsageDTO);
        assertHkjMaterialUsageUpdatableFieldsEquals(returnedHkjMaterialUsage, getPersistedHkjMaterialUsage(returnedHkjMaterialUsage));

        insertedHkjMaterialUsage = returnedHkjMaterialUsage;
    }

    @Test
    @Transactional
    void createHkjMaterialUsageWithExistingId() throws Exception {
        // Create the HkjMaterialUsage with an existing ID
        hkjMaterialUsage.setId(1L);
        HkjMaterialUsageDTO hkjMaterialUsageDTO = hkjMaterialUsageMapper.toDto(hkjMaterialUsage);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjMaterialUsageMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjMaterialUsageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjMaterialUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjMaterialUsage.setQuantity(null);

        // Create the HkjMaterialUsage, which fails.
        HkjMaterialUsageDTO hkjMaterialUsageDTO = hkjMaterialUsageMapper.toDto(hkjMaterialUsage);

        restHkjMaterialUsageMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjMaterialUsageDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkUsageDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjMaterialUsage.setUsageDate(null);

        // Create the HkjMaterialUsage, which fails.
        HkjMaterialUsageDTO hkjMaterialUsageDTO = hkjMaterialUsageMapper.toDto(hkjMaterialUsage);

        restHkjMaterialUsageMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjMaterialUsageDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsages() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList
        restHkjMaterialUsageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjMaterialUsage.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].lossQuantity").value(hasItem(DEFAULT_LOSS_QUANTITY)))
            .andExpect(jsonPath("$.[*].usageDate").value(hasItem(DEFAULT_USAGE_DATE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getHkjMaterialUsage() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get the hkjMaterialUsage
        restHkjMaterialUsageMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjMaterialUsage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjMaterialUsage.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.lossQuantity").value(DEFAULT_LOSS_QUANTITY))
            .andExpect(jsonPath("$.usageDate").value(DEFAULT_USAGE_DATE.toString()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.weight").value(DEFAULT_WEIGHT.doubleValue()))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getHkjMaterialUsagesByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        Long id = hkjMaterialUsage.getId();

        defaultHkjMaterialUsageFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjMaterialUsageFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjMaterialUsageFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where quantity equals to
        defaultHkjMaterialUsageFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where quantity in
        defaultHkjMaterialUsageFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where quantity is not null
        defaultHkjMaterialUsageFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where quantity is greater than or equal to
        defaultHkjMaterialUsageFiltering(
            "quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY,
            "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where quantity is less than or equal to
        defaultHkjMaterialUsageFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where quantity is less than
        defaultHkjMaterialUsageFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where quantity is greater than
        defaultHkjMaterialUsageFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByLossQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where lossQuantity equals to
        defaultHkjMaterialUsageFiltering("lossQuantity.equals=" + DEFAULT_LOSS_QUANTITY, "lossQuantity.equals=" + UPDATED_LOSS_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByLossQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where lossQuantity in
        defaultHkjMaterialUsageFiltering(
            "lossQuantity.in=" + DEFAULT_LOSS_QUANTITY + "," + UPDATED_LOSS_QUANTITY,
            "lossQuantity.in=" + UPDATED_LOSS_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByLossQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where lossQuantity is not null
        defaultHkjMaterialUsageFiltering("lossQuantity.specified=true", "lossQuantity.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByLossQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where lossQuantity is greater than or equal to
        defaultHkjMaterialUsageFiltering(
            "lossQuantity.greaterThanOrEqual=" + DEFAULT_LOSS_QUANTITY,
            "lossQuantity.greaterThanOrEqual=" + UPDATED_LOSS_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByLossQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where lossQuantity is less than or equal to
        defaultHkjMaterialUsageFiltering(
            "lossQuantity.lessThanOrEqual=" + DEFAULT_LOSS_QUANTITY,
            "lossQuantity.lessThanOrEqual=" + SMALLER_LOSS_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByLossQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where lossQuantity is less than
        defaultHkjMaterialUsageFiltering(
            "lossQuantity.lessThan=" + UPDATED_LOSS_QUANTITY,
            "lossQuantity.lessThan=" + DEFAULT_LOSS_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByLossQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where lossQuantity is greater than
        defaultHkjMaterialUsageFiltering(
            "lossQuantity.greaterThan=" + SMALLER_LOSS_QUANTITY,
            "lossQuantity.greaterThan=" + DEFAULT_LOSS_QUANTITY
        );
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByUsageDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where usageDate equals to
        defaultHkjMaterialUsageFiltering("usageDate.equals=" + DEFAULT_USAGE_DATE, "usageDate.equals=" + UPDATED_USAGE_DATE);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByUsageDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where usageDate in
        defaultHkjMaterialUsageFiltering(
            "usageDate.in=" + DEFAULT_USAGE_DATE + "," + UPDATED_USAGE_DATE,
            "usageDate.in=" + UPDATED_USAGE_DATE
        );
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByUsageDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where usageDate is not null
        defaultHkjMaterialUsageFiltering("usageDate.specified=true", "usageDate.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where notes equals to
        defaultHkjMaterialUsageFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where notes in
        defaultHkjMaterialUsageFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where notes is not null
        defaultHkjMaterialUsageFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where notes contains
        defaultHkjMaterialUsageFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where notes does not contain
        defaultHkjMaterialUsageFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByWeightIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where weight equals to
        defaultHkjMaterialUsageFiltering("weight.equals=" + DEFAULT_WEIGHT, "weight.equals=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByWeightIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where weight in
        defaultHkjMaterialUsageFiltering("weight.in=" + DEFAULT_WEIGHT + "," + UPDATED_WEIGHT, "weight.in=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByWeightIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where weight is not null
        defaultHkjMaterialUsageFiltering("weight.specified=true", "weight.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByWeightIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where weight is greater than or equal to
        defaultHkjMaterialUsageFiltering("weight.greaterThanOrEqual=" + DEFAULT_WEIGHT, "weight.greaterThanOrEqual=" + UPDATED_WEIGHT);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByWeightIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where weight is less than or equal to
        defaultHkjMaterialUsageFiltering("weight.lessThanOrEqual=" + DEFAULT_WEIGHT, "weight.lessThanOrEqual=" + SMALLER_WEIGHT);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByWeightIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where weight is less than
        defaultHkjMaterialUsageFiltering("weight.lessThan=" + UPDATED_WEIGHT, "weight.lessThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByWeightIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where weight is greater than
        defaultHkjMaterialUsageFiltering("weight.greaterThan=" + SMALLER_WEIGHT, "weight.greaterThan=" + DEFAULT_WEIGHT);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where price equals to
        defaultHkjMaterialUsageFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where price in
        defaultHkjMaterialUsageFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where price is not null
        defaultHkjMaterialUsageFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where price is greater than or equal to
        defaultHkjMaterialUsageFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where price is less than or equal to
        defaultHkjMaterialUsageFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where price is less than
        defaultHkjMaterialUsageFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where price is greater than
        defaultHkjMaterialUsageFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where isDeleted equals to
        defaultHkjMaterialUsageFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where isDeleted in
        defaultHkjMaterialUsageFiltering(
            "isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED,
            "isDeleted.in=" + UPDATED_IS_DELETED
        );
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where isDeleted is not null
        defaultHkjMaterialUsageFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByMaterialIsEqualToSomething() throws Exception {
        HkjMaterial material;
        if (TestUtil.findAll(em, HkjMaterial.class).isEmpty()) {
            hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);
            material = HkjMaterialResourceIT.createEntity();
        } else {
            material = TestUtil.findAll(em, HkjMaterial.class).get(0);
        }
        em.persist(material);
        em.flush();
        hkjMaterialUsage.setMaterial(material);
        hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);
        Long materialId = material.getId();
        // Get all the hkjMaterialUsageList where material equals to materialId
        defaultHkjMaterialUsageShouldBeFound("materialId.equals=" + materialId);

        // Get all the hkjMaterialUsageList where material equals to (materialId + 1)
        defaultHkjMaterialUsageShouldNotBeFound("materialId.equals=" + (materialId + 1));
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByTaskIsEqualToSomething() throws Exception {
        HkjTask task;
        if (TestUtil.findAll(em, HkjTask.class).isEmpty()) {
            hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);
            task = HkjTaskResourceIT.createEntity();
        } else {
            task = TestUtil.findAll(em, HkjTask.class).get(0);
        }
        em.persist(task);
        em.flush();
        hkjMaterialUsage.setTask(task);
        hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);
        Long taskId = task.getId();
        // Get all the hkjMaterialUsageList where task equals to taskId
        defaultHkjMaterialUsageShouldBeFound("taskId.equals=" + taskId);

        // Get all the hkjMaterialUsageList where task equals to (taskId + 1)
        defaultHkjMaterialUsageShouldNotBeFound("taskId.equals=" + (taskId + 1));
    }

    private void defaultHkjMaterialUsageFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjMaterialUsageShouldBeFound(shouldBeFound);
        defaultHkjMaterialUsageShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjMaterialUsageShouldBeFound(String filter) throws Exception {
        restHkjMaterialUsageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjMaterialUsage.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].lossQuantity").value(hasItem(DEFAULT_LOSS_QUANTITY)))
            .andExpect(jsonPath("$.[*].usageDate").value(hasItem(DEFAULT_USAGE_DATE.toString())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].weight").value(hasItem(DEFAULT_WEIGHT.doubleValue())))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restHkjMaterialUsageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjMaterialUsageShouldNotBeFound(String filter) throws Exception {
        restHkjMaterialUsageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjMaterialUsageMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjMaterialUsage() throws Exception {
        // Get the hkjMaterialUsage
        restHkjMaterialUsageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjMaterialUsage() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjMaterialUsage
        HkjMaterialUsage updatedHkjMaterialUsage = hkjMaterialUsageRepository.findById(hkjMaterialUsage.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjMaterialUsage are not directly saved in db
        em.detach(updatedHkjMaterialUsage);
        updatedHkjMaterialUsage
            .quantity(UPDATED_QUANTITY)
            .lossQuantity(UPDATED_LOSS_QUANTITY)
            .usageDate(UPDATED_USAGE_DATE)
            .notes(UPDATED_NOTES)
            .weight(UPDATED_WEIGHT)
            .price(UPDATED_PRICE)
            .isDeleted(UPDATED_IS_DELETED);
        HkjMaterialUsageDTO hkjMaterialUsageDTO = hkjMaterialUsageMapper.toDto(updatedHkjMaterialUsage);

        restHkjMaterialUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjMaterialUsageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjMaterialUsageDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjMaterialUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjMaterialUsageToMatchAllProperties(updatedHkjMaterialUsage);
    }

    @Test
    @Transactional
    void putNonExistingHkjMaterialUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjMaterialUsage.setId(longCount.incrementAndGet());

        // Create the HkjMaterialUsage
        HkjMaterialUsageDTO hkjMaterialUsageDTO = hkjMaterialUsageMapper.toDto(hkjMaterialUsage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjMaterialUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjMaterialUsageDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjMaterialUsageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjMaterialUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjMaterialUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjMaterialUsage.setId(longCount.incrementAndGet());

        // Create the HkjMaterialUsage
        HkjMaterialUsageDTO hkjMaterialUsageDTO = hkjMaterialUsageMapper.toDto(hkjMaterialUsage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjMaterialUsageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjMaterialUsageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjMaterialUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjMaterialUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjMaterialUsage.setId(longCount.incrementAndGet());

        // Create the HkjMaterialUsage
        HkjMaterialUsageDTO hkjMaterialUsageDTO = hkjMaterialUsageMapper.toDto(hkjMaterialUsage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjMaterialUsageMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjMaterialUsageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjMaterialUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjMaterialUsageWithPatch() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjMaterialUsage using partial update
        HkjMaterialUsage partialUpdatedHkjMaterialUsage = new HkjMaterialUsage();
        partialUpdatedHkjMaterialUsage.setId(hkjMaterialUsage.getId());

        partialUpdatedHkjMaterialUsage.lossQuantity(UPDATED_LOSS_QUANTITY).notes(UPDATED_NOTES);

        restHkjMaterialUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjMaterialUsage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjMaterialUsage))
            )
            .andExpect(status().isOk());

        // Validate the HkjMaterialUsage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjMaterialUsageUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHkjMaterialUsage, hkjMaterialUsage),
            getPersistedHkjMaterialUsage(hkjMaterialUsage)
        );
    }

    @Test
    @Transactional
    void fullUpdateHkjMaterialUsageWithPatch() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjMaterialUsage using partial update
        HkjMaterialUsage partialUpdatedHkjMaterialUsage = new HkjMaterialUsage();
        partialUpdatedHkjMaterialUsage.setId(hkjMaterialUsage.getId());

        partialUpdatedHkjMaterialUsage
            .quantity(UPDATED_QUANTITY)
            .lossQuantity(UPDATED_LOSS_QUANTITY)
            .usageDate(UPDATED_USAGE_DATE)
            .notes(UPDATED_NOTES)
            .weight(UPDATED_WEIGHT)
            .price(UPDATED_PRICE)
            .isDeleted(UPDATED_IS_DELETED);

        restHkjMaterialUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjMaterialUsage.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjMaterialUsage))
            )
            .andExpect(status().isOk());

        // Validate the HkjMaterialUsage in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjMaterialUsageUpdatableFieldsEquals(
            partialUpdatedHkjMaterialUsage,
            getPersistedHkjMaterialUsage(partialUpdatedHkjMaterialUsage)
        );
    }

    @Test
    @Transactional
    void patchNonExistingHkjMaterialUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjMaterialUsage.setId(longCount.incrementAndGet());

        // Create the HkjMaterialUsage
        HkjMaterialUsageDTO hkjMaterialUsageDTO = hkjMaterialUsageMapper.toDto(hkjMaterialUsage);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjMaterialUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjMaterialUsageDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjMaterialUsageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjMaterialUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjMaterialUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjMaterialUsage.setId(longCount.incrementAndGet());

        // Create the HkjMaterialUsage
        HkjMaterialUsageDTO hkjMaterialUsageDTO = hkjMaterialUsageMapper.toDto(hkjMaterialUsage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjMaterialUsageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjMaterialUsageDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjMaterialUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjMaterialUsage() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjMaterialUsage.setId(longCount.incrementAndGet());

        // Create the HkjMaterialUsage
        HkjMaterialUsageDTO hkjMaterialUsageDTO = hkjMaterialUsageMapper.toDto(hkjMaterialUsage);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjMaterialUsageMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjMaterialUsageDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjMaterialUsage in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjMaterialUsage() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjMaterialUsage
        restHkjMaterialUsageMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjMaterialUsage.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjMaterialUsageRepository.count();
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

    protected HkjMaterialUsage getPersistedHkjMaterialUsage(HkjMaterialUsage hkjMaterialUsage) {
        return hkjMaterialUsageRepository.findById(hkjMaterialUsage.getId()).orElseThrow();
    }

    protected void assertPersistedHkjMaterialUsageToMatchAllProperties(HkjMaterialUsage expectedHkjMaterialUsage) {
        assertHkjMaterialUsageAllPropertiesEquals(expectedHkjMaterialUsage, getPersistedHkjMaterialUsage(expectedHkjMaterialUsage));
    }

    protected void assertPersistedHkjMaterialUsageToMatchUpdatableProperties(HkjMaterialUsage expectedHkjMaterialUsage) {
        assertHkjMaterialUsageAllUpdatablePropertiesEquals(
            expectedHkjMaterialUsage,
            getPersistedHkjMaterialUsage(expectedHkjMaterialUsage)
        );
    }
}
