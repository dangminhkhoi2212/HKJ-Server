package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjTaskRepository;
import com.server.hkj.service.HkjTaskQueryService;
import com.server.hkj.service.HkjTaskService;
import com.server.hkj.service.criteria.HkjTaskCriteria;
import com.server.hkj.service.dto.HkjTaskDTO;
import com.server.hkj.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.server.hkj.domain.HkjTask}.
 */
@RestController
@RequestMapping("/api/hkj-tasks")
public class HkjTaskResource {

    private static final Logger log = LoggerFactory.getLogger(HkjTaskResource.class);

    private static final String ENTITY_NAME = "hkjTask";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjTaskService hkjTaskService;

    private final HkjTaskRepository hkjTaskRepository;

    private final HkjTaskQueryService hkjTaskQueryService;

    public HkjTaskResource(HkjTaskService hkjTaskService, HkjTaskRepository hkjTaskRepository, HkjTaskQueryService hkjTaskQueryService) {
        this.hkjTaskService = hkjTaskService;
        this.hkjTaskRepository = hkjTaskRepository;
        this.hkjTaskQueryService = hkjTaskQueryService;
    }

    /**
     * {@code POST  /hkj-tasks} : Create a new hkjTask.
     *
     * @param hkjTaskDTO the hkjTaskDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjTaskDTO, or with status {@code 400 (Bad Request)} if the hkjTask has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjTaskDTO> createHkjTask(@Valid @RequestBody HkjTaskDTO hkjTaskDTO) throws URISyntaxException {
        log.debug("REST request to save HkjTask : {}", hkjTaskDTO);
        if (hkjTaskDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjTask cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjTaskDTO = hkjTaskService.save(hkjTaskDTO);
        return ResponseEntity.created(new URI("/api/hkj-tasks/" + hkjTaskDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjTaskDTO.getId().toString()))
            .body(hkjTaskDTO);
    }

    /**
     * {@code PUT  /hkj-tasks/:id} : Updates an existing hkjTask.
     *
     * @param id the id of the hkjTaskDTO to save.
     * @param hkjTaskDTO the hkjTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjTaskDTO,
     * or with status {@code 400 (Bad Request)} if the hkjTaskDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjTaskDTO> updateHkjTask(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HkjTaskDTO hkjTaskDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HkjTask : {}, {}", id, hkjTaskDTO);
        if (hkjTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjTaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjTaskDTO = hkjTaskService.update(hkjTaskDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjTaskDTO.getId().toString()))
            .body(hkjTaskDTO);
    }

    /**
     * {@code PATCH  /hkj-tasks/:id} : Partial updates given fields of an existing hkjTask, field will ignore if it is null
     *
     * @param id the id of the hkjTaskDTO to save.
     * @param hkjTaskDTO the hkjTaskDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjTaskDTO,
     * or with status {@code 400 (Bad Request)} if the hkjTaskDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjTaskDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjTaskDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjTaskDTO> partialUpdateHkjTask(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HkjTaskDTO hkjTaskDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HkjTask partially : {}, {}", id, hkjTaskDTO);
        if (hkjTaskDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjTaskDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjTaskRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjTaskDTO> result = hkjTaskService.partialUpdate(hkjTaskDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjTaskDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-tasks} : get all the hkjTasks.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjTasks in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjTaskDTO>> getAllHkjTasks(
        HkjTaskCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get HkjTasks by criteria: {}", criteria);

        Page<HkjTaskDTO> page = hkjTaskQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-tasks/count} : count all the hkjTasks.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjTasks(HkjTaskCriteria criteria) {
        log.debug("REST request to count HkjTasks by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjTaskQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-tasks/:id} : get the "id" hkjTask.
     *
     * @param id the id of the hkjTaskDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjTaskDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjTaskDTO> getHkjTask(@PathVariable("id") Long id) {
        log.debug("REST request to get HkjTask : {}", id);
        Optional<HkjTaskDTO> hkjTaskDTO = hkjTaskService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjTaskDTO);
    }

    /**
     * {@code DELETE  /hkj-tasks/:id} : delete the "id" hkjTask.
     *
     * @param id the id of the hkjTaskDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjTask(@PathVariable("id") Long id) {
        log.debug("REST request to delete HkjTask : {}", id);
        hkjTaskService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
