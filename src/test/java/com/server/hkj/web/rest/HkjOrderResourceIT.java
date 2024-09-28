package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjOrderAsserts.*;
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
import com.server.hkj.domain.HkjOrder;
import com.server.hkj.domain.HkjProject;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.domain.enumeration.HkjOrderStatus;
import com.server.hkj.repository.HkjOrderRepository;
import com.server.hkj.service.dto.HkjOrderDTO;
import com.server.hkj.service.mapper.HkjOrderMapper;
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
 * Integration tests for the {@link HkjOrderResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjOrderResourceIT {

    private static final Instant DEFAULT_ORDER_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ORDER_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPECTED_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPECTED_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_ACTUAL_DELIVERY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ACTUAL_DELIVERY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_SPECIAL_REQUESTS = "AAAAAAAAAA";
    private static final String UPDATED_SPECIAL_REQUESTS = "BBBBBBBBBB";

    private static final HkjOrderStatus DEFAULT_STATUS = HkjOrderStatus.NEW;
    private static final HkjOrderStatus UPDATED_STATUS = HkjOrderStatus.PROCESSING;

    private static final Integer DEFAULT_CUSTOMER_RATING = 1;
    private static final Integer UPDATED_CUSTOMER_RATING = 2;
    private static final Integer SMALLER_CUSTOMER_RATING = 1 - 1;

    private static final BigDecimal DEFAULT_TOTAL_PRICE = new BigDecimal(1);
    private static final BigDecimal UPDATED_TOTAL_PRICE = new BigDecimal(2);
    private static final BigDecimal SMALLER_TOTAL_PRICE = new BigDecimal(1 - 1);

    private static final BigDecimal DEFAULT_DEPOSIT_AMOUNT = new BigDecimal(1);
    private static final BigDecimal UPDATED_DEPOSIT_AMOUNT = new BigDecimal(2);
    private static final BigDecimal SMALLER_DEPOSIT_AMOUNT = new BigDecimal(1 - 1);

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/hkj-orders";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjOrderRepository hkjOrderRepository;

    @Autowired
    private HkjOrderMapper hkjOrderMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjOrderMockMvc;

    private HkjOrder hkjOrder;

    private HkjOrder insertedHkjOrder;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjOrder createEntity() {
        return new HkjOrder()
            .orderDate(DEFAULT_ORDER_DATE)
            .expectedDeliveryDate(DEFAULT_EXPECTED_DELIVERY_DATE)
            .actualDeliveryDate(DEFAULT_ACTUAL_DELIVERY_DATE)
            .specialRequests(DEFAULT_SPECIAL_REQUESTS)
            .status(DEFAULT_STATUS)
            .customerRating(DEFAULT_CUSTOMER_RATING)
            .totalPrice(DEFAULT_TOTAL_PRICE)
            .depositAmount(DEFAULT_DEPOSIT_AMOUNT)
            .notes(DEFAULT_NOTES)
            .isDeleted(DEFAULT_IS_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjOrder createUpdatedEntity() {
        return new HkjOrder()
            .orderDate(UPDATED_ORDER_DATE)
            .expectedDeliveryDate(UPDATED_EXPECTED_DELIVERY_DATE)
            .actualDeliveryDate(UPDATED_ACTUAL_DELIVERY_DATE)
            .specialRequests(UPDATED_SPECIAL_REQUESTS)
            .status(UPDATED_STATUS)
            .customerRating(UPDATED_CUSTOMER_RATING)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .depositAmount(UPDATED_DEPOSIT_AMOUNT)
            .notes(UPDATED_NOTES)
            .isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    public void initTest() {
        hkjOrder = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjOrder != null) {
            hkjOrderRepository.delete(insertedHkjOrder);
            insertedHkjOrder = null;
        }
    }

    @Test
    @Transactional
    void createHkjOrder() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjOrder
        HkjOrderDTO hkjOrderDTO = hkjOrderMapper.toDto(hkjOrder);
        var returnedHkjOrderDTO = om.readValue(
            restHkjOrderMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjOrderDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjOrderDTO.class
        );

        // Validate the HkjOrder in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjOrder = hkjOrderMapper.toEntity(returnedHkjOrderDTO);
        assertHkjOrderUpdatableFieldsEquals(returnedHkjOrder, getPersistedHkjOrder(returnedHkjOrder));

        insertedHkjOrder = returnedHkjOrder;
    }

    @Test
    @Transactional
    void createHkjOrderWithExistingId() throws Exception {
        // Create the HkjOrder with an existing ID
        hkjOrder.setId(1L);
        HkjOrderDTO hkjOrderDTO = hkjOrderMapper.toDto(hkjOrder);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjOrderMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjOrderDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HkjOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkOrderDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjOrder.setOrderDate(null);

        // Create the HkjOrder, which fails.
        HkjOrderDTO hkjOrderDTO = hkjOrderMapper.toDto(hkjOrder);

        restHkjOrderMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjOrderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjOrder.setStatus(null);

        // Create the HkjOrder, which fails.
        HkjOrderDTO hkjOrderDTO = hkjOrderMapper.toDto(hkjOrder);

        restHkjOrderMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjOrderDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHkjOrders() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList
        restHkjOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].expectedDeliveryDate").value(hasItem(DEFAULT_EXPECTED_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].actualDeliveryDate").value(hasItem(DEFAULT_ACTUAL_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].specialRequests").value(hasItem(DEFAULT_SPECIAL_REQUESTS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].customerRating").value(hasItem(DEFAULT_CUSTOMER_RATING)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].depositAmount").value(hasItem(sameNumber(DEFAULT_DEPOSIT_AMOUNT))))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getHkjOrder() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get the hkjOrder
        restHkjOrderMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjOrder.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjOrder.getId().intValue()))
            .andExpect(jsonPath("$.orderDate").value(DEFAULT_ORDER_DATE.toString()))
            .andExpect(jsonPath("$.expectedDeliveryDate").value(DEFAULT_EXPECTED_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.actualDeliveryDate").value(DEFAULT_ACTUAL_DELIVERY_DATE.toString()))
            .andExpect(jsonPath("$.specialRequests").value(DEFAULT_SPECIAL_REQUESTS))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.customerRating").value(DEFAULT_CUSTOMER_RATING))
            .andExpect(jsonPath("$.totalPrice").value(sameNumber(DEFAULT_TOTAL_PRICE)))
            .andExpect(jsonPath("$.depositAmount").value(sameNumber(DEFAULT_DEPOSIT_AMOUNT)))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getHkjOrdersByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        Long id = hkjOrder.getId();

        defaultHkjOrderFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjOrderFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjOrderFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjOrdersByOrderDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where orderDate equals to
        defaultHkjOrderFiltering("orderDate.equals=" + DEFAULT_ORDER_DATE, "orderDate.equals=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllHkjOrdersByOrderDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where orderDate in
        defaultHkjOrderFiltering("orderDate.in=" + DEFAULT_ORDER_DATE + "," + UPDATED_ORDER_DATE, "orderDate.in=" + UPDATED_ORDER_DATE);
    }

    @Test
    @Transactional
    void getAllHkjOrdersByOrderDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where orderDate is not null
        defaultHkjOrderFiltering("orderDate.specified=true", "orderDate.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjOrdersByExpectedDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where expectedDeliveryDate equals to
        defaultHkjOrderFiltering(
            "expectedDeliveryDate.equals=" + DEFAULT_EXPECTED_DELIVERY_DATE,
            "expectedDeliveryDate.equals=" + UPDATED_EXPECTED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllHkjOrdersByExpectedDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where expectedDeliveryDate in
        defaultHkjOrderFiltering(
            "expectedDeliveryDate.in=" + DEFAULT_EXPECTED_DELIVERY_DATE + "," + UPDATED_EXPECTED_DELIVERY_DATE,
            "expectedDeliveryDate.in=" + UPDATED_EXPECTED_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllHkjOrdersByExpectedDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where expectedDeliveryDate is not null
        defaultHkjOrderFiltering("expectedDeliveryDate.specified=true", "expectedDeliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjOrdersByActualDeliveryDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where actualDeliveryDate equals to
        defaultHkjOrderFiltering(
            "actualDeliveryDate.equals=" + DEFAULT_ACTUAL_DELIVERY_DATE,
            "actualDeliveryDate.equals=" + UPDATED_ACTUAL_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllHkjOrdersByActualDeliveryDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where actualDeliveryDate in
        defaultHkjOrderFiltering(
            "actualDeliveryDate.in=" + DEFAULT_ACTUAL_DELIVERY_DATE + "," + UPDATED_ACTUAL_DELIVERY_DATE,
            "actualDeliveryDate.in=" + UPDATED_ACTUAL_DELIVERY_DATE
        );
    }

    @Test
    @Transactional
    void getAllHkjOrdersByActualDeliveryDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where actualDeliveryDate is not null
        defaultHkjOrderFiltering("actualDeliveryDate.specified=true", "actualDeliveryDate.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjOrdersBySpecialRequestsIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where specialRequests equals to
        defaultHkjOrderFiltering(
            "specialRequests.equals=" + DEFAULT_SPECIAL_REQUESTS,
            "specialRequests.equals=" + UPDATED_SPECIAL_REQUESTS
        );
    }

    @Test
    @Transactional
    void getAllHkjOrdersBySpecialRequestsIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where specialRequests in
        defaultHkjOrderFiltering(
            "specialRequests.in=" + DEFAULT_SPECIAL_REQUESTS + "," + UPDATED_SPECIAL_REQUESTS,
            "specialRequests.in=" + UPDATED_SPECIAL_REQUESTS
        );
    }

    @Test
    @Transactional
    void getAllHkjOrdersBySpecialRequestsIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where specialRequests is not null
        defaultHkjOrderFiltering("specialRequests.specified=true", "specialRequests.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjOrdersBySpecialRequestsContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where specialRequests contains
        defaultHkjOrderFiltering(
            "specialRequests.contains=" + DEFAULT_SPECIAL_REQUESTS,
            "specialRequests.contains=" + UPDATED_SPECIAL_REQUESTS
        );
    }

    @Test
    @Transactional
    void getAllHkjOrdersBySpecialRequestsNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where specialRequests does not contain
        defaultHkjOrderFiltering(
            "specialRequests.doesNotContain=" + UPDATED_SPECIAL_REQUESTS,
            "specialRequests.doesNotContain=" + DEFAULT_SPECIAL_REQUESTS
        );
    }

    @Test
    @Transactional
    void getAllHkjOrdersByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where status equals to
        defaultHkjOrderFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllHkjOrdersByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where status in
        defaultHkjOrderFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllHkjOrdersByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where status is not null
        defaultHkjOrderFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjOrdersByCustomerRatingIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where customerRating equals to
        defaultHkjOrderFiltering("customerRating.equals=" + DEFAULT_CUSTOMER_RATING, "customerRating.equals=" + UPDATED_CUSTOMER_RATING);
    }

    @Test
    @Transactional
    void getAllHkjOrdersByCustomerRatingIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where customerRating in
        defaultHkjOrderFiltering(
            "customerRating.in=" + DEFAULT_CUSTOMER_RATING + "," + UPDATED_CUSTOMER_RATING,
            "customerRating.in=" + UPDATED_CUSTOMER_RATING
        );
    }

    @Test
    @Transactional
    void getAllHkjOrdersByCustomerRatingIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where customerRating is not null
        defaultHkjOrderFiltering("customerRating.specified=true", "customerRating.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjOrdersByCustomerRatingIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where customerRating is greater than or equal to
        defaultHkjOrderFiltering(
            "customerRating.greaterThanOrEqual=" + DEFAULT_CUSTOMER_RATING,
            "customerRating.greaterThanOrEqual=" + (DEFAULT_CUSTOMER_RATING + 1)
        );
    }

    @Test
    @Transactional
    void getAllHkjOrdersByCustomerRatingIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where customerRating is less than or equal to
        defaultHkjOrderFiltering(
            "customerRating.lessThanOrEqual=" + DEFAULT_CUSTOMER_RATING,
            "customerRating.lessThanOrEqual=" + SMALLER_CUSTOMER_RATING
        );
    }

    @Test
    @Transactional
    void getAllHkjOrdersByCustomerRatingIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where customerRating is less than
        defaultHkjOrderFiltering(
            "customerRating.lessThan=" + (DEFAULT_CUSTOMER_RATING + 1),
            "customerRating.lessThan=" + DEFAULT_CUSTOMER_RATING
        );
    }

    @Test
    @Transactional
    void getAllHkjOrdersByCustomerRatingIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where customerRating is greater than
        defaultHkjOrderFiltering(
            "customerRating.greaterThan=" + SMALLER_CUSTOMER_RATING,
            "customerRating.greaterThan=" + DEFAULT_CUSTOMER_RATING
        );
    }

    @Test
    @Transactional
    void getAllHkjOrdersByTotalPriceIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where totalPrice equals to
        defaultHkjOrderFiltering("totalPrice.equals=" + DEFAULT_TOTAL_PRICE, "totalPrice.equals=" + UPDATED_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjOrdersByTotalPriceIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where totalPrice in
        defaultHkjOrderFiltering(
            "totalPrice.in=" + DEFAULT_TOTAL_PRICE + "," + UPDATED_TOTAL_PRICE,
            "totalPrice.in=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllHkjOrdersByTotalPriceIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where totalPrice is not null
        defaultHkjOrderFiltering("totalPrice.specified=true", "totalPrice.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjOrdersByTotalPriceIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where totalPrice is greater than or equal to
        defaultHkjOrderFiltering(
            "totalPrice.greaterThanOrEqual=" + DEFAULT_TOTAL_PRICE,
            "totalPrice.greaterThanOrEqual=" + UPDATED_TOTAL_PRICE
        );
    }

    @Test
    @Transactional
    void getAllHkjOrdersByTotalPriceIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where totalPrice is less than or equal to
        defaultHkjOrderFiltering("totalPrice.lessThanOrEqual=" + DEFAULT_TOTAL_PRICE, "totalPrice.lessThanOrEqual=" + SMALLER_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjOrdersByTotalPriceIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where totalPrice is less than
        defaultHkjOrderFiltering("totalPrice.lessThan=" + UPDATED_TOTAL_PRICE, "totalPrice.lessThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjOrdersByTotalPriceIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where totalPrice is greater than
        defaultHkjOrderFiltering("totalPrice.greaterThan=" + SMALLER_TOTAL_PRICE, "totalPrice.greaterThan=" + DEFAULT_TOTAL_PRICE);
    }

    @Test
    @Transactional
    void getAllHkjOrdersByDepositAmountIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where depositAmount equals to
        defaultHkjOrderFiltering("depositAmount.equals=" + DEFAULT_DEPOSIT_AMOUNT, "depositAmount.equals=" + UPDATED_DEPOSIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllHkjOrdersByDepositAmountIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where depositAmount in
        defaultHkjOrderFiltering(
            "depositAmount.in=" + DEFAULT_DEPOSIT_AMOUNT + "," + UPDATED_DEPOSIT_AMOUNT,
            "depositAmount.in=" + UPDATED_DEPOSIT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllHkjOrdersByDepositAmountIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where depositAmount is not null
        defaultHkjOrderFiltering("depositAmount.specified=true", "depositAmount.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjOrdersByDepositAmountIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where depositAmount is greater than or equal to
        defaultHkjOrderFiltering(
            "depositAmount.greaterThanOrEqual=" + DEFAULT_DEPOSIT_AMOUNT,
            "depositAmount.greaterThanOrEqual=" + UPDATED_DEPOSIT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllHkjOrdersByDepositAmountIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where depositAmount is less than or equal to
        defaultHkjOrderFiltering(
            "depositAmount.lessThanOrEqual=" + DEFAULT_DEPOSIT_AMOUNT,
            "depositAmount.lessThanOrEqual=" + SMALLER_DEPOSIT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllHkjOrdersByDepositAmountIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where depositAmount is less than
        defaultHkjOrderFiltering("depositAmount.lessThan=" + UPDATED_DEPOSIT_AMOUNT, "depositAmount.lessThan=" + DEFAULT_DEPOSIT_AMOUNT);
    }

    @Test
    @Transactional
    void getAllHkjOrdersByDepositAmountIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where depositAmount is greater than
        defaultHkjOrderFiltering(
            "depositAmount.greaterThan=" + SMALLER_DEPOSIT_AMOUNT,
            "depositAmount.greaterThan=" + DEFAULT_DEPOSIT_AMOUNT
        );
    }

    @Test
    @Transactional
    void getAllHkjOrdersByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where notes equals to
        defaultHkjOrderFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjOrdersByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where notes in
        defaultHkjOrderFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjOrdersByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where notes is not null
        defaultHkjOrderFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjOrdersByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where notes contains
        defaultHkjOrderFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjOrdersByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where notes does not contain
        defaultHkjOrderFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjOrdersByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where isDeleted equals to
        defaultHkjOrderFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjOrdersByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where isDeleted in
        defaultHkjOrderFiltering("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED, "isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjOrdersByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        // Get all the hkjOrderList where isDeleted is not null
        defaultHkjOrderFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjOrdersByProjectIsEqualToSomething() throws Exception {
        HkjProject project;
        if (TestUtil.findAll(em, HkjProject.class).isEmpty()) {
            hkjOrderRepository.saveAndFlush(hkjOrder);
            project = HkjProjectResourceIT.createEntity();
        } else {
            project = TestUtil.findAll(em, HkjProject.class).get(0);
        }
        em.persist(project);
        em.flush();
        hkjOrder.setProject(project);
        hkjOrderRepository.saveAndFlush(hkjOrder);
        Long projectId = project.getId();
        // Get all the hkjOrderList where project equals to projectId
        defaultHkjOrderShouldBeFound("projectId.equals=" + projectId);

        // Get all the hkjOrderList where project equals to (projectId + 1)
        defaultHkjOrderShouldNotBeFound("projectId.equals=" + (projectId + 1));
    }

    @Test
    @Transactional
    void getAllHkjOrdersByCustomerIsEqualToSomething() throws Exception {
        UserExtra customer;
        if (TestUtil.findAll(em, UserExtra.class).isEmpty()) {
            hkjOrderRepository.saveAndFlush(hkjOrder);
            customer = UserExtraResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, UserExtra.class).get(0);
        }
        em.persist(customer);
        em.flush();
        hkjOrder.setCustomer(customer);
        hkjOrderRepository.saveAndFlush(hkjOrder);
        Long customerId = customer.getId();
        // Get all the hkjOrderList where customer equals to customerId
        defaultHkjOrderShouldBeFound("customerId.equals=" + customerId);

        // Get all the hkjOrderList where customer equals to (customerId + 1)
        defaultHkjOrderShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllHkjOrdersByJewelryIsEqualToSomething() throws Exception {
        HkjJewelryModel jewelry;
        if (TestUtil.findAll(em, HkjJewelryModel.class).isEmpty()) {
            hkjOrderRepository.saveAndFlush(hkjOrder);
            jewelry = HkjJewelryModelResourceIT.createEntity();
        } else {
            jewelry = TestUtil.findAll(em, HkjJewelryModel.class).get(0);
        }
        em.persist(jewelry);
        em.flush();
        hkjOrder.setJewelry(jewelry);
        hkjOrderRepository.saveAndFlush(hkjOrder);
        Long jewelryId = jewelry.getId();
        // Get all the hkjOrderList where jewelry equals to jewelryId
        defaultHkjOrderShouldBeFound("jewelryId.equals=" + jewelryId);

        // Get all the hkjOrderList where jewelry equals to (jewelryId + 1)
        defaultHkjOrderShouldNotBeFound("jewelryId.equals=" + (jewelryId + 1));
    }

    private void defaultHkjOrderFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjOrderShouldBeFound(shouldBeFound);
        defaultHkjOrderShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjOrderShouldBeFound(String filter) throws Exception {
        restHkjOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjOrder.getId().intValue())))
            .andExpect(jsonPath("$.[*].orderDate").value(hasItem(DEFAULT_ORDER_DATE.toString())))
            .andExpect(jsonPath("$.[*].expectedDeliveryDate").value(hasItem(DEFAULT_EXPECTED_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].actualDeliveryDate").value(hasItem(DEFAULT_ACTUAL_DELIVERY_DATE.toString())))
            .andExpect(jsonPath("$.[*].specialRequests").value(hasItem(DEFAULT_SPECIAL_REQUESTS)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].customerRating").value(hasItem(DEFAULT_CUSTOMER_RATING)))
            .andExpect(jsonPath("$.[*].totalPrice").value(hasItem(sameNumber(DEFAULT_TOTAL_PRICE))))
            .andExpect(jsonPath("$.[*].depositAmount").value(hasItem(sameNumber(DEFAULT_DEPOSIT_AMOUNT))))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restHkjOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjOrderShouldNotBeFound(String filter) throws Exception {
        restHkjOrderMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjOrderMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjOrder() throws Exception {
        // Get the hkjOrder
        restHkjOrderMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjOrder() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjOrder
        HkjOrder updatedHkjOrder = hkjOrderRepository.findById(hkjOrder.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjOrder are not directly saved in db
        em.detach(updatedHkjOrder);
        updatedHkjOrder
            .orderDate(UPDATED_ORDER_DATE)
            .expectedDeliveryDate(UPDATED_EXPECTED_DELIVERY_DATE)
            .actualDeliveryDate(UPDATED_ACTUAL_DELIVERY_DATE)
            .specialRequests(UPDATED_SPECIAL_REQUESTS)
            .status(UPDATED_STATUS)
            .customerRating(UPDATED_CUSTOMER_RATING)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .depositAmount(UPDATED_DEPOSIT_AMOUNT)
            .notes(UPDATED_NOTES)
            .isDeleted(UPDATED_IS_DELETED);
        HkjOrderDTO hkjOrderDTO = hkjOrderMapper.toDto(updatedHkjOrder);

        restHkjOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjOrderDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjOrderDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjOrderToMatchAllProperties(updatedHkjOrder);
    }

    @Test
    @Transactional
    void putNonExistingHkjOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjOrder.setId(longCount.incrementAndGet());

        // Create the HkjOrder
        HkjOrderDTO hkjOrderDTO = hkjOrderMapper.toDto(hkjOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjOrderDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjOrder.setId(longCount.incrementAndGet());

        // Create the HkjOrder
        HkjOrderDTO hkjOrderDTO = hkjOrderMapper.toDto(hkjOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjOrderMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjOrder.setId(longCount.incrementAndGet());

        // Create the HkjOrder
        HkjOrderDTO hkjOrderDTO = hkjOrderMapper.toDto(hkjOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjOrderMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjOrderDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjOrderWithPatch() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjOrder using partial update
        HkjOrder partialUpdatedHkjOrder = new HkjOrder();
        partialUpdatedHkjOrder.setId(hkjOrder.getId());

        partialUpdatedHkjOrder
            .orderDate(UPDATED_ORDER_DATE)
            .actualDeliveryDate(UPDATED_ACTUAL_DELIVERY_DATE)
            .specialRequests(UPDATED_SPECIAL_REQUESTS)
            .status(UPDATED_STATUS)
            .customerRating(UPDATED_CUSTOMER_RATING)
            .notes(UPDATED_NOTES);

        restHkjOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjOrder.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjOrder))
            )
            .andExpect(status().isOk());

        // Validate the HkjOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjOrderUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedHkjOrder, hkjOrder), getPersistedHkjOrder(hkjOrder));
    }

    @Test
    @Transactional
    void fullUpdateHkjOrderWithPatch() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjOrder using partial update
        HkjOrder partialUpdatedHkjOrder = new HkjOrder();
        partialUpdatedHkjOrder.setId(hkjOrder.getId());

        partialUpdatedHkjOrder
            .orderDate(UPDATED_ORDER_DATE)
            .expectedDeliveryDate(UPDATED_EXPECTED_DELIVERY_DATE)
            .actualDeliveryDate(UPDATED_ACTUAL_DELIVERY_DATE)
            .specialRequests(UPDATED_SPECIAL_REQUESTS)
            .status(UPDATED_STATUS)
            .customerRating(UPDATED_CUSTOMER_RATING)
            .totalPrice(UPDATED_TOTAL_PRICE)
            .depositAmount(UPDATED_DEPOSIT_AMOUNT)
            .notes(UPDATED_NOTES)
            .isDeleted(UPDATED_IS_DELETED);

        restHkjOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjOrder.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjOrder))
            )
            .andExpect(status().isOk());

        // Validate the HkjOrder in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjOrderUpdatableFieldsEquals(partialUpdatedHkjOrder, getPersistedHkjOrder(partialUpdatedHkjOrder));
    }

    @Test
    @Transactional
    void patchNonExistingHkjOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjOrder.setId(longCount.incrementAndGet());

        // Create the HkjOrder
        HkjOrderDTO hkjOrderDTO = hkjOrderMapper.toDto(hkjOrder);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjOrderDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjOrder.setId(longCount.incrementAndGet());

        // Create the HkjOrder
        HkjOrderDTO hkjOrderDTO = hkjOrderMapper.toDto(hkjOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjOrderMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjOrderDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjOrder() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjOrder.setId(longCount.incrementAndGet());

        // Create the HkjOrder
        HkjOrderDTO hkjOrderDTO = hkjOrderMapper.toDto(hkjOrder);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjOrderMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hkjOrderDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjOrder in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjOrder() throws Exception {
        // Initialize the database
        insertedHkjOrder = hkjOrderRepository.saveAndFlush(hkjOrder);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjOrder
        restHkjOrderMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjOrder.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjOrderRepository.count();
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

    protected HkjOrder getPersistedHkjOrder(HkjOrder hkjOrder) {
        return hkjOrderRepository.findById(hkjOrder.getId()).orElseThrow();
    }

    protected void assertPersistedHkjOrderToMatchAllProperties(HkjOrder expectedHkjOrder) {
        assertHkjOrderAllPropertiesEquals(expectedHkjOrder, getPersistedHkjOrder(expectedHkjOrder));
    }

    protected void assertPersistedHkjOrderToMatchUpdatableProperties(HkjOrder expectedHkjOrder) {
        assertHkjOrderAllUpdatablePropertiesEquals(expectedHkjOrder, getPersistedHkjOrder(expectedHkjOrder));
    }
}
