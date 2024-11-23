package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjOrderItemAsserts.*;
import static com.server.hkj.web.rest.TestUtil.createUpdateProxyForBean;
import static com.server.hkj.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.hkj.IntegrationTest;
import com.server.hkj.domain.HkjCategory;
import com.server.hkj.domain.HkjJewelryModel;
import com.server.hkj.domain.HkjMaterial;
import com.server.hkj.domain.HkjOrder;
import com.server.hkj.domain.HkjOrderItem;
import com.server.hkj.repository.HkjOrderItemRepository;
import com.server.hkj.service.dto.HkjOrderItemDTO;
import com.server.hkj.service.mapper.HkjOrderItemMapper;
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
 * Integration tests for the {@link HkjOrderItemResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjOrderItemResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final String DEFAULT_SPECIAL_REQUESTS = "AAAAAAAAAA";
    private static final String UPDATED_SPECIAL_REQUESTS = "BBBBBBBBBB";

    private static final BigDecimal DEFAULT_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_PRICE = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/hkj-order-items";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjOrderItemRepository hkjOrderItemRepository;

    @Autowired
    private HkjOrderItemMapper hkjOrderItemMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjOrderItemMockMvc;

    private HkjOrderItem hkjOrderItem;

    private HkjOrderItem insertedHkjOrderItem;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjOrderItem createEntity() {
        return new HkjOrderItem()
            .quantity(DEFAULT_QUANTITY)
            .specialRequests(DEFAULT_SPECIAL_REQUESTS)
            .price(DEFAULT_PRICE)
            .isDeleted(DEFAULT_IS_DELETED)
            .notes(DEFAULT_NOTES);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjOrderItem createUpdatedEntity() {
        return new HkjOrderItem()
            .quantity(UPDATED_QUANTITY)
            .specialRequests(UPDATED_SPECIAL_REQUESTS)
            .price(UPDATED_PRICE)
            .isDeleted(UPDATED_IS_DELETED)
            .notes(UPDATED_NOTES);
    }

    @BeforeEach
    public void initTest() {
        hkjOrderItem = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjOrderItem != null) {
            hkjOrderItemRepository.delete(insertedHkjOrderItem);
            insertedHkjOrderItem = null;
        }
    }

    @Test
    @Transactional
    void createHkjOrderItem() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjOrderItem
        HkjOrderItemDTO hkjOrderItemDTO = hkjOrderItemMapper.toDto(hkjOrderItem);
        var returnedHkjOrderItemDTO = om.readValue(
            restHkjOrderItemMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjOrderItemDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjOrderItemDTO.class
        );

        // Validate the HkjOrderItem in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjOrderItem = hkjOrderItemMapper.toEntity(returnedHkjOrderItemDTO);
        assertHkjOrderItemUpdatableFieldsEquals(returnedHkjOrderItem, getPersistedHkjOrderItem(returnedHkjOrderItem));

        insertedHkjOrderItem = returnedHkjOrderItem;
    }

    @Test
    @Transactional
    void createHkjOrderItemWithExistingId() throws Exception {
        // Create the HkjOrderItem with an existing ID
        hkjOrderItem.setId(1L);
        HkjOrderItemDTO hkjOrderItemDTO = hkjOrderItemMapper.toDto(hkjOrderItem);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjOrderItemMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjOrderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjOrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHkjOrderItems() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList
        restHkjOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjOrderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].specialRequests").value(hasItem(DEFAULT_SPECIAL_REQUESTS)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));
    }

    @Test
    @Transactional
    void getHkjOrderItem() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get the hkjOrderItem
        restHkjOrderItemMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjOrderItem.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjOrderItem.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.specialRequests").value(DEFAULT_SPECIAL_REQUESTS))
            .andExpect(jsonPath("$.price").value(sameNumber(DEFAULT_PRICE)))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES));
    }

    @Test
    @Transactional
    void getHkjOrderItemsByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        Long id = hkjOrderItem.getId();

        defaultHkjOrderItemFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjOrderItemFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjOrderItemFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where quantity equals to
        defaultHkjOrderItemFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where quantity in
        defaultHkjOrderItemFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where quantity is not null
        defaultHkjOrderItemFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where quantity is greater than or equal to
        defaultHkjOrderItemFiltering(
            "quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY,
            "quantity.greaterThanOrEqual=" + (DEFAULT_QUANTITY + 1)
        );
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where quantity is less than or equal to
        defaultHkjOrderItemFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where quantity is less than
        defaultHkjOrderItemFiltering("quantity.lessThan=" + (DEFAULT_QUANTITY + 1), "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where quantity is greater than
        defaultHkjOrderItemFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsBySpecialRequestsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where specialRequests equals to
        defaultHkjOrderItemFiltering(
            "specialRequests.equals=" + DEFAULT_SPECIAL_REQUESTS,
            "specialRequests.equals=" + UPDATED_SPECIAL_REQUESTS
        );
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsBySpecialRequestsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where specialRequests in
        defaultHkjOrderItemFiltering(
            "specialRequests.in=" + DEFAULT_SPECIAL_REQUESTS + "," + UPDATED_SPECIAL_REQUESTS,
            "specialRequests.in=" + UPDATED_SPECIAL_REQUESTS
        );
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsBySpecialRequestsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where specialRequests is not null
        defaultHkjOrderItemFiltering("specialRequests.specified=true", "specialRequests.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsBySpecialRequestsContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where specialRequests contains
        defaultHkjOrderItemFiltering(
            "specialRequests.contains=" + DEFAULT_SPECIAL_REQUESTS,
            "specialRequests.contains=" + UPDATED_SPECIAL_REQUESTS
        );
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsBySpecialRequestsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where specialRequests does not contain
        defaultHkjOrderItemFiltering(
            "specialRequests.doesNotContain=" + UPDATED_SPECIAL_REQUESTS,
            "specialRequests.doesNotContain=" + DEFAULT_SPECIAL_REQUESTS
        );
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where price equals to
        defaultHkjOrderItemFiltering("price.equals=" + DEFAULT_PRICE, "price.equals=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where price in
        defaultHkjOrderItemFiltering("price.in=" + DEFAULT_PRICE + "," + UPDATED_PRICE, "price.in=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where price is not null
        defaultHkjOrderItemFiltering("price.specified=true", "price.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where price is greater than or equal to
        defaultHkjOrderItemFiltering("price.greaterThanOrEqual=" + DEFAULT_PRICE, "price.greaterThanOrEqual=" + UPDATED_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where price is less than or equal to
        defaultHkjOrderItemFiltering("price.lessThanOrEqual=" + DEFAULT_PRICE, "price.lessThanOrEqual=" + SMALLER_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where price is less than
        defaultHkjOrderItemFiltering("price.lessThan=" + UPDATED_PRICE, "price.lessThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where price is greater than
        defaultHkjOrderItemFiltering("price.greaterThan=" + SMALLER_PRICE, "price.greaterThan=" + DEFAULT_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where isDeleted equals to
        defaultHkjOrderItemFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where isDeleted in
        defaultHkjOrderItemFiltering("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED, "isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where isDeleted is not null
        defaultHkjOrderItemFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where notes equals to
        defaultHkjOrderItemFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where notes in
        defaultHkjOrderItemFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where notes is not null
        defaultHkjOrderItemFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where notes contains
        defaultHkjOrderItemFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        // Get all the hkjOrderItemList where notes does not contain
        defaultHkjOrderItemFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByMaterialIsEqualToSomething() throws Exception {
        HkjMaterial material;
        if (TestUtil.findAll(em, HkjMaterial.class).isEmpty()) {
            hkjOrderItemRepository.saveAndFlush(hkjOrderItem);
            material = HkjMaterialResourceIT.createEntity();
        } else {
            material = TestUtil.findAll(em, HkjMaterial.class).get(0);
        }
        em.persist(material);
        em.flush();
        hkjOrderItem.setMaterial(material);
        hkjOrderItemRepository.saveAndFlush(hkjOrderItem);
        Long materialId = material.getId();
        // Get all the hkjOrderItemList where material equals to materialId
        defaultHkjOrderItemShouldBeFound("materialId.equals=" + materialId);

        // Get all the hkjOrderItemList where material equals to (materialId + 1)
        defaultHkjOrderItemShouldNotBeFound("materialId.equals=" + (materialId + 1));
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByOrderIsEqualToSomething() throws Exception {
        HkjOrder order;
        if (TestUtil.findAll(em, HkjOrder.class).isEmpty()) {
            hkjOrderItemRepository.saveAndFlush(hkjOrderItem);
            order = HkjOrderResourceIT.createEntity();
        } else {
            order = TestUtil.findAll(em, HkjOrder.class).get(0);
        }
        em.persist(order);
        em.flush();
        hkjOrderItem.setOrder(order);
        hkjOrderItemRepository.saveAndFlush(hkjOrderItem);
        Long orderId = order.getId();
        // Get all the hkjOrderItemList where order equals to orderId
        defaultHkjOrderItemShouldBeFound("orderId.equals=" + orderId);

        // Get all the hkjOrderItemList where order equals to (orderId + 1)
        defaultHkjOrderItemShouldNotBeFound("orderId.equals=" + (orderId + 1));
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByProductIsEqualToSomething() throws Exception {
        HkjJewelryModel product;
        if (TestUtil.findAll(em, HkjJewelryModel.class).isEmpty()) {
            hkjOrderItemRepository.saveAndFlush(hkjOrderItem);
            product = HkjJewelryModelResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, HkjJewelryModel.class).get(0);
        }
        em.persist(product);
        em.flush();
        hkjOrderItem.setProduct(product);
        hkjOrderItemRepository.saveAndFlush(hkjOrderItem);
        Long productId = product.getId();
        // Get all the hkjOrderItemList where product equals to productId
        defaultHkjOrderItemShouldBeFound("productId.equals=" + productId);

        // Get all the hkjOrderItemList where product equals to (productId + 1)
        defaultHkjOrderItemShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    @Test
    @Transactional
    void getAllHkjOrderItemsByCategoryIsEqualToSomething() throws Exception {
        HkjCategory category;
        if (TestUtil.findAll(em, HkjCategory.class).isEmpty()) {
            hkjOrderItemRepository.saveAndFlush(hkjOrderItem);
            category = HkjCategoryResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, HkjCategory.class).get(0);
        }
        em.persist(category);
        em.flush();
        hkjOrderItem.setCategory(category);
        hkjOrderItemRepository.saveAndFlush(hkjOrderItem);
        Long categoryId = category.getId();
        // Get all the hkjOrderItemList where category equals to categoryId
        defaultHkjOrderItemShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the hkjOrderItemList where category equals to (categoryId + 1)
        defaultHkjOrderItemShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    private void defaultHkjOrderItemFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjOrderItemShouldBeFound(shouldBeFound);
        defaultHkjOrderItemShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjOrderItemShouldBeFound(String filter) throws Exception {
        restHkjOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjOrderItem.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].specialRequests").value(hasItem(DEFAULT_SPECIAL_REQUESTS)))
            .andExpect(jsonPath("$.[*].price").value(hasItem(sameNumber(DEFAULT_PRICE))))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)));

        // Check, that the count call also returns 1
        restHkjOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjOrderItemShouldNotBeFound(String filter) throws Exception {
        restHkjOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjOrderItemMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjOrderItem() throws Exception {
        // Get the hkjOrderItem
        restHkjOrderItemMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjOrderItem() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjOrderItem
        HkjOrderItem updatedHkjOrderItem = hkjOrderItemRepository.findById(hkjOrderItem.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjOrderItem are not directly saved in db
        em.detach(updatedHkjOrderItem);
        updatedHkjOrderItem
            .quantity(UPDATED_QUANTITY)
            .specialRequests(UPDATED_SPECIAL_REQUESTS)
            .price(UPDATED_PRICE)
            .isDeleted(UPDATED_IS_DELETED)
            .notes(UPDATED_NOTES);
        HkjOrderItemDTO hkjOrderItemDTO = hkjOrderItemMapper.toDto(updatedHkjOrderItem);

        restHkjOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjOrderItemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjOrderItemDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjOrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjOrderItemToMatchAllProperties(updatedHkjOrderItem);
    }

    @Test
    @Transactional
    void putNonExistingHkjOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjOrderItem.setId(longCount.incrementAndGet());

        // Create the HkjOrderItem
        HkjOrderItemDTO hkjOrderItemDTO = hkjOrderItemMapper.toDto(hkjOrderItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjOrderItemDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjOrderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjOrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjOrderItem.setId(longCount.incrementAndGet());

        // Create the HkjOrderItem
        HkjOrderItemDTO hkjOrderItemDTO = hkjOrderItemMapper.toDto(hkjOrderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjOrderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjOrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjOrderItem.setId(longCount.incrementAndGet());

        // Create the HkjOrderItem
        HkjOrderItemDTO hkjOrderItemDTO = hkjOrderItemMapper.toDto(hkjOrderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjOrderItemMockMvc
            .perform(
                put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjOrderItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjOrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjOrderItemWithPatch() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjOrderItem using partial update
        HkjOrderItem partialUpdatedHkjOrderItem = new HkjOrderItem();
        partialUpdatedHkjOrderItem.setId(hkjOrderItem.getId());

        partialUpdatedHkjOrderItem.quantity(UPDATED_QUANTITY).specialRequests(UPDATED_SPECIAL_REQUESTS).price(UPDATED_PRICE);

        restHkjOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjOrderItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the HkjOrderItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjOrderItemUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHkjOrderItem, hkjOrderItem),
            getPersistedHkjOrderItem(hkjOrderItem)
        );
    }

    @Test
    @Transactional
    void fullUpdateHkjOrderItemWithPatch() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjOrderItem using partial update
        HkjOrderItem partialUpdatedHkjOrderItem = new HkjOrderItem();
        partialUpdatedHkjOrderItem.setId(hkjOrderItem.getId());

        partialUpdatedHkjOrderItem
            .quantity(UPDATED_QUANTITY)
            .specialRequests(UPDATED_SPECIAL_REQUESTS)
            .price(UPDATED_PRICE)
            .isDeleted(UPDATED_IS_DELETED)
            .notes(UPDATED_NOTES);

        restHkjOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjOrderItem.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjOrderItem))
            )
            .andExpect(status().isOk());

        // Validate the HkjOrderItem in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjOrderItemUpdatableFieldsEquals(partialUpdatedHkjOrderItem, getPersistedHkjOrderItem(partialUpdatedHkjOrderItem));
    }

    @Test
    @Transactional
    void patchNonExistingHkjOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjOrderItem.setId(longCount.incrementAndGet());

        // Create the HkjOrderItem
        HkjOrderItemDTO hkjOrderItemDTO = hkjOrderItemMapper.toDto(hkjOrderItem);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjOrderItemDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjOrderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjOrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjOrderItem.setId(longCount.incrementAndGet());

        // Create the HkjOrderItem
        HkjOrderItemDTO hkjOrderItemDTO = hkjOrderItemMapper.toDto(hkjOrderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjOrderItemDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjOrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjOrderItem() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjOrderItem.setId(longCount.incrementAndGet());

        // Create the HkjOrderItem
        HkjOrderItemDTO hkjOrderItemDTO = hkjOrderItemMapper.toDto(hkjOrderItem);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjOrderItemMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjOrderItemDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjOrderItem in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjOrderItem() throws Exception {
        // Initialize the database
        insertedHkjOrderItem = hkjOrderItemRepository.saveAndFlush(hkjOrderItem);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjOrderItem
        restHkjOrderItemMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjOrderItem.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjOrderItemRepository.count();
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

    protected HkjOrderItem getPersistedHkjOrderItem(HkjOrderItem hkjOrderItem) {
        return hkjOrderItemRepository.findById(hkjOrderItem.getId()).orElseThrow();
    }

    protected void assertPersistedHkjOrderItemToMatchAllProperties(HkjOrderItem expectedHkjOrderItem) {
        assertHkjOrderItemAllPropertiesEquals(expectedHkjOrderItem, getPersistedHkjOrderItem(expectedHkjOrderItem));
    }

    protected void assertPersistedHkjOrderItemToMatchUpdatableProperties(HkjOrderItem expectedHkjOrderItem) {
        assertHkjOrderItemAllUpdatablePropertiesEquals(expectedHkjOrderItem, getPersistedHkjOrderItem(expectedHkjOrderItem));
    }
}
