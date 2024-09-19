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

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final String DEFAULT_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_UNIT = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_UNIT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_UNIT_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_UNIT_PRICE = new BigDecimal(1 - 1);

    private static final String DEFAULT_SUPPLIER = "AAAAAAAAAA";
    private static final String UPDATED_SUPPLIER = "BBBBBBBBBB";

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
    public static HkjMaterial createEntity(EntityManager em) {
        HkjMaterial hkjMaterial = new HkjMaterial()
            .name(DEFAULT_NAME)
            .quantity(DEFAULT_QUANTITY)
            .unit(DEFAULT_UNIT)
            .unitPrice(DEFAULT_UNIT_PRICE)
            .supplier(DEFAULT_SUPPLIER)
            .isDeleted(DEFAULT_IS_DELETED);
        return hkjMaterial;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjMaterial createUpdatedEntity(EntityManager em) {
        HkjMaterial hkjMaterial = new HkjMaterial()
            .name(UPDATED_NAME)
            .quantity(UPDATED_QUANTITY)
            .unit(UPDATED_UNIT)
            .unitPrice(UPDATED_UNIT_PRICE)
            .supplier(UPDATED_SUPPLIER)
            .isDeleted(UPDATED_IS_DELETED);
        return hkjMaterial;
    }

    @BeforeEach
    public void initTest() {
        hkjMaterial = createEntity(em);
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
    void checkQuantityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjMaterial.setQuantity(null);

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
    void checkUnitIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjMaterial.setUnit(null);

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
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].supplier").value(hasItem(DEFAULT_SUPPLIER)))
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
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.unit").value(DEFAULT_UNIT))
            .andExpect(jsonPath("$.unitPrice").value(sameNumber(DEFAULT_UNIT_PRICE)))
            .andExpect(jsonPath("$.supplier").value(DEFAULT_SUPPLIER))
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
    void getAllHkjMaterialsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where quantity equals to
        defaultHkjMaterialFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where quantity in
        defaultHkjMaterialFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where quantity is not null
        defaultHkjMaterialFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where quantity is greater than or equal to
        defaultHkjMaterialFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where quantity is less than or equal to
        defaultHkjMaterialFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where quantity is less than
        defaultHkjMaterialFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where quantity is greater than
        defaultHkjMaterialFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
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
    void getAllHkjMaterialsByUnitPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where unitPrice equals to
        defaultHkjMaterialFiltering("unitPrice.equals=" + DEFAULT_UNIT_PRICE, "unitPrice.equals=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByUnitPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where unitPrice in
        defaultHkjMaterialFiltering("unitPrice.in=" + DEFAULT_UNIT_PRICE + "," + UPDATED_UNIT_PRICE, "unitPrice.in=" + UPDATED_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByUnitPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where unitPrice is not null
        defaultHkjMaterialFiltering("unitPrice.specified=true", "unitPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByUnitPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where unitPrice is greater than or equal to
        defaultHkjMaterialFiltering(
            "unitPrice.greaterThanOrEqual=" + DEFAULT_UNIT_PRICE,
            "unitPrice.greaterThanOrEqual=" + UPDATED_UNIT_PRICE
        );
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByUnitPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where unitPrice is less than or equal to
        defaultHkjMaterialFiltering("unitPrice.lessThanOrEqual=" + DEFAULT_UNIT_PRICE, "unitPrice.lessThanOrEqual=" + SMALLER_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByUnitPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where unitPrice is less than
        defaultHkjMaterialFiltering("unitPrice.lessThan=" + UPDATED_UNIT_PRICE, "unitPrice.lessThan=" + DEFAULT_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsByUnitPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where unitPrice is greater than
        defaultHkjMaterialFiltering("unitPrice.greaterThan=" + SMALLER_UNIT_PRICE, "unitPrice.greaterThan=" + DEFAULT_UNIT_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsBySupplierIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where supplier equals to
        defaultHkjMaterialFiltering("supplier.equals=" + DEFAULT_SUPPLIER, "supplier.equals=" + UPDATED_SUPPLIER);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsBySupplierIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where supplier in
        defaultHkjMaterialFiltering("supplier.in=" + DEFAULT_SUPPLIER + "," + UPDATED_SUPPLIER, "supplier.in=" + UPDATED_SUPPLIER);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsBySupplierIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where supplier is not null
        defaultHkjMaterialFiltering("supplier.specified=true", "supplier.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjMaterialsBySupplierContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where supplier contains
        defaultHkjMaterialFiltering("supplier.contains=" + DEFAULT_SUPPLIER, "supplier.contains=" + UPDATED_SUPPLIER);
    }

    @Test
    @Transactional
    void getAllHkjMaterialsBySupplierNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjMaterial = hkjMaterialRepository.saveAndFlush(hkjMaterial);

        // Get all the hkjMaterialList where supplier does not contain
        defaultHkjMaterialFiltering("supplier.doesNotContain=" + UPDATED_SUPPLIER, "supplier.doesNotContain=" + DEFAULT_SUPPLIER);
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
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].unit").value(hasItem(DEFAULT_UNIT)))
            .andExpect(jsonPath("$.[*].unitPrice").value(hasItem(sameNumber(DEFAULT_UNIT_PRICE))))
            .andExpect(jsonPath("$.[*].supplier").value(hasItem(DEFAULT_SUPPLIER)))
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
            .quantity(UPDATED_QUANTITY)
            .unit(UPDATED_UNIT)
            .unitPrice(UPDATED_UNIT_PRICE)
            .supplier(UPDATED_SUPPLIER)
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

        partialUpdatedHkjMaterial.name(UPDATED_NAME).quantity(UPDATED_QUANTITY).unit(UPDATED_UNIT).unitPrice(UPDATED_UNIT_PRICE);

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
            .quantity(UPDATED_QUANTITY)
            .unit(UPDATED_UNIT)
            .unitPrice(UPDATED_UNIT_PRICE)
            .supplier(UPDATED_SUPPLIER)
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
