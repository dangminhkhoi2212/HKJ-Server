package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjMaterialAsserts.*;
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
import com.server.hkj.repository.HkjMaterialRepository;
import com.server.hkj.service.dto.HkjMaterialDTO;
import com.server.hkj.service.mapper.HkjMaterialMapper;
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
 * Integration tests for the {@link HkjMaterialResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjMaterialResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE_PER_UNIT = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE_PER_UNIT = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE_PER_UNIT = new BigDecimal(1 - 1);

    private static final String DEFAULT_COVER_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_COVER_IMAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/hkj-materials";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjMaterialRepository hkjMaterialRepository;

    @Autowired
    private HkjMaterialMapper hkjMaterialMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjMaterialMockMvc;

    private HkjMaterial hkjMaterial;

    private HkjMaterial insertedHkjMaterial;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjMaterial createEntity() {
        return new HkjMaterial()
            .name(DEFAULT_NAME)
            .unit(DEFAULT_UNIT)
            .pricePerUnit(DEFAULT_PRICE_PER_UNIT)
            .coverImage(DEFAULT_COVER_IMAGE)
            .isDeleted(DEFAULT_IS_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjMaterial createUpdatedEntity() {
        return new HkjMaterial()
            .name(UPDATED_NAME)
            .unit(UPDATED_UNIT)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .coverImage(UPDATED_COVER_IMAGE)
            .isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    public void initTest() {
        hkjMaterial = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjMaterial != null) {
            hkjMaterialRepository.delete(insertedHkjMaterial);
            insertedHkjMaterial = null;
        }
    }

    @Test
    @Transactional
    void createHkjMaterial() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjMaterial
        HkjMaterialDTO hkjMaterialDTO = hkjMaterialMapper.toDto(hkjMaterial);
        var returnedHkjMaterialDTO = om.readValue(
            restHkjMaterialMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjMaterialDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjMaterialDTO.class
        );

        // Validate the HkjMaterial in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjMaterial = hkjMaterialMapper.toEntity(returnedHkjMaterialDTO);
        assertHkjMaterialUpdatableFieldsEquals(returnedHkjMaterial, getPersistedHkjMaterial(returnedHkjMaterial));

        insertedHkjMaterial = returnedHkjMaterial;
    }

    @Test
    @Transactional
    void createHkjMaterialWithExistingId() throws Exception {
        // Create the HkjMaterial with an existing ID
        hkjMaterial.setId(1L);
        HkjMaterialDTO hkjMaterialDTO = hkjMaterialMapper.toDto(hkjMaterial);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjMaterialMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjMaterialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjMaterial in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjMaterial.setName(null);

        // Create the HkjMaterial, which fails.
        HkjMaterialDTO hkjMaterialDTO = hkjMaterialMapper.toDto(hkjMaterial);

        restHkjMaterialMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjMaterialDTO))
            )
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHkjMaterials() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList
        restHkjMaterialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjMaterial.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].pricePerUnit").value(hasItem(sameNumber(DEFAULT_PRICE_PER_UNIT))))
            .andExpect(jsonPath("$.[*].coverImage").value(hasItem(DEFAULT_COVER_IMAGE)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getHkjMaterial() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get the hkjMaterial
        restHkjMaterialMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjMaterial.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjMaterial.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.pricePerUnit").value(sameNumber(DEFAULT_PRICE_PER_UNIT)))
            .andExpect(jsonPath("$.coverImage").value(DEFAULT_COVER_IMAGE))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getHkjMaterialsByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        Long id = hkjMaterial.getId();

        defaultHkjMaterialFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjMaterialFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjMaterialFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where name equals to
        defaultHkjMaterialFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where name in
        defaultHkjMaterialFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where name is not null
        defaultHkjMaterialFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where name contains
        defaultHkjMaterialFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where name does not contain
        defaultHkjMaterialFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where unit equals to
        defaultHkjMaterialFiltering("unit.equals=" + DEFAULT_UNIT, "unit.equals=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByUnitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where unit in
        defaultHkjMaterialFiltering("unit.in=" + DEFAULT_UNIT + "," + UPDATED_UNIT, "unit.in=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where unit is not null
        defaultHkjMaterialFiltering("unit.specified=true", "unit.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByUnitContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where unit contains
        defaultHkjMaterialFiltering("unit.contains=" + DEFAULT_UNIT, "unit.contains=" + UPDATED_UNIT);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByUnitNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where unit does not contain
        defaultHkjMaterialFiltering("unit.doesNotContain=" + UPDATED_UNIT, "unit.doesNotContain=" + DEFAULT_UNIT);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByPricePerUnitIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where pricePerUnit equals to
        defaultHkjMaterialFiltering("pricePerUnit.equals=" + DEFAULT_PRICE_PER_UNIT, "pricePerUnit.equals=" + UPDATED_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByPricePerUnitIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where pricePerUnit in
        defaultHkjMaterialFiltering(
            "pricePerUnit.in=" + DEFAULT_PRICE_PER_UNIT + "," + UPDATED_PRICE_PER_UNIT,
            "pricePerUnit.in=" + UPDATED_PRICE_PER_UNIT
        );
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByPricePerUnitIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where pricePerUnit is not null
        defaultHkjMaterialFiltering("pricePerUnit.specified=true", "pricePerUnit.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByPricePerUnitIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where pricePerUnit is greater than or equal to
        defaultHkjMaterialFiltering(
            "pricePerUnit.greaterThanOrEqual=" + DEFAULT_PRICE_PER_UNIT,
            "pricePerUnit.greaterThanOrEqual=" + UPDATED_PRICE_PER_UNIT
        );
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByPricePerUnitIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where pricePerUnit is less than or equal to
        defaultHkjMaterialFiltering(
            "pricePerUnit.lessThanOrEqual=" + DEFAULT_PRICE_PER_UNIT,
            "pricePerUnit.lessThanOrEqual=" + SMALLER_PRICE_PER_UNIT
        );
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByPricePerUnitIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where pricePerUnit is less than
        defaultHkjMaterialFiltering("pricePerUnit.lessThan=" + UPDATED_PRICE_PER_UNIT, "pricePerUnit.lessThan=" + DEFAULT_PRICE_PER_UNIT);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByPricePerUnitIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where pricePerUnit is greater than
        defaultHkjMaterialFiltering(
            "pricePerUnit.greaterThan=" + SMALLER_PRICE_PER_UNIT,
            "pricePerUnit.greaterThan=" + DEFAULT_PRICE_PER_UNIT
        );
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByCoverImageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where coverImage equals to
        defaultHkjMaterialFiltering("coverImage.equals=" + DEFAULT_COVER_IMAGE, "coverImage.equals=" + UPDATED_COVER_IMAGE);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByCoverImageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where coverImage in
        defaultHkjMaterialFiltering(
            "coverImage.in=" + DEFAULT_COVER_IMAGE + "," + UPDATED_COVER_IMAGE,
            "coverImage.in=" + UPDATED_COVER_IMAGE
        );
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByCoverImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where coverImage is not null
        defaultHkjMaterialFiltering("coverImage.specified=true", "coverImage.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByCoverImageContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where coverImage contains
        defaultHkjMaterialFiltering("coverImage.contains=" + DEFAULT_COVER_IMAGE, "coverImage.contains=" + UPDATED_COVER_IMAGE);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByCoverImageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where coverImage does not contain
        defaultHkjMaterialFiltering("coverImage.doesNotContain=" + UPDATED_COVER_IMAGE, "coverImage.doesNotContain=" + DEFAULT_COVER_IMAGE);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where isDeleted equals to
        defaultHkjMaterialFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where isDeleted in
        defaultHkjMaterialFiltering("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED, "isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where isDeleted is not null
        defaultHkjMaterialFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    private void defaultHkjMaterialFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjMaterialShouldBeFound(shouldBeFound);
        defaultHkjMaterialShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjMaterialShouldBeFound(String filter) throws Exception {
        restHkjMaterialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjMaterial.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].pricePerUnit").value(hasItem(sameNumber(DEFAULT_PRICE_PER_UNIT))))
            .andExpect(jsonPath("$.[*].coverImage").value(hasItem(DEFAULT_COVER_IMAGE)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restHkjMaterialMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjMaterialShouldNotBeFound(String filter) throws Exception {
        restHkjMaterialMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjMaterialMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjMaterial() throws Exception {
        // Get the hkjMaterial
        restHkjMaterialMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjMaterial() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjMaterial
        HkjMaterial updatedHkjMaterial = hkjMaterialRepository.findById(hkjMaterial.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjMaterial are not directly saved in db
        em.detach(updatedHkjMaterial);
        updatedHkjMaterial
            .name(UPDATED_NAME)
            .unit(UPDATED_UNIT)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .coverImage(UPDATED_COVER_IMAGE)
            .isDeleted(UPDATED_IS_DELETED);
        HkjMaterialDTO hkjMaterialDTO = hkjMaterialMapper.toDto(updatedHkjMaterial);

        restHkjMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjMaterialDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjMaterialDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjMaterial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjMaterialToMatchAllProperties(updatedHkjMaterial);
    }

    @Test
    @Transactional
    void putNonExistingHkjMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjMaterial.setId(longCount.incrementAndGet());

        // Create the HkjMaterial
        HkjMaterialDTO hkjMaterialDTO = hkjMaterialMapper.toDto(hkjMaterial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjMaterialDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjMaterialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjMaterial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjMaterial.setId(longCount.incrementAndGet());

        // Create the HkjMaterial
        HkjMaterialDTO hkjMaterialDTO = hkjMaterialMapper.toDto(hkjMaterial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjMaterialMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjMaterialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjMaterial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjMaterial.setId(longCount.incrementAndGet());

        // Create the HkjMaterial
        HkjMaterialDTO hkjMaterialDTO = hkjMaterialMapper.toDto(hkjMaterial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjMaterialMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjMaterialDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjMaterial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjMaterialWithPatch() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjMaterial using partial update
        HkjMaterial partialUpdatedHkjMaterial = new HkjMaterial();
        partialUpdatedHkjMaterial.setId(hkjMaterial.getId());

        partialUpdatedHkjMaterial.name(UPDATED_NAME).unit(UPDATED_UNIT).coverImage(UPDATED_COVER_IMAGE);

        restHkjMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjMaterial.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjMaterial))
            )
            .andExpect(status().isOk());

        // Validate the HkjMaterial in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjMaterialUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHkjMaterial, hkjMaterial),
            getPersistedHkjMaterial(hkjMaterial)
        );
    }

    @Test
    @Transactional
    void fullUpdateHkjMaterialWithPatch() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjMaterial using partial update
        HkjMaterial partialUpdatedHkjMaterial = new HkjMaterial();
        partialUpdatedHkjMaterial.setId(hkjMaterial.getId());

        partialUpdatedHkjMaterial
            .name(UPDATED_NAME)
            .unit(UPDATED_UNIT)
            .pricePerUnit(UPDATED_PRICE_PER_UNIT)
            .coverImage(UPDATED_COVER_IMAGE)
            .isDeleted(UPDATED_IS_DELETED);

        restHkjMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjMaterial.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjMaterial))
            )
            .andExpect(status().isOk());

        // Validate the HkjMaterial in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjMaterialUpdatableFieldsEquals(partialUpdatedHkjMaterial, getPersistedHkjMaterial(partialUpdatedHkjMaterial));
    }

    @Test
    @Transactional
    void patchNonExistingHkjMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjMaterial.setId(longCount.incrementAndGet());

        // Create the HkjMaterial
        HkjMaterialDTO hkjMaterialDTO = hkjMaterialMapper.toDto(hkjMaterial);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjMaterialDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjMaterialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjMaterial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjMaterial.setId(longCount.incrementAndGet());

        // Create the HkjMaterial
        HkjMaterialDTO hkjMaterialDTO = hkjMaterialMapper.toDto(hkjMaterial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjMaterialDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjMaterial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjMaterial() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjMaterial.setId(longCount.incrementAndGet());

        // Create the HkjMaterial
        HkjMaterialDTO hkjMaterialDTO = hkjMaterialMapper.toDto(hkjMaterial);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjMaterialMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hkjMaterialDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjMaterial in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjMaterial() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjMaterial
        restHkjMaterialMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjMaterial.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjMaterialRepository.count();
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

    protected HkjMaterial getPersistedHkjMaterial(HkjMaterial hkjMaterial) {
        return hkjMaterialRepository.findById(hkjMaterial.getId()).orElseThrow();
    }

    protected void assertPersistedHkjMaterialToMatchAllProperties(HkjMaterial expectedHkjMaterial) {
        assertHkjMaterialAllPropertiesEquals(expectedHkjMaterial, getPersistedHkjMaterial(expectedHkjMaterial));
    }

    protected void assertPersistedHkjMaterialToMatchUpdatableProperties(HkjMaterial expectedHkjMaterial) {
        assertHkjMaterialAllUpdatablePropertiesEquals(expectedHkjMaterial, getPersistedHkjMaterial(expectedHkjMaterial));
    }
}
