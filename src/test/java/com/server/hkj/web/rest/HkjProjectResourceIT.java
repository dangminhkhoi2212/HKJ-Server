package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjProjectAsserts.*;
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
import com.server.hkj.domain.HkjMaterial;
import com.server.hkj.domain.HkjProject;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.domain.enumeration.HkjOrderStatus;
import com.server.hkj.domain.enumeration.HkjPriority;
import com.server.hkj.repository.HkjProjectRepository;
import com.server.hkj.service.dto.HkjProjectDTO;
import com.server.hkj.service.mapper.HkjProjectMapper;
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
 * Integration tests for the {@link HkjProjectResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjProjectResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COVER_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_COVER_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPECT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPECT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final HkjOrderStatus DEFAULT_STATUS = HkjOrderStatus.NEW;
    private static final HkjOrderStatus UPDATED_STATUS = HkjOrderStatus.IN_PROCESS;

    private static final HkjPriority DEFAULT_PRIORITY = HkjPriority.LOW;
    private static final HkjPriority UPDATED_PRIORITY = HkjPriority.MEDIUM;

    private static final BigDecimal DEFAULT_ACTUAL_COST = new BigDecimal(1);
    private static final BigDecimal UPDATED_ACTUAL_COST = new BigDecimal(2);
    private static final BigDecimal SMALLER_ACTUAL_COST = new BigDecimal(1 - 1);

    private static final Boolean DEFAULT_QUALITY_CHECK = false;
    private static final Boolean UPDATED_QUALITY_CHECK = true;

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/hkj-projects";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjProjectRepository hkjProjectRepository;

    @Autowired
    private HkjProjectMapper hkjProjectMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjProjectMockMvc;

    private HkjProject hkjProject;

    private HkjProject insertedHkjProject;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjProject createEntity() {
        return new HkjProject()
            .name(DEFAULT_NAME)
            .coverImage(DEFAULT_COVER_IMAGE)
            .description(DEFAULT_DESCRIPTION)
            .startDate(DEFAULT_START_DATE)
            .expectDate(DEFAULT_EXPECT_DATE)
            .endDate(DEFAULT_END_DATE)
            .status(DEFAULT_STATUS)
            .priority(DEFAULT_PRIORITY)
            .actualCost(DEFAULT_ACTUAL_COST)
            .qualityCheck(DEFAULT_QUALITY_CHECK)
            .isDeleted(DEFAULT_IS_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjProject createUpdatedEntity() {
        return new HkjProject()
            .name(UPDATED_NAME)
            .coverImage(UPDATED_COVER_IMAGE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .expectDate(UPDATED_EXPECT_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .actualCost(UPDATED_ACTUAL_COST)
            .qualityCheck(UPDATED_QUALITY_CHECK)
            .isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    public void initTest() {
        hkjProject = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjProject != null) {
            hkjProjectRepository.delete(insertedHkjProject);
            insertedHkjProject = null;
        }
    }

    @Test
    @Transactional
    void createHkjProject() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjProject
        HkjProjectDTO hkjProjectDTO = hkjProjectMapper.toDto(hkjProject);
        var returnedHkjProjectDTO = om.readValue(
            restHkjProjectMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjProjectDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjProjectDTO.class
        );

        // Validate the HkjProject in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjProject = hkjProjectMapper.toEntity(returnedHkjProjectDTO);
        assertHkjProjectUpdatableFieldsEquals(returnedHkjProject, getPersistedHkjProject(returnedHkjProject));

        insertedHkjProject = returnedHkjProject;
    }

    @Test
    @Transactional
    void createHkjProjectWithExistingId() throws Exception {
        // Create the HkjProject with an existing ID
        hkjProject.setId(1L);
        HkjProjectDTO hkjProjectDTO = hkjProjectMapper.toDto(hkjProject);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjProjectMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjProjectDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HkjProject in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjProject.setName(null);

        // Create the HkjProject, which fails.
        HkjProjectDTO hkjProjectDTO = hkjProjectMapper.toDto(hkjProject);

        restHkjProjectMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjProjectDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStartDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjProject.setStartDate(null);

        // Create the HkjProject, which fails.
        HkjProjectDTO hkjProjectDTO = hkjProjectMapper.toDto(hkjProject);

        restHkjProjectMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjProjectDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjProject.setStatus(null);

        // Create the HkjProject, which fails.
        HkjProjectDTO hkjProjectDTO = hkjProjectMapper.toDto(hkjProject);

        restHkjProjectMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjProjectDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjProject.setPriority(null);

        // Create the HkjProject, which fails.
        HkjProjectDTO hkjProjectDTO = hkjProjectMapper.toDto(hkjProject);

        restHkjProjectMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjProjectDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHkjProjects() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList
        restHkjProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjProject.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].coverImage").value(hasItem(DEFAULT_COVER_IMAGE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].expectDate").value(hasItem(DEFAULT_EXPECT_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].actualCost").value(hasItem(sameNumber(DEFAULT_ACTUAL_COST))))
            .andExpect(jsonPath("$.[*].qualityCheck").value(hasItem(DEFAULT_QUALITY_CHECK.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getHkjProject() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get the hkjProject
        restHkjProjectMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjProject.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjProject.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.coverImage").value(DEFAULT_COVER_IMAGE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.expectDate").value(DEFAULT_EXPECT_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.actualCost").value(sameNumber(DEFAULT_ACTUAL_COST)))
            .andExpect(jsonPath("$.qualityCheck").value(DEFAULT_QUALITY_CHECK.booleanValue()))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getHkjProjectsByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        Long id = hkjProject.getId();

        defaultHkjProjectFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjProjectFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjProjectFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where name equals to
        defaultHkjProjectFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where name in
        defaultHkjProjectFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where name is not null
        defaultHkjProjectFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjProjectsByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where name contains
        defaultHkjProjectFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where name does not contain
        defaultHkjProjectFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByCoverImageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where coverImage equals to
        defaultHkjProjectFiltering("coverImage.equals=" + DEFAULT_COVER_IMAGE, "coverImage.equals=" + UPDATED_COVER_IMAGE);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByCoverImageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where coverImage in
        defaultHkjProjectFiltering(
            "coverImage.in=" + DEFAULT_COVER_IMAGE + "," + UPDATED_COVER_IMAGE,
            "coverImage.in=" + UPDATED_COVER_IMAGE
        );
    }

    @Test
    @Transactional
    void getAllHkjProjectsByCoverImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where coverImage is not null
        defaultHkjProjectFiltering("coverImage.specified=true", "coverImage.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjProjectsByCoverImageContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where coverImage contains
        defaultHkjProjectFiltering("coverImage.contains=" + DEFAULT_COVER_IMAGE, "coverImage.contains=" + UPDATED_COVER_IMAGE);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByCoverImageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where coverImage does not contain
        defaultHkjProjectFiltering("coverImage.doesNotContain=" + UPDATED_COVER_IMAGE, "coverImage.doesNotContain=" + DEFAULT_COVER_IMAGE);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where description equals to
        defaultHkjProjectFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where description in
        defaultHkjProjectFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllHkjProjectsByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where description is not null
        defaultHkjProjectFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjProjectsByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where description contains
        defaultHkjProjectFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where description does not contain
        defaultHkjProjectFiltering(
            "description.doesNotContain=" + UPDATED_DESCRIPTION,
            "description.doesNotContain=" + DEFAULT_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllHkjProjectsByStartDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where startDate equals to
        defaultHkjProjectFiltering("startDate.equals=" + DEFAULT_START_DATE, "startDate.equals=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByStartDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where startDate in
        defaultHkjProjectFiltering("startDate.in=" + DEFAULT_START_DATE + "," + UPDATED_START_DATE, "startDate.in=" + UPDATED_START_DATE);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByStartDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where startDate is not null
        defaultHkjProjectFiltering("startDate.specified=true", "startDate.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjProjectsByExpectDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where expectDate equals to
        defaultHkjProjectFiltering("expectDate.equals=" + DEFAULT_EXPECT_DATE, "expectDate.equals=" + UPDATED_EXPECT_DATE);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByExpectDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where expectDate in
        defaultHkjProjectFiltering(
            "expectDate.in=" + DEFAULT_EXPECT_DATE + "," + UPDATED_EXPECT_DATE,
            "expectDate.in=" + UPDATED_EXPECT_DATE
        );
    }

    @Test
    @Transactional
    void getAllHkjProjectsByExpectDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where expectDate is not null
        defaultHkjProjectFiltering("expectDate.specified=true", "expectDate.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjProjectsByEndDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where endDate equals to
        defaultHkjProjectFiltering("endDate.equals=" + DEFAULT_END_DATE, "endDate.equals=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByEndDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where endDate in
        defaultHkjProjectFiltering("endDate.in=" + DEFAULT_END_DATE + "," + UPDATED_END_DATE, "endDate.in=" + UPDATED_END_DATE);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByEndDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where endDate is not null
        defaultHkjProjectFiltering("endDate.specified=true", "endDate.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjProjectsByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where status equals to
        defaultHkjProjectFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where status in
        defaultHkjProjectFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where status is not null
        defaultHkjProjectFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjProjectsByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where priority equals to
        defaultHkjProjectFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where priority in
        defaultHkjProjectFiltering("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY, "priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where priority is not null
        defaultHkjProjectFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjProjectsByActualCostIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where actualCost equals to
        defaultHkjProjectFiltering("actualCost.equals=" + DEFAULT_ACTUAL_COST, "actualCost.equals=" + UPDATED_ACTUAL_COST);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByActualCostIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where actualCost in
        defaultHkjProjectFiltering(
            "actualCost.in=" + DEFAULT_ACTUAL_COST + "," + UPDATED_ACTUAL_COST,
            "actualCost.in=" + UPDATED_ACTUAL_COST
        );
    }

    @Test
    @Transactional
    void getAllHkjProjectsByActualCostIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where actualCost is not null
        defaultHkjProjectFiltering("actualCost.specified=true", "actualCost.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjProjectsByActualCostIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where actualCost is greater than or equal to
        defaultHkjProjectFiltering(
            "actualCost.greaterThanOrEqual=" + DEFAULT_ACTUAL_COST,
            "actualCost.greaterThanOrEqual=" + UPDATED_ACTUAL_COST
        );
    }

    @Test
    @Transactional
    void getAllHkjProjectsByActualCostIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where actualCost is less than or equal to
        defaultHkjProjectFiltering(
            "actualCost.lessThanOrEqual=" + DEFAULT_ACTUAL_COST,
            "actualCost.lessThanOrEqual=" + SMALLER_ACTUAL_COST
        );
    }

    @Test
    @Transactional
    void getAllHkjProjectsByActualCostIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where actualCost is less than
        defaultHkjProjectFiltering("actualCost.lessThan=" + UPDATED_ACTUAL_COST, "actualCost.lessThan=" + DEFAULT_ACTUAL_COST);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByActualCostIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where actualCost is greater than
        defaultHkjProjectFiltering("actualCost.greaterThan=" + SMALLER_ACTUAL_COST, "actualCost.greaterThan=" + DEFAULT_ACTUAL_COST);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByQualityCheckIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where qualityCheck equals to
        defaultHkjProjectFiltering("qualityCheck.equals=" + DEFAULT_QUALITY_CHECK, "qualityCheck.equals=" + UPDATED_QUALITY_CHECK);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByQualityCheckIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where qualityCheck in
        defaultHkjProjectFiltering(
            "qualityCheck.in=" + DEFAULT_QUALITY_CHECK + "," + UPDATED_QUALITY_CHECK,
            "qualityCheck.in=" + UPDATED_QUALITY_CHECK
        );
    }

    @Test
    @Transactional
    void getAllHkjProjectsByQualityCheckIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where qualityCheck is not null
        defaultHkjProjectFiltering("qualityCheck.specified=true", "qualityCheck.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjProjectsByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where isDeleted equals to
        defaultHkjProjectFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where isDeleted in
        defaultHkjProjectFiltering("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED, "isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjProjectsByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        // Get all the hkjProjectList where isDeleted is not null
        defaultHkjProjectFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjProjectsByManagerIsEqualToSomething() throws Exception {
        UserExtra manager;
        if (TestUtil.findAll(em, UserExtra.class).isEmpty()) {
            hkjProjectRepository.saveAndFlush(hkjProject);
            manager = UserExtraResourceIT.createEntity();
        } else {
            manager = TestUtil.findAll(em, UserExtra.class).get(0);
        }
        em.persist(manager);
        em.flush();
        hkjProject.setManager(manager);
        hkjProjectRepository.saveAndFlush(hkjProject);
        Long managerId = manager.getId();
        // Get all the hkjProjectList where manager equals to managerId
        defaultHkjProjectShouldBeFound("managerId.equals=" + managerId);

        // Get all the hkjProjectList where manager equals to (managerId + 1)
        defaultHkjProjectShouldNotBeFound("managerId.equals=" + (managerId + 1));
    }

    @Test
    @Transactional
    void getAllHkjProjectsByCategoryIsEqualToSomething() throws Exception {
        HkjCategory category;
        if (TestUtil.findAll(em, HkjCategory.class).isEmpty()) {
            hkjProjectRepository.saveAndFlush(hkjProject);
            category = HkjCategoryResourceIT.createEntity();
        } else {
            category = TestUtil.findAll(em, HkjCategory.class).get(0);
        }
        em.persist(category);
        em.flush();
        hkjProject.setCategory(category);
        hkjProjectRepository.saveAndFlush(hkjProject);
        Long categoryId = category.getId();
        // Get all the hkjProjectList where category equals to categoryId
        defaultHkjProjectShouldBeFound("categoryId.equals=" + categoryId);

        // Get all the hkjProjectList where category equals to (categoryId + 1)
        defaultHkjProjectShouldNotBeFound("categoryId.equals=" + (categoryId + 1));
    }

    @Test
    @Transactional
    void getAllHkjProjectsByMaterialIsEqualToSomething() throws Exception {
        HkjMaterial material;
        if (TestUtil.findAll(em, HkjMaterial.class).isEmpty()) {
            hkjProjectRepository.saveAndFlush(hkjProject);
            material = HkjMaterialResourceIT.createEntity();
        } else {
            material = TestUtil.findAll(em, HkjMaterial.class).get(0);
        }
        em.persist(material);
        em.flush();
        hkjProject.setMaterial(material);
        hkjProjectRepository.saveAndFlush(hkjProject);
        Long materialId = material.getId();
        // Get all the hkjProjectList where material equals to materialId
        defaultHkjProjectShouldBeFound("materialId.equals=" + materialId);

        // Get all the hkjProjectList where material equals to (materialId + 1)
        defaultHkjProjectShouldNotBeFound("materialId.equals=" + (materialId + 1));
    }

    private void defaultHkjProjectFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjProjectShouldBeFound(shouldBeFound);
        defaultHkjProjectShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjProjectShouldBeFound(String filter) throws Exception {
        restHkjProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjProject.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].coverImage").value(hasItem(DEFAULT_COVER_IMAGE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].expectDate").value(hasItem(DEFAULT_EXPECT_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].actualCost").value(hasItem(sameNumber(DEFAULT_ACTUAL_COST))))
            .andExpect(jsonPath("$.[*].qualityCheck").value(hasItem(DEFAULT_QUALITY_CHECK.booleanValue())))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restHkjProjectMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjProjectShouldNotBeFound(String filter) throws Exception {
        restHkjProjectMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjProjectMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjProject() throws Exception {
        // Get the hkjProject
        restHkjProjectMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjProject() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjProject
        HkjProject updatedHkjProject = hkjProjectRepository.findById(hkjProject.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjProject are not directly saved in db
        em.detach(updatedHkjProject);
        updatedHkjProject
            .name(UPDATED_NAME)
            .coverImage(UPDATED_COVER_IMAGE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .expectDate(UPDATED_EXPECT_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .actualCost(UPDATED_ACTUAL_COST)
            .qualityCheck(UPDATED_QUALITY_CHECK)
            .isDeleted(UPDATED_IS_DELETED);
        HkjProjectDTO hkjProjectDTO = hkjProjectMapper.toDto(updatedHkjProject);

        restHkjProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjProjectDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjProjectDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjProjectToMatchAllProperties(updatedHkjProject);
    }

    @Test
    @Transactional
    void putNonExistingHkjProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjProject.setId(longCount.incrementAndGet());

        // Create the HkjProject
        HkjProjectDTO hkjProjectDTO = hkjProjectMapper.toDto(hkjProject);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjProjectDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjProjectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjProject.setId(longCount.incrementAndGet());

        // Create the HkjProject
        HkjProjectDTO hkjProjectDTO = hkjProjectMapper.toDto(hkjProject);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjProjectMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjProjectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjProject.setId(longCount.incrementAndGet());

        // Create the HkjProject
        HkjProjectDTO hkjProjectDTO = hkjProjectMapper.toDto(hkjProject);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjProjectMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjProjectDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjProjectWithPatch() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjProject using partial update
        HkjProject partialUpdatedHkjProject = new HkjProject();
        partialUpdatedHkjProject.setId(hkjProject.getId());

        partialUpdatedHkjProject
            .description(UPDATED_DESCRIPTION)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .qualityCheck(UPDATED_QUALITY_CHECK);

        restHkjProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjProject.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjProject))
            )
            .andExpect(status().isOk());

        // Validate the HkjProject in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjProjectUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHkjProject, hkjProject),
            getPersistedHkjProject(hkjProject)
        );
    }

    @Test
    @Transactional
    void fullUpdateHkjProjectWithPatch() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjProject using partial update
        HkjProject partialUpdatedHkjProject = new HkjProject();
        partialUpdatedHkjProject.setId(hkjProject.getId());

        partialUpdatedHkjProject
            .name(UPDATED_NAME)
            .coverImage(UPDATED_COVER_IMAGE)
            .description(UPDATED_DESCRIPTION)
            .startDate(UPDATED_START_DATE)
            .expectDate(UPDATED_EXPECT_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .actualCost(UPDATED_ACTUAL_COST)
            .qualityCheck(UPDATED_QUALITY_CHECK)
            .isDeleted(UPDATED_IS_DELETED);

        restHkjProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjProject.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjProject))
            )
            .andExpect(status().isOk());

        // Validate the HkjProject in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjProjectUpdatableFieldsEquals(partialUpdatedHkjProject, getPersistedHkjProject(partialUpdatedHkjProject));
    }

    @Test
    @Transactional
    void patchNonExistingHkjProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjProject.setId(longCount.incrementAndGet());

        // Create the HkjProject
        HkjProjectDTO hkjProjectDTO = hkjProjectMapper.toDto(hkjProject);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjProjectDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjProjectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjProject.setId(longCount.incrementAndGet());

        // Create the HkjProject
        HkjProjectDTO hkjProjectDTO = hkjProjectMapper.toDto(hkjProject);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjProjectMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjProjectDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjProject() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjProject.setId(longCount.incrementAndGet());

        // Create the HkjProject
        HkjProjectDTO hkjProjectDTO = hkjProjectMapper.toDto(hkjProject);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjProjectMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hkjProjectDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjProject in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjProject() throws Exception {
        // Initialize the database
        insertedHkjProject = hkjProjectRepository.saveAndFlush(hkjProject);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjProject
        restHkjProjectMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjProject.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjProjectRepository.count();
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

    protected HkjProject getPersistedHkjProject(HkjProject hkjProject) {
        return hkjProjectRepository.findById(hkjProject.getId()).orElseThrow();
    }

    protected void assertPersistedHkjProjectToMatchAllProperties(HkjProject expectedHkjProject) {
        assertHkjProjectAllPropertiesEquals(expectedHkjProject, getPersistedHkjProject(expectedHkjProject));
    }

    protected void assertPersistedHkjProjectToMatchUpdatableProperties(HkjProject expectedHkjProject) {
        assertHkjProjectAllUpdatablePropertiesEquals(expectedHkjProject, getPersistedHkjProject(expectedHkjProject));
    }
}
