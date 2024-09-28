package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjProjectRepository;
import com.server.hkj.service.HkjProjectQueryService;
import com.server.hkj.service.HkjProjectService;
import com.server.hkj.service.criteria.HkjProjectCriteria;
import com.server.hkj.service.dto.HkjProjectDTO;
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
 * REST controller for managing {@link com.server.hkj.domain.HkjProject}.
 */
@RestController
@RequestMapping("/api/hkj-projects")
public class HkjProjectResource {

    private static final Logger LOG = LoggerFactory.getLogger(HkjProjectResource.class);

    private static final String ENTITY_NAME = "hkjProject";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjProjectService hkjProjectService;

    private final HkjProjectRepository hkjProjectRepository;

    private final HkjProjectQueryService hkjProjectQueryService;

    public HkjProjectResource(
        HkjProjectService hkjProjectService,
        HkjProjectRepository hkjProjectRepository,
        HkjProjectQueryService hkjProjectQueryService
    ) {
        this.hkjProjectService = hkjProjectService;
        this.hkjProjectRepository = hkjProjectRepository;
        this.hkjProjectQueryService = hkjProjectQueryService;
    }

    /**
     * {@code POST  /hkj-projects} : Create a new hkjProject.
     *
     * @param hkjProjectDTO the hkjProjectDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjProjectDTO, or with status {@code 400 (Bad Request)} if the hkjProject has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjProjectDTO> createHkjProject(@Valid @RequestBody HkjProjectDTO hkjProjectDTO) throws URISyntaxException {
        LOG.debug("REST request to save HkjProject : {}", hkjProjectDTO);
        if (hkjProjectDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjProject cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjProjectDTO = hkjProjectService.save(hkjProjectDTO);
        return ResponseEntity.created(new URI("/api/hkj-projects/" + hkjProjectDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjProjectDTO.getId().toString()))
            .body(hkjProjectDTO);
    }

    /**
     * {@code PUT  /hkj-projects/:id} : Updates an existing hkjProject.
     *
     * @param id the id of the hkjProjectDTO to save.
     * @param hkjProjectDTO the hkjProjectDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjProjectDTO,
     * or with status {@code 400 (Bad Request)} if the hkjProjectDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjProjectDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjProjectDTO> updateHkjProject(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HkjProjectDTO hkjProjectDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HkjProject : {}, {}", id, hkjProjectDTO);
        if (hkjProjectDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjProjectDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjProjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjProjectDTO = hkjProjectService.update(hkjProjectDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjProjectDTO.getId().toString()))
            .body(hkjProjectDTO);
    }

    /**
     * {@code PATCH  /hkj-projects/:id} : Partial updates given fields of an existing hkjProject, field will ignore if it is null
     *
     * @param id the id of the hkjProjectDTO to save.
     * @param hkjProjectDTO the hkjProjectDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjProjectDTO,
     * or with status {@code 400 (Bad Request)} if the hkjProjectDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjProjectDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjProjectDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjProjectDTO> partialUpdateHkjProject(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HkjProjectDTO hkjProjectDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HkjProject partially : {}, {}", id, hkjProjectDTO);
        if (hkjProjectDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjProjectDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjProjectRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjProjectDTO> result = hkjProjectService.partialUpdate(hkjProjectDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjProjectDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-projects} : get all the hkjProjects.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjProjects in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjProjectDTO>> getAllHkjProjects(
        HkjProjectCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HkjProjects by criteria: {}", criteria);

        Page<HkjProjectDTO> page = hkjProjectQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-projects/count} : count all the hkjProjects.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjProjects(HkjProjectCriteria criteria) {
        LOG.debug("REST request to count HkjProjects by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjProjectQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-projects/:id} : get the "id" hkjProject.
     *
     * @param id the id of the hkjProjectDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjProjectDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjProjectDTO> getHkjProject(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HkjProject : {}", id);
        Optional<HkjProjectDTO> hkjProjectDTO = hkjProjectService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjProjectDTO);
    }

    /**
     * {@code DELETE  /hkj-projects/:id} : delete the "id" hkjProject.
     *
     * @param id the id of the hkjProjectDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjProject(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HkjProject : {}", id);
        hkjProjectService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
