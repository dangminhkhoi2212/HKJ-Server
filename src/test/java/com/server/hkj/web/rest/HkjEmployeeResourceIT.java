package com.server.hkj.web.rest;

import static com.server.hkj.domain.HkjEmployeeAsserts.*;
import static com.server.hkj.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.server.hkj.IntegrationTest;
import com.server.hkj.domain.HkjEmployee;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.repository.HkjEmployeeRepository;
import com.server.hkj.service.dto.HkjEmployeeDTO;
import com.server.hkj.service.mapper.HkjEmployeeMapper;
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
 * Integration tests for the {@link HkjEmployeeResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class HkjEmployeeResourceIT {

    private static final String DEFAULT_NOTES = "AAAAAAAAAA";
    private static final String UPDATED_NOTES = "BBBBBBBBBB";

    private static final Boolean DEFAULT_IS_DELETED = false;
    private static final Boolean UPDATED_IS_DELETED = true;

    private static final String ENTITY_API_URL = "/api/hkj-employees";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private HkjEmployeeRepository hkjEmployeeRepository;

    @Autowired
    private HkjEmployeeMapper hkjEmployeeMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restHkjEmployeeMockMvc;

    private HkjEmployee hkjEmployee;

    private HkjEmployee insertedHkjEmployee;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjEmployee createEntity(EntityManager em) {
        HkjEmployee hkjEmployee = new HkjEmployee().notes(DEFAULT_NOTES).isDeleted(DEFAULT_IS_DELETED);
        return hkjEmployee;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static HkjEmployee createUpdatedEntity(EntityManager em) {
        HkjEmployee hkjEmployee = new HkjEmployee().notes(UPDATED_NOTES).isDeleted(UPDATED_IS_DELETED);
        return hkjEmployee;
    }

    @BeforeEach
    public void initTest() {
        hkjEmployee = createEntity(em);
    }

    @AfterEach
    public void cleanup() {
        if (insertedHkjEmployee != null) {
            hkjEmployeeRepository.delete(insertedHkjEmployee);
            insertedHkjEmployee = null;
        }
    }

    @Test
    @Transactional
    void createHkjEmployee() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the HkjEmployee
        HkjEmployeeDTO hkjEmployeeDTO = hkjEmployeeMapper.toDto(hkjEmployee);
        var returnedHkjEmployeeDTO = om.readValue(
            restHkjEmployeeMockMvc
                .perform(
                    post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjEmployeeDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            HkjEmployeeDTO.class
        );

        // Validate the HkjEmployee in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedHkjEmployee = hkjEmployeeMapper.toEntity(returnedHkjEmployeeDTO);
        assertHkjEmployeeUpdatableFieldsEquals(returnedHkjEmployee, getPersistedHkjEmployee(returnedHkjEmployee));

        insertedHkjEmployee = returnedHkjEmployee;
    }

    @Test
    @Transactional
    void createHkjEmployeeWithExistingId() throws Exception {
        // Create the HkjEmployee with an existing ID
        hkjEmployee.setId(1L);
        HkjEmployeeDTO hkjEmployeeDTO = hkjEmployeeMapper.toDto(hkjEmployee);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restHkjEmployeeMockMvc
            .perform(
                post(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjEmployee in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllHkjEmployees() throws Exception {
        // Initialize the database
        insertedHkjEmployee = hkjEmployeeRepository.saveAndFlush(hkjEmployee);

        // Get all the hkjEmployeeList
        restHkjEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjEmployee.getId().intValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));
    }

    @Test
    @Transactional
    void getHkjEmployee() throws Exception {
        // Initialize the database
        insertedHkjEmployee = hkjEmployeeRepository.saveAndFlush(hkjEmployee);

        // Get the hkjEmployee
        restHkjEmployeeMockMvc
            .perform(get(ENTITY_API_URL_ID, hkjEmployee.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(hkjEmployee.getId().intValue()))
            .andExpect(jsonPath("$.notes").value(DEFAULT_NOTES))
            .andExpect(jsonPath("$.isDeleted").value(DEFAULT_IS_DELETED.booleanValue()));
    }

    @Test
    @Transactional
    void getHkjEmployeesByIdFiltering() throws Exception {
        // Initialize the database
        insertedHkjEmployee = hkjEmployeeRepository.saveAndFlush(hkjEmployee);

        Long id = hkjEmployee.getId();

        defaultHkjEmployeeFiltering("id.equals=" + id, "id.notEquals=" + id);

        defaultHkjEmployeeFiltering("id.greaterThanOrEqual=" + id, "id.greaterThan=" + id);

        defaultHkjEmployeeFiltering("id.lessThanOrEqual=" + id, "id.lessThan=" + id);
    }

    @Test
    @Transactional
    void getAllHkjEmployeesByNotesIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjEmployee = hkjEmployeeRepository.saveAndFlush(hkjEmployee);

        // Get all the hkjEmployeeList where notes equals to
        defaultHkjEmployeeFiltering("notes.equals=" + DEFAULT_NOTES, "notes.equals=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjEmployeesByNotesIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjEmployee = hkjEmployeeRepository.saveAndFlush(hkjEmployee);

        // Get all the hkjEmployeeList where notes in
        defaultHkjEmployeeFiltering("notes.in=" + DEFAULT_NOTES + "," + UPDATED_NOTES, "notes.in=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjEmployeesByNotesIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjEmployee = hkjEmployeeRepository.saveAndFlush(hkjEmployee);

        // Get all the hkjEmployeeList where notes is not null
        defaultHkjEmployeeFiltering("notes.specified=true", "notes.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjEmployeesByNotesContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjEmployee = hkjEmployeeRepository.saveAndFlush(hkjEmployee);

        // Get all the hkjEmployeeList where notes contains
        defaultHkjEmployeeFiltering("notes.contains=" + DEFAULT_NOTES, "notes.contains=" + UPDATED_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjEmployeesByNotesNotContainsSomething() throws Exception {
        // Initialize the database
        insertedHkjEmployee = hkjEmployeeRepository.saveAndFlush(hkjEmployee);

        // Get all the hkjEmployeeList where notes does not contain
        defaultHkjEmployeeFiltering("notes.doesNotContain=" + UPDATED_NOTES, "notes.doesNotContain=" + DEFAULT_NOTES);
    }

    @Test
    @Transactional
    void getAllHkjEmployeesByIsDeletedIsEqualToSomething() throws Exception {
        // Initialize the database
        insertedHkjEmployee = hkjEmployeeRepository.saveAndFlush(hkjEmployee);

        // Get all the hkjEmployeeList where isDeleted equals to
        defaultHkjEmployeeFiltering("isDeleted.equals=" + DEFAULT_IS_DELETED, "isDeleted.equals=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjEmployeesByIsDeletedIsInShouldWork() throws Exception {
        // Initialize the database
        insertedHkjEmployee = hkjEmployeeRepository.saveAndFlush(hkjEmployee);

        // Get all the hkjEmployeeList where isDeleted in
        defaultHkjEmployeeFiltering("isDeleted.in=" + DEFAULT_IS_DELETED + "," + UPDATED_IS_DELETED, "isDeleted.in=" + UPDATED_IS_DELETED);
    }

    @Test
    @Transactional
    void getAllHkjEmployeesByIsDeletedIsNullOrNotNull() throws Exception {
        // Initialize the database
        insertedHkjEmployee = hkjEmployeeRepository.saveAndFlush(hkjEmployee);

        // Get all the hkjEmployeeList where isDeleted is not null
        defaultHkjEmployeeFiltering("isDeleted.specified=true", "isDeleted.specified=false");
    }

    @Test
    @Transactional
    void getAllHkjEmployeesByUserExtraIsEqualToSomething() throws Exception {
        UserExtra userExtra;
        if (TestUtil.findAll(em, UserExtra.class).isEmpty()) {
            hkjEmployeeRepository.saveAndFlush(hkjEmployee);
            userExtra = UserExtraResourceIT.createEntity(em);
        } else {
            userExtra = TestUtil.findAll(em, UserExtra.class).get(0);
        }
        em.persist(userExtra);
        em.flush();
        hkjEmployee.setUserExtra(userExtra);
        hkjEmployeeRepository.saveAndFlush(hkjEmployee);
        Long userExtraId = userExtra.getId();
        // Get all the hkjEmployeeList where userExtra equals to userExtraId
        defaultHkjEmployeeShouldBeFound("userExtraId.equals=" + userExtraId);

        // Get all the hkjEmployeeList where userExtra equals to (userExtraId + 1)
        defaultHkjEmployeeShouldNotBeFound("userExtraId.equals=" + (userExtraId + 1));
    }

    private void defaultHkjEmployeeFiltering(String shouldBeFound, String shouldNotBeFound) throws Exception {
        defaultHkjEmployeeShouldBeFound(shouldBeFound);
        defaultHkjEmployeeShouldNotBeFound(shouldNotBeFound);
    }

    /**
     * Executes the search, and checks that the default entity is returned.
     */
    private void defaultHkjEmployeeShouldBeFound(String filter) throws Exception {
        restHkjEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(hkjEmployee.getId().intValue())))
            .andExpect(jsonPath("$.[*].notes").value(hasItem(DEFAULT_NOTES)))
            .andExpect(jsonPath("$.[*].isDeleted").value(hasItem(DEFAULT_IS_DELETED.booleanValue())));

        // Check, that the count call also returns 1
        restHkjEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("1"));
    }

    /**
     * Executes the search, and checks that the default entity is not returned.
     */
    private void defaultHkjEmployeeShouldNotBeFound(String filter) throws Exception {
        restHkjEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$").isArray())
            .andExpect(jsonPath("$").isEmpty());

        // Check, that the count call also returns 0
        restHkjEmployeeMockMvc
            .perform(get(ENTITY_API_URL + "/count?sort=id,desc&" + filter))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(content().string("0"));
    }

    @Test
    @Transactional
    void getNonExistingHkjEmployee() throws Exception {
        // Get the hkjEmployee
        restHkjEmployeeMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingHkjEmployee() throws Exception {
        // Initialize the database
        insertedHkjEmployee = hkjEmployeeRepository.saveAndFlush(hkjEmployee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjEmployee
        HkjEmployee updatedHkjEmployee = hkjEmployeeRepository.findById(hkjEmployee.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedHkjEmployee are not directly saved in db
        em.detach(updatedHkjEmployee);
        updatedHkjEmployee.notes(UPDATED_NOTES).isDeleted(UPDATED_IS_DELETED);
        HkjEmployeeDTO hkjEmployeeDTO = hkjEmployeeMapper.toDto(updatedHkjEmployee);

        restHkjEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjEmployeeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjEmployeeDTO))
            )
            .andExpect(status().isOk());

        // Validate the HkjEmployee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedHkjEmployeeToMatchAllProperties(updatedHkjEmployee);
    }

    @Test
    @Transactional
    void putNonExistingHkjEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjEmployee.setId(longCount.incrementAndGet());

        // Create the HkjEmployee
        HkjEmployeeDTO hkjEmployeeDTO = hkjEmployeeMapper.toDto(hkjEmployee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, hkjEmployeeDTO.getId())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjEmployee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchHkjEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjEmployee.setId(longCount.incrementAndGet());

        // Create the HkjEmployee
        HkjEmployeeDTO hkjEmployeeDTO = hkjEmployeeMapper.toDto(hkjEmployee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjEmployeeMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(hkjEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjEmployee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamHkjEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjEmployee.setId(longCount.incrementAndGet());

        // Create the HkjEmployee
        HkjEmployeeDTO hkjEmployeeDTO = hkjEmployeeMapper.toDto(hkjEmployee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjEmployeeMockMvc
            .perform(put(ENTITY_API_URL).with(csrf()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(hkjEmployeeDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjEmployee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateHkjEmployeeWithPatch() throws Exception {
        // Initialize the database
        insertedHkjEmployee = hkjEmployeeRepository.saveAndFlush(hkjEmployee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjEmployee using partial update
        HkjEmployee partialUpdatedHkjEmployee = new HkjEmployee();
        partialUpdatedHkjEmployee.setId(hkjEmployee.getId());

        partialUpdatedHkjEmployee.isDeleted(UPDATED_IS_DELETED);

        restHkjEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjEmployee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjEmployee))
            )
            .andExpect(status().isOk());

        // Validate the HkjEmployee in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjEmployeeUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedHkjEmployee, hkjEmployee),
            getPersistedHkjEmployee(hkjEmployee)
        );
    }

    @Test
    @Transactional
    void fullUpdateHkjEmployeeWithPatch() throws Exception {
        // Initialize the database
        insertedHkjEmployee = hkjEmployeeRepository.saveAndFlush(hkjEmployee);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the hkjEmployee using partial update
        HkjEmployee partialUpdatedHkjEmployee = new HkjEmployee();
        partialUpdatedHkjEmployee.setId(hkjEmployee.getId());

        partialUpdatedHkjEmployee.notes(UPDATED_NOTES).isDeleted(UPDATED_IS_DELETED);

        restHkjEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedHkjEmployee.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedHkjEmployee))
            )
            .andExpect(status().isOk());

        // Validate the HkjEmployee in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertHkjEmployeeUpdatableFieldsEquals(partialUpdatedHkjEmployee, getPersistedHkjEmployee(partialUpdatedHkjEmployee));
    }

    @Test
    @Transactional
    void patchNonExistingHkjEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjEmployee.setId(longCount.incrementAndGet());

        // Create the HkjEmployee
        HkjEmployeeDTO hkjEmployeeDTO = hkjEmployeeMapper.toDto(hkjEmployee);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restHkjEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, hkjEmployeeDTO.getId())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjEmployee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchHkjEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjEmployee.setId(longCount.incrementAndGet());

        // Create the HkjEmployee
        HkjEmployeeDTO hkjEmployeeDTO = hkjEmployeeMapper.toDto(hkjEmployee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .with(csrf())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(hkjEmployeeDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the HkjEmployee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamHkjEmployee() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        hkjEmployee.setId(longCount.incrementAndGet());

        // Create the HkjEmployee
        HkjEmployeeDTO hkjEmployeeDTO = hkjEmployeeMapper.toDto(hkjEmployee);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restHkjEmployeeMockMvc
            .perform(
                patch(ENTITY_API_URL).with(csrf()).contentType("application/merge-patch+json").content(om.writeValueAsBytes(hkjEmployeeDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the HkjEmployee in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteHkjEmployee() throws Exception {
        // Initialize the database
        insertedHkjEmployee = hkjEmployeeRepository.saveAndFlush(hkjEmployee);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the hkjEmployee
        restHkjEmployeeMockMvc
            .perform(delete(ENTITY_API_URL_ID, hkjEmployee.getId()).with(csrf()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return hkjEmployeeRepository.count();
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

    protected HkjEmployee getPersistedHkjEmployee(HkjEmployee hkjEmployee) {
        return hkjEmployeeRepository.findById(hkjEmployee.getId()).orElseThrow();
    }

    protected void assertPersistedHkjEmployeeToMatchAllProperties(HkjEmployee expectedHkjEmployee) {
        assertHkjEmployeeAllPropertiesEquals(expectedHkjEmployee, getPersistedHkjEmployee(expectedHkjEmployee));
    }

    protected void assertPersistedHkjEmployeeToMatchUpdatableProperties(HkjEmployee expectedHkjEmployee) {
        assertHkjEmployeeAllUpdatablePropertiesEquals(expectedHkjEmployee, getPersistedHkjEmployee(expectedHkjEmployee));
    }
}
