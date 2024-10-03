package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjTaskAsserts.*;
import static com.server.hkj.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.hkj.IntegrationTest;
import com.server.hkj.domain.HkjProject;
import com.server.hkj.domain.HkjTask;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.domain.enumeration.HkjOrderStatus;
import com.server.hkj.domain.enumeration.HkjPriority;
import com.server.hkj.repository.HkjTaskRepository;
import com.server.hkj.service.dto.HkjTaskDTO;
import com.server.hkj.service.mapper.HkjTaskMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link HkjTaskResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjTaskResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_COVER_IMAGE = "AAAAAAAAAA";
    private static final String UPDATED_COVER_IMAGE = "BBBBBBBBBB";

    private static final String DEFAULT_DESCRIPTION = "AAAAAAAAAA";
    private static final String UPDATED_DESCRIPTION = "BBBBBBBBBB";

    private static final Instant DEFAULT_ASSIGNED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_ASSIGNED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_EXPECT_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_EXPECT_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_COMPLETED_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_COMPLETED_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final HkjOrderStatus DEFAULT_STATUS = HkjOrderStatus.NEW;
    private static final HkjOrderStatus UPDATED_STATUS = HkjOrderStatus.PROCESSING;

    private static final HkjPriority DEFAULT_PRIORITY = HkjPriority.LOW;
    private static final HkjPriority UPDATED_PRIORITY = HkjPriority.MEDIUM;

    private static final Integer DEFAULT_POINT = 0;
    private static final Integer UPDATED_POINT = 1;
    private static final Integer SMALLER_POINT = 0 - 1;

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/hkj-tasks";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjTaskRepository hkjTaskRepository;

    @Autowired
    private HkjTaskMapper hkjTaskMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjTaskMockMvc;

    private HkjTask hkjTask;

    private HkjTask insertedHkjTask;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjTask createEntity() {
        return new HkjTask()
            .name(DEFAULT_NAME)
            .coverImage(DEFAULT_COVER_IMAGE)
            .description(DEFAULT_DESCRIPTION)
            .assignedDate(DEFAULT_ASSIGNED_DATE)
            .expectDate(DEFAULT_EXPECT_DATE)
            .completedDate(DEFAULT_COMPLETED_DATE)
            .status(DEFAULT_STATUS)
            .priority(DEFAULT_PRIORITY)
            .point(DEFAULT_POINT)
            .notes(DEFAULT_NOTES)
            .isDeleted(DEFAULT_IS_DELETED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjTask createUpdatedEntity() {
        return new HkjTask()
            .name(UPDATED_NAME)
            .coverImage(UPDATED_COVER_IMAGE)
            .description(UPDATED_DESCRIPTION)
            .assignedDate(UPDATED_ASSIGNED_DATE)
            .expectDate(UPDATED_EXPECT_DATE)
            .completedDate(UPDATED_COMPLETED_DATE)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .point(UPDATED_POINT)
            .notes(UPDATED_NOTES)
            .isDeleted(UPDATED_IS_DELETED);
    }

    @BeforeEach
    public void initTest() {
        hkjTask = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjTask != null) {
            hkjTaskRepository.delete(insertedHkjTask);
            insertedHkjTask = null;
        }
    }

    @Test
    @Transactional
    void createHkjTask() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjTask
        HkjTaskDTO hkjTaskDTO = hkjTaskMapper.toDto(hkjTask);
        var returnedHkjTaskDTO = om.readValue(
            restHkjTaskMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTaskDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjTaskDTO.class
        );

        // Validate the HkjTask in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjTask = hkjTaskMapper.toEntity(returnedHkjTaskDTO);
        assertHkjTaskUpdatableFieldsEquals(returnedHkjTask, getPersistedHkjTask(returnedHkjTask));

        insertedHkjTask = returnedHkjTask;
    }

    @Test
    @Transactional
    void createHkjTaskWithExistingId() throws Exception {
        // Create the HkjTask with an existing ID
        hkjTask.setId(1L);
        HkjTaskDTO hkjTaskDTO = hkjTaskMapper.toDto(hkjTask);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjTaskMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTaskDTO)))
            .andExpect(status().isBadRequest());

        // Validate the HkjTask in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjTask.setName(null);

        // Create the HkjTask, which fails.
        HkjTaskDTO hkjTaskDTO = hkjTaskMapper.toDto(hkjTask);

        restHkjTaskMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTaskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAssignedDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjTask.setAssignedDate(null);

        // Create the HkjTask, which fails.
        HkjTaskDTO hkjTaskDTO = hkjTaskMapper.toDto(hkjTask);

        restHkjTaskMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTaskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExpectDateIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjTask.setExpectDate(null);

        // Create the HkjTask, which fails.
        HkjTaskDTO hkjTaskDTO = hkjTaskMapper.toDto(hkjTask);

        restHkjTaskMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTaskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjTask.setStatus(null);

        // Create the HkjTask, which fails.
        HkjTaskDTO hkjTaskDTO = hkjTaskMapper.toDto(hkjTask);

        restHkjTaskMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTaskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkPriorityIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        hkjTask.setPriority(null);

        // Create the HkjTask, which fails.
        HkjTaskDTO hkjTaskDTO = hkjTaskMapper.toDto(hkjTask);

        restHkjTaskMockMvc
            .perform(post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTaskDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllHkjTasks() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList
        restHkjTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].coverImage").value(hasItem(DEFAULT_COVER_IMAGE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].assignedDate").value(hasItem(DEFAULT_ASSIGNED_DATE.toString())))
            .andExpect(jsonPath("$.[*].expectDate").value(hasItem(DEFAULT_EXPECT_DATE.toString())))
            .andExpect(jsonPath("$.[*].completedDate").value(hasItem(DEFAULT_COMPLETED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].point").value(hasItem(DEFAULT_POINT)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getHkjTask() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get the hkjTask
        restHkjTaskMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjTask.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjTask.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.coverImage").value(DEFAULT_COVER_IMAGE))
            .andExpect(jsonPath("$.description").value(DEFAULT_DESCRIPTION))
            .andExpect(jsonPath("$.assignedDate").value(DEFAULT_ASSIGNED_DATE.toString()))
            .andExpect(jsonPath("$.expectDate").value(DEFAULT_EXPECT_DATE.toString()))
            .andExpect(jsonPath("$.completedDate").value(DEFAULT_COMPLETED_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.priority").value(DEFAULT_PRIORITY.toString()))
            .andExpect(jsonPath("$.point").value(DEFAULT_POINT))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getHkjTasksByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        Long id = hkjTask.getId();

        defaultHkjTaskFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjTaskFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjTaskFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjTasksByNameIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where name equals to
        defaultHkjTaskFiltering("name.equals=" + DEFAULT_NAME, "name.equals=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjTasksByNameIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where name in
        defaultHkjTaskFiltering("name.in=" + DEFAULT_NAME + "," + UPDATED_NAME, "name.in=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjTasksByNameIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where name is not null
        defaultHkjTaskFiltering("name.specified=true", "name.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjTasksByNameContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where name contains
        defaultHkjTaskFiltering("name.contains=" + DEFAULT_NAME, "name.contains=" + UPDATED_NAME);
    }

    @Test
    @Transactional
    void getAllHkjTasksByNameNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where name does not contain
        defaultHkjTaskFiltering("name.doesNotContain=" + UPDATED_NAME, "name.doesNotContain=" + DEFAULT_NAME);
    }

    @Test
    @Transactional
    void getAllHkjTasksByCoverImageIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where coverImage equals to
        defaultHkjTaskFiltering("coverImage.equals=" + DEFAULT_COVER_IMAGE, "coverImage.equals=" + UPDATED_COVER_IMAGE);
    }

    @Test
    @Transactional
    void getAllHkjTasksByCoverImageIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where coverImage in
        defaultHkjTaskFiltering("coverImage.in=" + DEFAULT_COVER_IMAGE + "," + UPDATED_COVER_IMAGE, "coverImage.in=" + UPDATED_COVER_IMAGE);
    }

    @Test
    @Transactional
    void getAllHkjTasksByCoverImageIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where coverImage is not null
        defaultHkjTaskFiltering("coverImage.specified=true", "coverImage.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjTasksByCoverImageContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where coverImage contains
        defaultHkjTaskFiltering("coverImage.contains=" + DEFAULT_COVER_IMAGE, "coverImage.contains=" + UPDATED_COVER_IMAGE);
    }

    @Test
    @Transactional
    void getAllHkjTasksByCoverImageNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where coverImage does not contain
        defaultHkjTaskFiltering("coverImage.doesNotContain=" + UPDATED_COVER_IMAGE, "coverImage.doesNotContain=" + DEFAULT_COVER_IMAGE);
    }

    @Test
    @Transactional
    void getAllHkjTasksByDescriptionIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where description equals to
        defaultHkjTaskFiltering("description.equals=" + DEFAULT_DESCRIPTION, "description.equals=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHkjTasksByDescriptionIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where description in
        defaultHkjTaskFiltering(
            "description.in=" + DEFAULT_DESCRIPTION + "," + UPDATED_DESCRIPTION,
            "description.in=" + UPDATED_DESCRIPTION
        );
    }

    @Test
    @Transactional
    void getAllHkjTasksByDescriptionIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where description is not null
        defaultHkjTaskFiltering("description.specified=true", "description.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjTasksByDescriptionContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where description contains
        defaultHkjTaskFiltering("description.contains=" + DEFAULT_DESCRIPTION, "description.contains=" + UPDATED_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHkjTasksByDescriptionNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where description does not contain
        defaultHkjTaskFiltering("description.doesNotContain=" + UPDATED_DESCRIPTION, "description.doesNotContain=" + DEFAULT_DESCRIPTION);
    }

    @Test
    @Transactional
    void getAllHkjTasksByAssignedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where assignedDate equals to
        defaultHkjTaskFiltering("assignedDate.equals=" + DEFAULT_ASSIGNED_DATE, "assignedDate.equals=" + UPDATED_ASSIGNED_DATE);
    }

    @Test
    @Transactional
    void getAllHkjTasksByAssignedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where assignedDate in
        defaultHkjTaskFiltering(
            "assignedDate.in=" + DEFAULT_ASSIGNED_DATE + "," + UPDATED_ASSIGNED_DATE,
            "assignedDate.in=" + UPDATED_ASSIGNED_DATE
        );
    }

    @Test
    @Transactional
    void getAllHkjTasksByAssignedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where assignedDate is not null
        defaultHkjTaskFiltering("assignedDate.specified=true", "assignedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjTasksByExpectDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where expectDate equals to
        defaultHkjTaskFiltering("expectDate.equals=" + DEFAULT_EXPECT_DATE, "expectDate.equals=" + UPDATED_EXPECT_DATE);
    }

    @Test
    @Transactional
    void getAllHkjTasksByExpectDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where expectDate in
        defaultHkjTaskFiltering("expectDate.in=" + DEFAULT_EXPECT_DATE + "," + UPDATED_EXPECT_DATE, "expectDate.in=" + UPDATED_EXPECT_DATE);
    }

    @Test
    @Transactional
    void getAllHkjTasksByExpectDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where expectDate is not null
        defaultHkjTaskFiltering("expectDate.specified=true", "expectDate.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjTasksByCompletedDateIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where completedDate equals to
        defaultHkjTaskFiltering("completedDate.equals=" + DEFAULT_COMPLETED_DATE, "completedDate.equals=" + UPDATED_COMPLETED_DATE);
    }

    @Test
    @Transactional
    void getAllHkjTasksByCompletedDateIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where completedDate in
        defaultHkjTaskFiltering(
            "completedDate.in=" + DEFAULT_COMPLETED_DATE + "," + UPDATED_COMPLETED_DATE,
            "completedDate.in=" + UPDATED_COMPLETED_DATE
        );
    }

    @Test
    @Transactional
    void getAllHkjTasksByCompletedDateIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where completedDate is not null
        defaultHkjTaskFiltering("completedDate.specified=true", "completedDate.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjTasksByStatusIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where status equals to
        defaultHkjTaskFiltering("status.equals=" + DEFAULT_STATUS, "status.equals=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllHkjTasksByStatusIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where status in
        defaultHkjTaskFiltering("status.in=" + DEFAULT_STATUS + "," + UPDATED_STATUS, "status.in=" + UPDATED_STATUS);
    }

    @Test
    @Transactional
    void getAllHkjTasksByStatusIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where status is not null
        defaultHkjTaskFiltering("status.specified=true", "status.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjTasksByPriorityIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where priority equals to
        defaultHkjTaskFiltering("priority.equals=" + DEFAULT_PRIORITY, "priority.equals=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllHkjTasksByPriorityIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where priority in
        defaultHkjTaskFiltering("priority.in=" + DEFAULT_PRIORITY + "," + UPDATED_PRIORITY, "priority.in=" + UPDATED_PRIORITY);
    }

    @Test
    @Transactional
    void getAllHkjTasksByPriorityIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where priority is not null
        defaultHkjTaskFiltering("priority.specified=true", "priority.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjTasksByPointIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where point equals to
        defaultHkjTaskFiltering("point.equals=" + DEFAULT_POINT, "point.equals=" + UPDATED_POINT);
    }

    @Test
    @Transactional
    void getAllHkjTasksByPointIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where point in
        defaultHkjTaskFiltering("point.in=" + DEFAULT_POINT + "," + UPDATED_POINT, "point.in=" + UPDATED_POINT);
    }

    @Test
    @Transactional
    void getAllHkjTasksByPointIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where point is not null
        defaultHkjTaskFiltering("point.specified=true", "point.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjTasksByPointIsGreaterThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where point is greater than or equal to
        defaultHkjTaskFiltering("point.greaterThanOrEqual=" + DEFAULT_POINT, "point.greaterThanOrEqual=" + (DEFAULT_POINT + 1));
    }

    @Test
    @Transactional
    void getAllHkjTasksByPointIsLessThanOrEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where point is less than or equal to
        defaultHkjTaskFiltering("point.lessThanOrEqual=" + DEFAULT_POINT, "point.lessThanOrEqual=" + SMALLER_POINT);
    }

    @Test
    @Transactional
    void getAllHkjTasksByPointIsLessThanSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where point is less than
        defaultHkjTaskFiltering("point.lessThan=" + (DEFAULT_POINT + 1), "point.lessThan=" + DEFAULT_POINT);
    }

    @Test
    @Transactional
    void getAllHkjTasksByPointIsGreaterThanSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where point is greater than
        defaultHkjTaskFiltering("point.greaterThan=" + SMALLER_POINT, "point.greaterThan=" + DEFAULT_POINT);
    }

    @Test
    @Transactional
    void getAllHkjTasksByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where notes equals to
        defaultHkjTaskFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjTasksByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where notes in
        defaultHkjTaskFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjTasksByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where notes is not null
        defaultHkjTaskFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjTasksByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where notes contains
        defaultHkjTaskFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjTasksByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where notes does not contain
        defaultHkjTaskFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjTasksByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where isDeleted equals to
        defaultHkjTaskFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjTasksByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where isDeleted in
        defaultHkjTaskFiltering("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED, "isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjTasksByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        // Get all the hkjTaskList where isDeleted is not null
        defaultHkjTaskFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjTasksByEmployeeIsEqualToSomething() throws Exception {
        UserExtra employee;
        if (TestUtil.findAll(em, UserExtra.class).isEmpty()) {
            hkjTaskRepository.saveAndFlush(hkjTask);
            employee = UserExtraResourceIT.createEntity();
        } else {
            employee = TestUtil.findAll(em, UserExtra.class).get(0);
        }
        em.persist(employee);
        em.flush();
        hkjTask.setEmployee(employee);
        hkjTaskRepository.saveAndFlush(hkjTask);
        Long employeeId = employee.getId();
        // Get all the hkjTaskList where employee equals to employeeId
        defaultHkjTaskShouldBeFound("employeeId.equals=" + employeeId);

        // Get all the hkjTaskList where employee equals to (employeeId + 1)
        defaultHkjTaskShouldNotBeFound("employeeId.equals=" + (employeeId + 1));
    }

    @Test
    @Transactional
    void getAllHkjTasksByHkjProjectIsEqualToSomething() throws Exception {
        HkjProject hkjProject;
        if (TestUtil.findAll(em, HkjProject.class).isEmpty()) {
            hkjTaskRepository.saveAndFlush(hkjTask);
            hkjProject = HkjProjectResourceIT.createEntity();
        } else {
            hkjProject = TestUtil.findAll(em, HkjProject.class).get(0);
        }
        em.persist(hkjProject);
        em.flush();
        hkjTask.setHkjProject(hkjProject);
        hkjTaskRepository.saveAndFlush(hkjTask);
        Long hkjProjectId = hkjProject.getId();
        // Get all the hkjTaskList where hkjProject equals to hkjProjectId
        defaultHkjTaskShouldBeFound("hkjProjectId.equals=" + hkjProjectId);

        // Get all the hkjTaskList where hkjProject equals to (hkjProjectId + 1)
        defaultHkjTaskShouldNotBeFound("hkjProjectId.equals=" + (hkjProjectId + 1));
    }

    private void defaultHkjTaskFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjTaskShouldBeFound(shouldBeFound);
        defaultHkjTaskShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjTaskShouldBeFound(String filter) throws Exception {
        restHkjTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjTask.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].coverImage").value(hasItem(DEFAULT_COVER_IMAGE)))
            .andExpect(jsonPath("$.[*].description").value(hasItem(DEFAULT_DESCRIPTION)))
            .andExpect(jsonPath("$.[*].assignedDate").value(hasItem(DEFAULT_ASSIGNED_DATE.toString())))
            .andExpect(jsonPath("$.[*].expectDate").value(hasItem(DEFAULT_EXPECT_DATE.toString())))
            .andExpect(jsonPath("$.[*].completedDate").value(hasItem(DEFAULT_COMPLETED_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].priority").value(hasItem(DEFAULT_PRIORITY.toString())))
            .andExpect(jsonPath("$.[*].point").value(hasItem(DEFAULT_POINT)))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restHkjTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjTaskShouldNotBeFound(String filter) throws Exception {
        restHkjTaskMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjTaskMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjTask() throws Exception {
        // Get the hkjTask
        restHkjTaskMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjTask() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjTask
        HkjTask updatedHkjTask = hkjTaskRepository.findById(hkjTask.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjTask are not directly saved in db
        em.detach(updatedHkjTask);
        updatedHkjTask
            .name(UPDATED_NAME)
            .coverImage(UPDATED_COVER_IMAGE)
            .description(UPDATED_DESCRIPTION)
            .assignedDate(UPDATED_ASSIGNED_DATE)
            .expectDate(UPDATED_EXPECT_DATE)
            .completedDate(UPDATED_COMPLETED_DATE)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .point(UPDATED_POINT)
            .notes(UPDATED_NOTES)
            .isDeleted(UPDATED_IS_DELETED);
        HkjTaskDTO hkjTaskDTO = hkjTaskMapper.toDto(updatedHkjTask);

        restHkjTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjTaskDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjTaskDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjTaskToMatchAllProperties(updatedHkjTask);
    }

    @Test
    @Transactional
    void putNonExistingHkjTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTask.setId(longCount.incrementAndGet());

        // Create the HkjTask
        HkjTaskDTO hkjTaskDTO = hkjTaskMapper.toDto(hkjTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjTaskDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTask.setId(longCount.incrementAndGet());

        // Create the HkjTask
        HkjTaskDTO hkjTaskDTO = hkjTaskMapper.toDto(hkjTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTaskMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTask.setId(longCount.incrementAndGet());

        // Create the HkjTask
        HkjTaskDTO hkjTaskDTO = hkjTaskMapper.toDto(hkjTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTaskMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjTaskDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjTaskWithPatch() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjTask using partial update
        HkjTask partialUpdatedHkjTask = new HkjTask();
        partialUpdatedHkjTask.setId(hkjTask.getId());

        partialUpdatedHkjTask.coverImage(UPDATED_COVER_IMAGE).assignedDate(UPDATED_ASSIGNED_DATE).notes(UPDATED_NOTES);

        restHkjTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjTask.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjTask))
            )
            .andExpect(status().isOk());

        // Validate the HkjTask in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjTaskUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedHkjTask, hkjTask), getPersistedHkjTask(hkjTask));
    }

    @Test
    @Transactional
    void fullUpdateHkjTaskWithPatch() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjTask using partial update
        HkjTask partialUpdatedHkjTask = new HkjTask();
        partialUpdatedHkjTask.setId(hkjTask.getId());

        partialUpdatedHkjTask
            .name(UPDATED_NAME)
            .coverImage(UPDATED_COVER_IMAGE)
            .description(UPDATED_DESCRIPTION)
            .assignedDate(UPDATED_ASSIGNED_DATE)
            .expectDate(UPDATED_EXPECT_DATE)
            .completedDate(UPDATED_COMPLETED_DATE)
            .status(UPDATED_STATUS)
            .priority(UPDATED_PRIORITY)
            .point(UPDATED_POINT)
            .notes(UPDATED_NOTES)
            .isDeleted(UPDATED_IS_DELETED);

        restHkjTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjTask.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjTask))
            )
            .andExpect(status().isOk());

        // Validate the HkjTask in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjTaskUpdatableFieldsEquals(partialUpdatedHkjTask, getPersistedHkjTask(partialUpdatedHkjTask));
    }

    @Test
    @Transactional
    void patchNonExistingHkjTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTask.setId(longCount.incrementAndGet());

        // Create the HkjTask
        HkjTaskDTO hkjTaskDTO = hkjTaskMapper.toDto(hkjTask);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjTaskDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTask.setId(longCount.incrementAndGet());

        // Create the HkjTask
        HkjTaskDTO hkjTaskDTO = hkjTaskMapper.toDto(hkjTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTaskMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjTaskDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjTask() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjTask.setId(longCount.incrementAndGet());

        // Create the HkjTask
        HkjTaskDTO hkjTaskDTO = hkjTaskMapper.toDto(hkjTask);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjTaskMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hkjTaskDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjTask in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjTask() throws Exception {
        // Initialize the database
        insertedHkjTask = hkjTaskRepository.saveAndFlush(hkjTask);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjTask
        restHkjTaskMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjTask.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjTaskRepository.count();
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

    protected HkjTask getPersistedHkjTask(HkjTask hkjTask) {
        return hkjTaskRepository.findById(hkjTask.getId()).orElseThrow();
    }

    protected void assertPersistedHkjTaskToMatchAllProperties(HkjTask expectedHkjTask) {
        assertHkjTaskAllPropertiesEquals(expectedHkjTask, getPersistedHkjTask(expectedHkjTask));
    }

    protected void assertPersistedHkjTaskToMatchUpdatableProperties(HkjTask expectedHkjTask) {
        assertHkjTaskAllUpdatablePropertiesEquals(expectedHkjTask, getPersistedHkjTask(expectedHkjTask));
    }
}
