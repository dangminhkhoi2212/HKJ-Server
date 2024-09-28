package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjTemplateStepRepository;
import com.server.hkj.service.HkjTemplateStepQueryService;
import com.server.hkj.service.HkjTemplateStepService;
import com.server.hkj.service.criteria.HkjTemplateStepCriteria;
import com.server.hkj.service.dto.HkjTemplateStepDTO;
import com.server.hkj.web.rest.errors.BadRequestAlertException;
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
 * REST controller for managing {@link com.server.hkj.domain.HkjTemplateStep}.
 */
@RestController
@RequestMapping("/api/hkj-template-steps")
public class HkjTemplateStepResource {

    private static final Logger LOG = LoggerFactory.getLogger(HkjTemplateStepResource.class);

    private static final String ENTITY_NAME = "hkjTemplateStep";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjTemplateStepService hkjTemplateStepService;

    private final HkjTemplateStepRepository hkjTemplateStepRepository;

    private final HkjTemplateStepQueryService hkjTemplateStepQueryService;

    public HkjTemplateStepResource(
        HkjTemplateStepService hkjTemplateStepService,
        HkjTemplateStepRepository hkjTemplateStepRepository,
        HkjTemplateStepQueryService hkjTemplateStepQueryService
    ) {
        this.hkjTemplateStepService = hkjTemplateStepService;
        this.hkjTemplateStepRepository = hkjTemplateStepRepository;
        this.hkjTemplateStepQueryService = hkjTemplateStepQueryService;
    }

    /**
     * {@code POST  /hkj-template-steps} : Create a new hkjTemplateStep.
     *
     * @param hkjTemplateStepDTO the hkjTemplateStepDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjTemplateStepDTO, or with status {@code 400 (Bad Request)} if the hkjTemplateStep has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjTemplateStepDTO> createHkjTemplateStep(@RequestBody HkjTemplateStepDTO hkjTemplateStepDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save HkjTemplateStep : {}", hkjTemplateStepDTO);
        if (hkjTemplateStepDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjTemplateStep cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjTemplateStepDTO = hkjTemplateStepService.save(hkjTemplateStepDTO);
        return ResponseEntity.created(new URI("/api/hkj-template-steps/" + hkjTemplateStepDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjTemplateStepDTO.getId().toString()))
            .body(hkjTemplateStepDTO);
    }

    /**
     * {@code PUT  /hkj-template-steps/:id} : Updates an existing hkjTemplateStep.
     *
     * @param id the id of the hkjTemplateStepDTO to save.
     * @param hkjTemplateStepDTO the hkjTemplateStepDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjTemplateStepDTO,
     * or with status {@code 400 (Bad Request)} if the hkjTemplateStepDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjTemplateStepDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjTemplateStepDTO> updateHkjTemplateStep(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HkjTemplateStepDTO hkjTemplateStepDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HkjTemplateStep : {}, {}", id, hkjTemplateStepDTO);
        if (hkjTemplateStepDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjTemplateStepDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjTemplateStepRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjTemplateStepDTO = hkjTemplateStepService.update(hkjTemplateStepDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjTemplateStepDTO.getId().toString()))
            .body(hkjTemplateStepDTO);
    }

    /**
     * {@code PATCH  /hkj-template-steps/:id} : Partial updates given fields of an existing hkjTemplateStep, field will ignore if it is null
     *
     * @param id the id of the hkjTemplateStepDTO to save.
     * @param hkjTemplateStepDTO the hkjTemplateStepDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjTemplateStepDTO,
     * or with status {@code 400 (Bad Request)} if the hkjTemplateStepDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjTemplateStepDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjTemplateStepDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjTemplateStepDTO> partialUpdateHkjTemplateStep(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HkjTemplateStepDTO hkjTemplateStepDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HkjTemplateStep partially : {}, {}", id, hkjTemplateStepDTO);
        if (hkjTemplateStepDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjTemplateStepDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjTemplateStepRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjTemplateStepDTO> result = hkjTemplateStepService.partialUpdate(hkjTemplateStepDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjTemplateStepDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-template-steps} : get all the hkjTemplateSteps.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjTemplateSteps in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjTemplateStepDTO>> getAllHkjTemplateSteps(
        HkjTemplateStepCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HkjTemplateSteps by criteria: {}", criteria);

        Page<HkjTemplateStepDTO> page = hkjTemplateStepQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-template-steps/count} : count all the hkjTemplateSteps.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjTemplateSteps(HkjTemplateStepCriteria criteria) {
        LOG.debug("REST request to count HkjTemplateSteps by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjTemplateStepQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-template-steps/:id} : get the "id" hkjTemplateStep.
     *
     * @param id the id of the hkjTemplateStepDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjTemplateStepDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjTemplateStepDTO> getHkjTemplateStep(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HkjTemplateStep : {}", id);
        Optional<HkjTemplateStepDTO> hkjTemplateStepDTO = hkjTemplateStepService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjTemplateStepDTO);
    }

    /**
     * {@code DELETE  /hkj-template-steps/:id} : delete the "id" hkjTemplateStep.
     *
     * @param id the id of the hkjTemplateStepDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjTemplateStep(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HkjTemplateStep : {}", id);
        hkjTemplateStepService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
