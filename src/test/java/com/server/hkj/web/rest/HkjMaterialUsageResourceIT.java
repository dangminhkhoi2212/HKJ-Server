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
import com.server.hkj.domain.HkjJewelryModel;
import com.server.hkj.domain.HkjMaterial;
import com.server.hkj.domain.HkjMaterialUsage;
import com.server.hkj.domain.HkjTask;
import com.server.hkj.repository.HkjMaterialUsageRepository;
import com.server.hkj.service.dto.HkjMaterialUsageDTO;
import com.server.hkj.service.mapper.HkjMaterialUsageMapper;
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
 * Integration tests for the {@link HkjMaterialUsageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjMaterialUsageResourceIT {

    private static final Integer DEFAULT_USAGE = 1;
    private static final Integer UPDATED_USAGE = 2;
    private static final Integer SMALLER_USAGE = 1 - 1;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

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
        return new HkjMaterialUsage().usage(DEFAULT_USAGE).notes(DEFAULT_NOTES).price(DEFAULT_PRICE).isDeleted(DEFAULT_IS_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjMaterialUsage createUpdatedEntity() {
        return new HkjMaterialUsage().usage(UPDATED_USAGE).notes(UPDATED_NOTES).price(UPDATED_PRICE).isDeleted(UPDATED_IS_DELETED);
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
    void getAllHkjMaterialUsages() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList
        restHkjMaterialUsageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjMaterialUsage.getId().intValue())))
            .andExpect(jsonPath("$.[*].usage").value(hasItem(DEFAULT_USAGE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
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
            .andExpect(jsonPath("$.usage").value(DEFAULT_USAGE))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
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
    void getAllHkjMaterialUsagesByUsageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where usage equals to
        defaultHkjMaterialUsageFiltering("usage.equals=" + DEFAULT_USAGE, "usage.equals=" + UPDATED_USAGE);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByUsageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where usage in
        defaultHkjMaterialUsageFiltering("usage.in=" + DEFAULT_USAGE + "," + UPDATED_USAGE, "usage.in=" + UPDATED_USAGE);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByUsageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where usage is not null
        defaultHkjMaterialUsageFiltering("usage.specified=true", "usage.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByUsageIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where usage is greater than or equal to
        defaultHkjMaterialUsageFiltering("usage.greaterThanOrEqual=" + DEFAULT_USAGE, "usage.greaterThanOrEqual=" + UPDATED_USAGE);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByUsageIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where usage is less than or equal to
        defaultHkjMaterialUsageFiltering("usage.lessThanOrEqual=" + DEFAULT_USAGE, "usage.lessThanOrEqual=" + SMALLER_USAGE);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByUsageIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where usage is less than
        defaultHkjMaterialUsageFiltering("usage.lessThan=" + UPDATED_USAGE, "usage.lessThan=" + DEFAULT_USAGE);
    }

    @Test
    @Transactional
    void getAllHkjMaterialUsagesByUsageIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterialUsage = hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);

        // Get all the hkjMaterialUsageList where usage is greater than
        defaultHkjMaterialUsageFiltering("usage.greaterThan=" + SMALLER_USAGE, "usage.greaterThan=" + DEFAULT_USAGE);
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
    void getAllHkjMaterialUsagesByJewelryIsEqualToSomething() throws Exception {
        HkjJewelryModel jewelry;
        if (TestUtil.findAll(em, HkjJewelryModel.class).isEmpty()) {
            hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);
            jewelry = HkjJewelryModelResourceIT.createEntity();
        } else {
            jewelry = TestUtil.findAll(em, HkjJewelryModel.class).get(0);
        }
        em.persist(jewelry);
        em.flush();
        hkjMaterialUsage.setJewelry(jewelry);
        hkjMaterialUsageRepository.saveAndFlush(hkjMaterialUsage);
        Long jewelryId = jewelry.getId();
        // Get all the hkjMaterialUsageList where jewelry equals to jewelryId
        defaultHkjMaterialUsageShouldBeFound("jewelryId.equals=" + jewelryId);

        // Get all the hkjMaterialUsageList where jewelry equals to (jewelryId + 1)
        defaultHkjMaterialUsageShouldNotBeFound("jewelryId.equals=" + (jewelryId + 1));
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
            .andExpect(jsonPath("$.[*].usage").value(hasItem(DEFAULT_USAGE)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
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
        updatedHkjMaterialUsage.usage(UPDATED_USAGE).notes(UPDATED_NOTES).price(UPDATED_PRICE).isDeleted(UPDATED_IS_DELETED);
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

        partialUpdatedHkjMaterialUsage.notes(UPDATED_NOTES).isDeleted(UPDATED_IS_DELETED);

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

        partialUpdatedHkjMaterialUsage.usage(UPDATED_USAGE).notes(UPDATED_NOTES).price(UPDATED_PRICE).isDeleted(UPDATED_IS_DELETED);

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
