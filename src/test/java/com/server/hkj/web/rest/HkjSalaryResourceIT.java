package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjSalaryAsserts.*;
import static com.server.hkj.web.rest.TestUtil.createUpdateProxyForBean;
import static com.server.hkj.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.hkj.IntegrationTest;
import com.server.hkj.domain.HkjSalary;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.repository.HkjSalaryRepository;
import com.server.hkj.service.dto.HkjSalaryDTO;
import com.server.hkj.service.mapper.HkjSalaryMapper;
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
 * Integration tests for the {@link HkjSalaryResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjSalaryResourceIT {

    private static final BigDecimal DEFAULT_SALARY = new BigDecimal(1);
    private static final BigDecimal UPDATED_SALARY = new BigDecimal(2);
    private static final BigDecimal SMALLER_SALARY = new BigDecimal(1 - 1);

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Instant DEFAULT_PAY_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_PAY_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/hkj-salaries";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjSalaryRepository hkjSalaryRepository;

    @Autowired
    private HkjSalaryMapper hkjSalaryMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjSalaryMockMvc;

    private HkjSalary hkjSalary;

    private HkjSalary insertedHkjSalary;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjSalary createEntity() {
        return new HkjSalary().salary(DEFAULT_SALARY).notes(DEFAULT_NOTES).payDate(DEFAULT_PAY_DATE).isDeleted(DEFAULT_IS_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjSalary createUpdatedEntity() {
        return new HkjSalary().salary(UPDATED_SALARY).notes(UPDATED_NOTES).payDate(UPDATED_PAY_DATE).isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    public void initTest() {
        hkjSalary = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjSalary != null) {
            hkjSalaryRepository.delete(insertedHkjSalary);
            insertedHkjSalary = null;
        }
    }

    @Test
    @Transactional
    void createHkjSalary() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjSalary
        HkjSalaryDTO hkjSalaryDTO = hkjSalaryMapper.toDto(hkjSalary);
        var returnedHkjSalaryDTO = om.readValue(
            restHkjSalaryMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjSalaryDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjSalaryDTO.class
        );

        // Validate the HkjSalary in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjSalary = hkjSalaryMapper.toEntity(returnedHkjSalaryDTO);
        assertHkjSalaryUpdatableFieldsEquals(returnedHkjSalary, getPersistedHkjSalary(returnedHkjSalary));

        insertedHkjSalary = returnedHkjSalary;
    }

    @Test
    @Transactional
    void createHkjSalaryWithExistingId() throws Exception {
        // Create the HkjSalary with an existing ID
        hkjSalary.setId(1L);
        HkjSalaryDTO hkjSalaryDTO = hkjSalaryMapper.toDto(hkjSalary);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjSalaryMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjSalaryDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HkjSalary in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHkjSalaries() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        // Get all the hkjSalaryList
        restHkjSalaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjSalary.getId().intValue())))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(sameNumber(DEFAULT_SALARY))))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].payDate").value(hasItem(DEFAULT_PAY_DATE.toString())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getHkjSalary() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        // Get the hkjSalary
        restHkjSalaryMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjSalary.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjSalary.getId().intValue()))
            .andExpect(jsonPath("$.salary").value(sameNumber(DEFAULT_SALARY)))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.payDate").value(DEFAULT_PAY_DATE.toString()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getHkjSalariesByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        Long id = hkjSalary.getId();

        defaultHkjSalaryFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjSalaryFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjSalaryFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjSalariesBySalaryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        // Get all the hkjSalaryList where salary equals to
        defaultHkjSalaryFiltering("salary.equals=" + DEFAULT_SALARY, "salary.equals=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllHkjSalariesBySalaryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        // Get all the hkjSalaryList where salary in
        defaultHkjSalaryFiltering("salary.in=" + DEFAULT_SALARY + "," + UPDATED_SALARY, "salary.in=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllHkjSalariesBySalaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        // Get all the hkjSalaryList where salary is not null
        defaultHkjSalaryFiltering("salary.specified=true", "salary.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjSalariesBySalaryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        // Get all the hkjSalaryList where salary is greater than or equal to
        defaultHkjSalaryFiltering("salary.greaterThanOrEqual=" + DEFAULT_SALARY, "salary.greaterThanOrEqual=" + UPDATED_SALARY);
    }

    @Test
    @Transactional
    void getAllHkjSalariesBySalaryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        // Get all the hkjSalaryList where salary is less than or equal to
        defaultHkjSalaryFiltering("salary.lessThanOrEqual=" + DEFAULT_SALARY, "salary.lessThanOrEqual=" + SMALLER_SALARY);
    }

    @Test
    @Transactional
    void getAllHkjSalariesBySalaryIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        // Get all the hkjSalaryList where salary is less than
        defaultHkjSalaryFiltering("salary.lessThan=" + UPDATED_SALARY, "salary.lessThan=" + DEFAULT_SALARY);
    }

    @Test
    @Transactional
    void getAllHkjSalariesBySalaryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        // Get all the hkjSalaryList where salary is greater than
        defaultHkjSalaryFiltering("salary.greaterThan=" + SMALLER_SALARY, "salary.greaterThan=" + DEFAULT_SALARY);
    }

    @Test
    @Transactional
    void getAllHkjSalariesByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        // Get all the hkjSalaryList where notes equals to
        defaultHkjSalaryFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjSalariesByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        // Get all the hkjSalaryList where notes in
        defaultHkjSalaryFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjSalariesByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        // Get all the hkjSalaryList where notes is not null
        defaultHkjSalaryFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjSalariesByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        // Get all the hkjSalaryList where notes contains
        defaultHkjSalaryFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjSalariesByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        // Get all the hkjSalaryList where notes does not contain
        defaultHkjSalaryFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjSalariesByPayDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        // Get all the hkjSalaryList where payDate equals to
        defaultHkjSalaryFiltering("payDate.equals=" + DEFAULT_PAY_DATE, "payDate.equals=" + UPDATED_PAY_DATE);
    }

    @Test
    @Transactional
    void getAllHkjSalariesByPayDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        // Get all the hkjSalaryList where payDate in
        defaultHkjSalaryFiltering("payDate.in=" + DEFAULT_PAY_DATE + "," + UPDATED_PAY_DATE, "payDate.in=" + UPDATED_PAY_DATE);
    }

    @Test
    @Transactional
    void getAllHkjSalariesByPayDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        // Get all the hkjSalaryList where payDate is not null
        defaultHkjSalaryFiltering("payDate.specified=true", "payDate.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjSalariesByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        // Get all the hkjSalaryList where isDeleted equals to
        defaultHkjSalaryFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjSalariesByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        // Get all the hkjSalaryList where isDeleted in
        defaultHkjSalaryFiltering("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED, "isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjSalariesByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        // Get all the hkjSalaryList where isDeleted is not null
        defaultHkjSalaryFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjSalariesByEmployeeIsEqualToSomething() throws Exception {
        UserExtra employee;
        if (TestUtil.findAll(em, UserExtra.class).isEmpty()) {
            hkjSalaryRepository.saveAndFlush(hkjSalary);
            employee = UserExtraResourceIT.createEntity();
        } else {
            employee = TestUtil.findAll(em, UserExtra.class).get(0);
        }
        em.persist(employee);
        em.flush();
        hkjSalary.setEmployee(employee);
        hkjSalaryRepository.saveAndFlush(hkjSalary);
        Long employeeId = employee.getId();
        // Get all the hkjSalaryList where employee equals to employeeId
        defaultHkjSalaryShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the hkjSalaryList where employee equals to (employeeId + 1)
        defaultHkjSalaryShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    private void defaultHkjSalaryFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjSalaryShouldBeFound(shouldBeFound);
        defaultHkjSalaryShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjSalaryShouldBeFound(String filter) throws Exception {
        restHkjSalaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjSalary.getId().intValue())))
            .andExpect(jsonPath("$.[*].salary").value(hasItem(sameNumber(DEFAULT_SALARY))))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].payDate").value(hasItem(DEFAULT_PAY_DATE.toString())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restHkjSalaryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjSalaryShouldNotBeFound(String filter) throws Exception {
        restHkjSalaryMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjSalaryMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjSalary() throws Exception {
        // Get the hkjSalary
        restHkjSalaryMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjSalary() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjSalary
        HkjSalary updatedHkjSalary = hkjSalaryRepository.findById(hkjSalary.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjSalary are not directly saved in db
        em.detach(updatedHkjSalary);
        updatedHkjSalary.salary(UPDATED_SALARY).notes(UPDATED_NOTES).payDate(UPDATED_PAY_DATE).isDeleted(UPDATED_IS_DELETED);
        HkjSalaryDTO hkjSalaryDTO = hkjSalaryMapper.toDto(updatedHkjSalary);

        restHkjSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjSalaryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjSalaryDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjSalary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjSalaryToMatchAllProperties(updatedHkjSalary);
    }

    @Test
    @Transactional
    void putNonExistingHkjSalary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjSalary.setId(longCount.incrementAndGet());

        // Create the HkjSalary
        HkjSalaryDTO hkjSalaryDTO = hkjSalaryMapper.toDto(hkjSalary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjSalaryDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjSalary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjSalary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjSalary.setId(longCount.incrementAndGet());

        // Create the HkjSalary
        HkjSalaryDTO hkjSalaryDTO = hkjSalaryMapper.toDto(hkjSalary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjSalaryMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjSalary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjSalary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjSalary.setId(longCount.incrementAndGet());

        // Create the HkjSalary
        HkjSalaryDTO hkjSalaryDTO = hkjSalaryMapper.toDto(hkjSalary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjSalaryMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjSalaryDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjSalary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjSalaryWithPatch() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjSalary using partial update
        HkjSalary partialUpdatedHkjSalary = new HkjSalary();
        partialUpdatedHkjSalary.setId(hkjSalary.getId());

        partialUpdatedHkjSalary.notes(UPDATED_NOTES).isDeleted(UPDATED_IS_DELETED);

        restHkjSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjSalary.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjSalary))
            )
            .andExpect(status().isOk());

        // Validate the HkjSalary in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjSalaryUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHkjSalary, hkjSalary),
            getPersistedHkjSalary(hkjSalary)
        );
    }

    @Test
    @Transactional
    void fullUpdateHkjSalaryWithPatch() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjSalary using partial update
        HkjSalary partialUpdatedHkjSalary = new HkjSalary();
        partialUpdatedHkjSalary.setId(hkjSalary.getId());

        partialUpdatedHkjSalary.salary(UPDATED_SALARY).notes(UPDATED_NOTES).payDate(UPDATED_PAY_DATE).isDeleted(UPDATED_IS_DELETED);

        restHkjSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjSalary.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjSalary))
            )
            .andExpect(status().isOk());

        // Validate the HkjSalary in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjSalaryUpdatableFieldsEquals(partialUpdatedHkjSalary, getPersistedHkjSalary(partialUpdatedHkjSalary));
    }

    @Test
    @Transactional
    void patchNonExistingHkjSalary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjSalary.setId(longCount.incrementAndGet());

        // Create the HkjSalary
        HkjSalaryDTO hkjSalaryDTO = hkjSalaryMapper.toDto(hkjSalary);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjSalaryDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjSalary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjSalary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjSalary.setId(longCount.incrementAndGet());

        // Create the HkjSalary
        HkjSalaryDTO hkjSalaryDTO = hkjSalaryMapper.toDto(hkjSalary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjSalaryDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjSalary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjSalary() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjSalary.setId(longCount.incrementAndGet());

        // Create the HkjSalary
        HkjSalaryDTO hkjSalaryDTO = hkjSalaryMapper.toDto(hkjSalary);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjSalaryMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hkjSalaryDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjSalary in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjSalary() throws Exception {
        // Initialize the database
        insertedHkjSalary = hkjSalaryRepository.saveAndFlush(hkjSalary);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjSalary
        restHkjSalaryMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjSalary.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjSalaryRepository.count();
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

    protected HkjSalary getPersistedHkjSalary(HkjSalary hkjSalary) {
        return hkjSalaryRepository.findById(hkjSalary.getId()).orElseThrow();
    }

    protected void assertPersistedHkjSalaryToMatchAllProperties(HkjSalary expectedHkjSalary) {
        assertHkjSalaryAllPropertiesEquals(expectedHkjSalary, getPersistedHkjSalary(expectedHkjSalary));
    }

    protected void assertPersistedHkjSalaryToMatchUpdatableProperties(HkjSalary expectedHkjSalary) {
        assertHkjSalaryAllUpdatablePropertiesEquals(expectedHkjSalary, getPersistedHkjSalary(expectedHkjSalary));
    }
}
