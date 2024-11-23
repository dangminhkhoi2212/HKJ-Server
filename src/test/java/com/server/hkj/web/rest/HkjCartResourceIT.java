package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjCartAsserts.*;
import static com.server.hkj.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.hkj.IntegrationTest;
import com.server.hkj.domain.HkjCart;
import com.server.hkj.domain.HkjJewelryModel;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.repository.HkjCartRepository;
import com.server.hkj.service.dto.HkjCartDTO;
import com.server.hkj.service.mapper.HkjCartMapper;
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
 * Integration tests for the {@link HkjCartResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjCartResourceIT {

    private static final Integer DEFAULT_QUANTITY = 1;
    private static final Integer UPDATED_QUANTITY = 2;
    private static final Integer SMALLER_QUANTITY = 1 - 1;

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/hkj-carts";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjCartRepository hkjCartRepository;

    @Autowired
    private HkjCartMapper hkjCartMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjCartMockMvc;

    private HkjCart hkjCart;

    private HkjCart insertedHkjCart;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjCart createEntity() {
        return new HkjCart().quantity(DEFAULT_QUANTITY).isDeleted(DEFAULT_IS_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjCart createUpdatedEntity() {
        return new HkjCart().quantity(UPDATED_QUANTITY).isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    public void initTest() {
        hkjCart = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjCart != null) {
            hkjCartRepository.delete(insertedHkjCart);
            insertedHkjCart = null;
        }
    }

    @Test
    @Transactional
    void createHkjCart() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjCart
        HkjCartDTO hkjCartDTO = hkjCartMapper.toDto(hkjCart);
        var returnedHkjCartDTO = om.readValue(
            restHkjCartMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjCartDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjCartDTO.class
        );

        // Validate the HkjCart in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjCart = hkjCartMapper.toEntity(returnedHkjCartDTO);
        assertHkjCartUpdatableFieldsEquals(returnedHkjCart, getPersistedHkjCart(returnedHkjCart));

        insertedHkjCart = returnedHkjCart;
    }

    @Test
    @Transactional
    void createHkjCartWithExistingId() throws Exception {
        // Create the HkjCart with an existing ID
        hkjCart.setId(1L);
        HkjCartDTO hkjCartDTO = hkjCartMapper.toDto(hkjCart);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjCartMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjCartDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HkjCart in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHkjCarts() throws Exception {
        // Initialize the database
        insertedHkjCart = hkjCartRepository.saveAndFlush(hkjCart);

        // Get all the hkjCartList
        restHkjCartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjCart.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getHkjCart() throws Exception {
        // Initialize the database
        insertedHkjCart = hkjCartRepository.saveAndFlush(hkjCart);

        // Get the hkjCart
        restHkjCartMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjCart.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjCart.getId().intValue()))
            .andExpect(jsonPath("$.quantity").value(DEFAULT_QUANTITY))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getHkjCartsByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjCart = hkjCartRepository.saveAndFlush(hkjCart);

        Long id = hkjCart.getId();

        defaultHkjCartFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjCartFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjCartFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjCartsByQuantityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjCart = hkjCartRepository.saveAndFlush(hkjCart);

        // Get all the hkjCartList where quantity equals to
        defaultHkjCartFiltering("quantity.equals=" + DEFAULT_QUANTITY, "quantity.equals=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjCartsByQuantityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjCart = hkjCartRepository.saveAndFlush(hkjCart);

        // Get all the hkjCartList where quantity in
        defaultHkjCartFiltering("quantity.in=" + DEFAULT_QUANTITY + "," + UPDATED_QUANTITY, "quantity.in=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjCartsByQuantityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjCart = hkjCartRepository.saveAndFlush(hkjCart);

        // Get all the hkjCartList where quantity is not null
        defaultHkjCartFiltering("quantity.specified=true", "quantity.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjCartsByQuantityIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjCart = hkjCartRepository.saveAndFlush(hkjCart);

        // Get all the hkjCartList where quantity is greater than or equal to
        defaultHkjCartFiltering("quantity.greaterThanOrEqual=" + DEFAULT_QUANTITY, "quantity.greaterThanOrEqual=" + UPDATED_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjCartsByQuantityIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjCart = hkjCartRepository.saveAndFlush(hkjCart);

        // Get all the hkjCartList where quantity is less than or equal to
        defaultHkjCartFiltering("quantity.lessThanOrEqual=" + DEFAULT_QUANTITY, "quantity.lessThanOrEqual=" + SMALLER_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjCartsByQuantityIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjCart = hkjCartRepository.saveAndFlush(hkjCart);

        // Get all the hkjCartList where quantity is less than
        defaultHkjCartFiltering("quantity.lessThan=" + UPDATED_QUANTITY, "quantity.lessThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjCartsByQuantityIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjCart = hkjCartRepository.saveAndFlush(hkjCart);

        // Get all the hkjCartList where quantity is greater than
        defaultHkjCartFiltering("quantity.greaterThan=" + SMALLER_QUANTITY, "quantity.greaterThan=" + DEFAULT_QUANTITY);
    }

    @Test
    @Transactional
    void getAllHkjCartsByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjCart = hkjCartRepository.saveAndFlush(hkjCart);

        // Get all the hkjCartList where isDeleted equals to
        defaultHkjCartFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjCartsByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjCart = hkjCartRepository.saveAndFlush(hkjCart);

        // Get all the hkjCartList where isDeleted in
        defaultHkjCartFiltering("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED, "isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjCartsByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjCart = hkjCartRepository.saveAndFlush(hkjCart);

        // Get all the hkjCartList where isDeleted is not null
        defaultHkjCartFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjCartsByCustomerIsEqualToSomething() throws Exception {
        UserExtra customer;
        if (TestUtil.findAll(em, UserExtra.class).isEmpty()) {
            hkjCartRepository.saveAndFlush(hkjCart);
            customer = UserExtraResourceIT.createEntity();
        } else {
            customer = TestUtil.findAll(em, UserExtra.class).get(0);
        }
        em.persist(customer);
        em.flush();
        hkjCart.setCustomer(customer);
        hkjCartRepository.saveAndFlush(hkjCart);
        Long customerId = customer.getId();
        // Get all the hkjCartList where customer equals to customerId
        defaultHkjCartShouldBeFound("customerId.equals=" + customerId);

        // Get all the hkjCartList where customer equals to (customerId + 1)
        defaultHkjCartShouldNotBeFound("customerId.equals=" + (customerId + 1));
    }

    @Test
    @Transactional
    void getAllHkjCartsByProductIsEqualToSomething() throws Exception {
        HkjJewelryModel product;
        if (TestUtil.findAll(em, HkjJewelryModel.class).isEmpty()) {
            hkjCartRepository.saveAndFlush(hkjCart);
            product = HkjJewelryModelResourceIT.createEntity();
        } else {
            product = TestUtil.findAll(em, HkjJewelryModel.class).get(0);
        }
        em.persist(product);
        em.flush();
        hkjCart.setProduct(product);
        hkjCartRepository.saveAndFlush(hkjCart);
        Long productId = product.getId();
        // Get all the hkjCartList where product equals to productId
        defaultHkjCartShouldBeFound("productId.equals=" + productId);

        // Get all the hkjCartList where product equals to (productId + 1)
        defaultHkjCartShouldNotBeFound("productId.equals=" + (productId + 1));
    }

    private void defaultHkjCartFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjCartShouldBeFound(shouldBeFound);
        defaultHkjCartShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjCartShouldBeFound(String filter) throws Exception {
        restHkjCartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjCart.getId().intValue())))
            .andExpect(jsonPath("$.[*].quantity").value(hasItem(DEFAULT_QUANTITY)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restHkjCartMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjCartShouldNotBeFound(String filter) throws Exception {
        restHkjCartMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjCartMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjCart() throws Exception {
        // Get the hkjCart
        restHkjCartMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjCart() throws Exception {
        // Initialize the database
        insertedHkjCart = hkjCartRepository.saveAndFlush(hkjCart);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjCart
        HkjCart updatedHkjCart = hkjCartRepository.findById(hkjCart.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjCart are not directly saved in db
        em.detach(updatedHkjCart);
        updatedHkjCart.quantity(UPDATED_QUANTITY).isDeleted(UPDATED_IS_DELETED);
        HkjCartDTO hkjCartDTO = hkjCartMapper.toDto(updatedHkjCart);

        restHkjCartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjCartDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjCartDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjCart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjCartToMatchAllProperties(updatedHkjCart);
    }

    @Test
    @Transactional
    void putNonExistingHkjCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjCart.setId(longCount.incrementAndGet());

        // Create the HkjCart
        HkjCartDTO hkjCartDTO = hkjCartMapper.toDto(hkjCart);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjCartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjCartDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjCartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjCart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjCart.setId(longCount.incrementAndGet());

        // Create the HkjCart
        HkjCartDTO hkjCartDTO = hkjCartMapper.toDto(hkjCart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjCartMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjCartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjCart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjCart.setId(longCount.incrementAndGet());

        // Create the HkjCart
        HkjCartDTO hkjCartDTO = hkjCartMapper.toDto(hkjCart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjCartMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjCartDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjCart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjCartWithPatch() throws Exception {
        // Initialize the database
        insertedHkjCart = hkjCartRepository.saveAndFlush(hkjCart);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjCart using partial update
        HkjCart partialUpdatedHkjCart = new HkjCart();
        partialUpdatedHkjCart.setId(hkjCart.getId());

        restHkjCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjCart.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjCart))
            )
            .andExpect(status().isOk());

        // Validate the HkjCart in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjCartUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedHkjCart, hkjCart), getPersistedHkjCart(hkjCart));
    }

    @Test
    @Transactional
    void fullUpdateHkjCartWithPatch() throws Exception {
        // Initialize the database
        insertedHkjCart = hkjCartRepository.saveAndFlush(hkjCart);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjCart using partial update
        HkjCart partialUpdatedHkjCart = new HkjCart();
        partialUpdatedHkjCart.setId(hkjCart.getId());

        partialUpdatedHkjCart.quantity(UPDATED_QUANTITY).isDeleted(UPDATED_IS_DELETED);

        restHkjCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjCart.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjCart))
            )
            .andExpect(status().isOk());

        // Validate the HkjCart in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjCartUpdatableFieldsEquals(partialUpdatedHkjCart, getPersistedHkjCart(partialUpdatedHkjCart));
    }

    @Test
    @Transactional
    void patchNonExistingHkjCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjCart.setId(longCount.incrementAndGet());

        // Create the HkjCart
        HkjCartDTO hkjCartDTO = hkjCartMapper.toDto(hkjCart);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjCartDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjCartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjCart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjCart.setId(longCount.incrementAndGet());

        // Create the HkjCart
        HkjCartDTO hkjCartDTO = hkjCartMapper.toDto(hkjCart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjCartMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjCartDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjCart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjCart() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjCart.setId(longCount.incrementAndGet());

        // Create the HkjCart
        HkjCartDTO hkjCartDTO = hkjCartMapper.toDto(hkjCart);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjCartMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hkjCartDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjCart in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjCart() throws Exception {
        // Initialize the database
        insertedHkjCart = hkjCartRepository.saveAndFlush(hkjCart);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjCart
        restHkjCartMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjCart.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjCartRepository.count();
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

    protected HkjCart getPersistedHkjCart(HkjCart hkjCart) {
        return hkjCartRepository.findById(hkjCart.getId()).orElseThrow();
    }

    protected void assertPersistedHkjCartToMatchAllProperties(HkjCart expectedHkjCart) {
        assertHkjCartAllPropertiesEquals(expectedHkjCart, getPersistedHkjCart(expectedHkjCart));
    }

    protected void assertPersistedHkjCartToMatchUpdatableProperties(HkjCart expectedHkjCart) {
        assertHkjCartAllUpdatablePropertiesEquals(expectedHkjCart, getPersistedHkjCart(expectedHkjCart));
    }
}
