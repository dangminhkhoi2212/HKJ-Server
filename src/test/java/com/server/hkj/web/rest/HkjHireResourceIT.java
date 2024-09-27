package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjHireAsserts.*;
import static com.server.hkj.web.rest.TestUtil.createUpdateProxyForBean;
import static com.server.hkj.web.rest.TestUtil.sameNumber;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.hkj.IntegrationTest;
import com.server.hkj.domain.HkjHire;
import com.server.hkj.domain.HkjPosition;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.repository.HkjHireRepository;
import com.server.hkj.service.dto.HkjHireDTO;
import com.server.hkj.service.mapper.HkjHireMapper;
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
 * Integration tests for the {@link HkjHireResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjHireResourceIT {

    private static final Instant DEFAULT_BEGIN_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_BEGIN_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final BigDecimal DEFAULT_BEGIN_SALARY = new BigDecimal(1);
    private static final BigDecimal UPDATED_BEGIN_SALARY = new BigDecimal(2);
    private static final BigDecimal SMALLER_BEGIN_SALARY = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/hkj-hires";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjHireRepository hkjHireRepository;

    @Autowired
    private HkjHireMapper hkjHireMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjHireMockMvc;

    private HkjHire hkjHire;

    private HkjHire insertedHkjHire;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjHire createEntity(EntityManager em) {
        HkjHire hkjHire = new HkjHire()
            .beginDate(DEFAULT_BEGIN_DATE)
            .endDate(DEFAULT_END_DATE)
            .beginSalary(DEFAULT_BEGIN_SALARY)
            .isDeleted(DEFAULT_IS_DELETED);
        return hkjHire;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjHire createUpdatedEntity(EntityManager em) {
        HkjHire hkjHire = new HkjHire()
            .beginDate(UPDATED_BEGIN_DATE)
            .endDate(UPDATED_END_DATE)
            .beginSalary(UPDATED_BEGIN_SALARY)
            .isDeleted(UPDATED_IS_DELETED);
        return hkjHire;
    }

    @BeforeEach
    public void initTest() {
        hkjHire = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjHire != null) {
            hkjHireRepository.delete(insertedHkjHire);
            insertedHkjHire = null;
        }
    }

    @Test
    @Transactional
    void createHkjHire() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjHire
        HkjHireDTO hkjHireDTO = hkjHireMapper.toDto(hkjHire);
        var returnedHkjHireDTO = om.readValue(
            restHkjHireMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjHireDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjHireDTO.class
        );

        // Validate the HkjHire in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjHire = hkjHireMapper.toEntity(returnedHkjHireDTO);
        assertHkjHireUpdatableFieldsEquals(returnedHkjHire, getPersistedHkjHire(returnedHkjHire));

        insertedHkjHire = returnedHkjHire;
    }

    @Test
    @Transactional
    void createHkjHireWithExistingId() throws Exception {
        // Create the HkjHire with an existing ID
        hkjHire.setId(1L);
        HkjHireDTO hkjHireDTO = hkjHireMapper.toDto(hkjHire);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjHireMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjHireDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HkjHire in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkBeginDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjHire.setBeginDate(null);

        // Create the HkjHire, which fails.
        HkjHireDTO hkjHireDTO = hkjHireMapper.toDto(hkjHire);

        restHkjHireMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjHireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkEndDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjHire.setEndDate(null);

        // Create the HkjHire, which fails.
        HkjHireDTO hkjHireDTO = hkjHireMapper.toDto(hkjHire);

        restHkjHireMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjHireDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHkjHires() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        // Get all the hkjHireList
        restHkjHireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjHire.getId().intValue())))
            .andExpect(jsonPath("$.[*].beginDate").value(hasItem(DEFAULT_BEGIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].beginSalary").value(hasItem(sameNumber(DEFAULT_BEGIN_SALARY))))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getHkjHire() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        // Get the hkjHire
        restHkjHireMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjHire.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjHire.getId().intValue()))
            .andExpect(jsonPath("$.beginDate").value(DEFAULT_BEGIN_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.beginSalary").value(sameNumber(DEFAULT_BEGIN_SALARY)))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getHkjHiresByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        Long id = hkjHire.getId();

        defaultHkjHireFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjHireFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjHireFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjHiresByBeginDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        // Get all the hkjHireList where beginDate equals to
        defaultHkjHireFiltering("beginDate.equals=" + DEFAULT_BEGIN_DATE, "beginDate.equals=" + UPDATED_BEGIN_DATE);
    }

    @Test
    @Transactional
    void getAllHkjHiresByBeginDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        // Get all the hkjHireList where beginDate in
        defaultHkjHireFiltering("beginDate.in=" + DEFAULT_BEGIN_DATE + "," + UPDATED_BEGIN_DATE, "beginDate.in=" + UPDATED_BEGIN_DATE);
    }

    @Test
    @Transactional
    void getAllHkjHiresByBeginDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        // Get all the hkjHireList where beginDate is not null
        defaultHkjHireFiltering("beginDate.specified=true", "beginDate.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjHiresByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        // Get all the hkjHireList where endDate equals to
        defaultHkjHireFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllHkjHiresByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        // Get all the hkjHireList where endDate in
        defaultHkjHireFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllHkjHiresByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        // Get all the hkjHireList where endDate is not null
        defaultHkjHireFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjHiresByBeginSalaryIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        // Get all the hkjHireList where beginSalary equals to
        defaultHkjHireFiltering("beginSalary.equals=" + DEFAULT_BEGIN_SALARY, "beginSalary.equals=" + UPDATED_BEGIN_SALARY);
    }

    @Test
    @Transactional
    void getAllHkjHiresByBeginSalaryIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        // Get all the hkjHireList where beginSalary in
        defaultHkjHireFiltering(
            "beginSalary.in=" + DEFAULT_BEGIN_SALARY + "," + UPDATED_BEGIN_SALARY,
            "beginSalary.in=" + UPDATED_BEGIN_SALARY
        );
    }

    @Test
    @Transactional
    void getAllHkjHiresByBeginSalaryIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        // Get all the hkjHireList where beginSalary is not null
        defaultHkjHireFiltering("beginSalary.specified=true", "beginSalary.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjHiresByBeginSalaryIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        // Get all the hkjHireList where beginSalary is greater than or equal to
        defaultHkjHireFiltering(
            "beginSalary.greaterThanOrEqual=" + DEFAULT_BEGIN_SALARY,
            "beginSalary.greaterThanOrEqual=" + UPDATED_BEGIN_SALARY
        );
    }

    @Test
    @Transactional
    void getAllHkjHiresByBeginSalaryIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        // Get all the hkjHireList where beginSalary is less than or equal to
        defaultHkjHireFiltering(
            "beginSalary.lessThanOrEqual=" + DEFAULT_BEGIN_SALARY,
            "beginSalary.lessThanOrEqual=" + SMALLER_BEGIN_SALARY
        );
    }

    @Test
    @Transactional
    void getAllHkjHiresByBeginSalaryIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        // Get all the hkjHireList where beginSalary is less than
        defaultHkjHireFiltering("beginSalary.lessThan=" + UPDATED_BEGIN_SALARY, "beginSalary.lessThan=" + DEFAULT_BEGIN_SALARY);
    }

    @Test
    @Transactional
    void getAllHkjHiresByBeginSalaryIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        // Get all the hkjHireList where beginSalary is greater than
        defaultHkjHireFiltering("beginSalary.greaterThan=" + SMALLER_BEGIN_SALARY, "beginSalary.greaterThan=" + DEFAULT_BEGIN_SALARY);
    }

    @Test
    @Transactional
    void getAllHkjHiresByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        // Get all the hkjHireList where isDeleted equals to
        defaultHkjHireFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjHiresByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        // Get all the hkjHireList where isDeleted in
        defaultHkjHireFiltering("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED, "isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjHiresByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        // Get all the hkjHireList where isDeleted is not null
        defaultHkjHireFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjHiresByPositionIsEqualToSomething() throws Exception {
        HkjPosition position;
        if (TestUtil.findAll(em, HkjPosition.class).isEmpty()) {
            hkjHireRepository.saveAndFlush(hkjHire);
            position = HkjPositionResourceIT.createEntity(em);
        } else {
            position = TestUtil.findAll(em, HkjPosition.class).get(0);
        }
        em.persist(position);
        em.flush();
        hkjHire.setPosition(position);
        hkjHireRepository.saveAndFlush(hkjHire);
        Long positionId = position.getId();
        // Get all the hkjHireList where position equals to positionId
        defaultHkjHireShouldBeFound("positionId.equals=" + positionId);

        // Get all the hkjHireList where position equals to (positionId + 1)
        defaultHkjHireShouldNotBeFound("positionId.equals=" + (positionId + 1));
    }

    @Test
    @Transactional
    void getAllHkjHiresByEmployeeIsEqualToSomething() throws Exception {
        UserExtra employee;
        if (TestUtil.findAll(em, UserExtra.class).isEmpty()) {
            hkjHireRepository.saveAndFlush(hkjHire);
            employee = UserExtraResourceIT.createEntity(em);
        } else {
            employee = TestUtil.findAll(em, UserExtra.class).get(0);
        }
        em.persist(employee);
        em.flush();
        hkjHire.setEmployee(employee);
        hkjHireRepository.saveAndFlush(hkjHire);
        Long employeeId = employee.getId();
        // Get all the hkjHireList where employee equals to employeeId
        defaultHkjHireShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the hkjHireList where employee equals to (employeeId + 1)
        defaultHkjHireShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    private void defaultHkjHireFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjHireShouldBeFound(shouldBeFound);
        defaultHkjHireShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjHireShouldBeFound(String filter) throws Exception {
        restHkjHireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjHire.getId().intValue())))
            .andExpect(jsonPath("$.[*].beginDate").value(hasItem(DEFAULT_BEGIN_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].beginSalary").value(hasItem(sameNumber(DEFAULT_BEGIN_SALARY))))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restHkjHireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjHireShouldNotBeFound(String filter) throws Exception {
        restHkjHireMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjHireMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjHire() throws Exception {
        // Get the hkjHire
        restHkjHireMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjHire() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjHire
        HkjHire updatedHkjHire = hkjHireRepository.findById(hkjHire.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjHire are not directly saved in db
        em.detach(updatedHkjHire);
        updatedHkjHire
            .beginDate(UPDATED_BEGIN_DATE)
            .endDate(UPDATED_END_DATE)
            .beginSalary(UPDATED_BEGIN_SALARY)
            .isDeleted(UPDATED_IS_DELETED);
        HkjHireDTO hkjHireDTO = hkjHireMapper.toDto(updatedHkjHire);

        restHkjHireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjHireDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjHireDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjHire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjHireToMatchAllProperties(updatedHkjHire);
    }

    @Test
    @Transactional
    void putNonExistingHkjHire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjHire.setId(longCount.incrementAndGet());

        // Create the HkjHire
        HkjHireDTO hkjHireDTO = hkjHireMapper.toDto(hkjHire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjHireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjHireDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjHireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjHire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjHire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjHire.setId(longCount.incrementAndGet());

        // Create the HkjHire
        HkjHireDTO hkjHireDTO = hkjHireMapper.toDto(hkjHire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjHireMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjHireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjHire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjHire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjHire.setId(longCount.incrementAndGet());

        // Create the HkjHire
        HkjHireDTO hkjHireDTO = hkjHireMapper.toDto(hkjHire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjHireMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjHireDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjHire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjHireWithPatch() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjHire using partial update
        HkjHire partialUpdatedHkjHire = new HkjHire();
        partialUpdatedHkjHire.setId(hkjHire.getId());

        partialUpdatedHkjHire.isDeleted(UPDATED_IS_DELETED);

        restHkjHireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjHire.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjHire))
            )
            .andExpect(status().isOk());

        // Validate the HkjHire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjHireUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedHkjHire, hkjHire), getPersistedHkjHire(hkjHire));
    }

    @Test
    @Transactional
    void fullUpdateHkjHireWithPatch() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjHire using partial update
        HkjHire partialUpdatedHkjHire = new HkjHire();
        partialUpdatedHkjHire.setId(hkjHire.getId());

        partialUpdatedHkjHire
            .beginDate(UPDATED_BEGIN_DATE)
            .endDate(UPDATED_END_DATE)
            .beginSalary(UPDATED_BEGIN_SALARY)
            .isDeleted(UPDATED_IS_DELETED);

        restHkjHireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjHire.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjHire))
            )
            .andExpect(status().isOk());

        // Validate the HkjHire in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjHireUpdatableFieldsEquals(partialUpdatedHkjHire, getPersistedHkjHire(partialUpdatedHkjHire));
    }

    @Test
    @Transactional
    void patchNonExistingHkjHire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjHire.setId(longCount.incrementAndGet());

        // Create the HkjHire
        HkjHireDTO hkjHireDTO = hkjHireMapper.toDto(hkjHire);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjHireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjHireDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjHireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjHire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjHire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjHire.setId(longCount.incrementAndGet());

        // Create the HkjHire
        HkjHireDTO hkjHireDTO = hkjHireMapper.toDto(hkjHire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjHireMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjHireDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjHire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjHire() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjHire.setId(longCount.incrementAndGet());

        // Create the HkjHire
        HkjHireDTO hkjHireDTO = hkjHireMapper.toDto(hkjHire);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjHireMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hkjHireDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjHire in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjHire() throws Exception {
        // Initialize the database
        insertedHkjHire = hkjHireRepository.saveAndFlush(hkjHire);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjHire
        restHkjHireMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjHire.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjHireRepository.count();
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

    protected HkjHire getPersistedHkjHire(HkjHire hkjHire) {
        return hkjHireRepository.findById(hkjHire.getId()).orElseThrow();
    }

    protected void assertPersistedHkjHireToMatchAllProperties(HkjHire expectedHkjHire) {
        assertHkjHireAllPropertiesEquals(expectedHkjHire, getPersistedHkjHire(expectedHkjHire));
    }

    protected void assertPersistedHkjHireToMatchUpdatableProperties(HkjHire expectedHkjHire) {
        assertHkjHireAllUpdatablePropertiesEquals(expectedHkjHire, getPersistedHkjHire(expectedHkjHire));
    }
}
