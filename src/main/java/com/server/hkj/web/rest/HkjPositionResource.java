package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjPositionRepository;
import com.server.hkj.service.HkjPositionQueryService;
import com.server.hkj.service.HkjPositionService;
import com.server.hkj.service.criteria.HkjPositionCriteria;
import com.server.hkj.service.dto.HkjPositionDTO;
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
 * REST controller for managing {@link com.server.hkj.domain.HkjPosition}.
 */
@RestController
@RequestMapping("/api/hkj-positions")
public class HkjPositionResource {

    private static final Logger LOG = LoggerFactory.getLogger(HkjPositionResource.class);

    private static final String ENTITY_NAME = "hkjPosition";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjPositionService hkjPositionService;

    private final HkjPositionRepository hkjPositionRepository;

    private final HkjPositionQueryService hkjPositionQueryService;

    public HkjPositionResource(
        HkjPositionService hkjPositionService,
        HkjPositionRepository hkjPositionRepository,
        HkjPositionQueryService hkjPositionQueryService
    ) {
        this.hkjPositionService = hkjPositionService;
        this.hkjPositionRepository = hkjPositionRepository;
        this.hkjPositionQueryService = hkjPositionQueryService;
    }

    /**
     * {@code POST  /hkj-positions} : Create a new hkjPosition.
     *
     * @param hkjPositionDTO the hkjPositionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjPositionDTO, or with status {@code 400 (Bad Request)} if the hkjPosition has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjPositionDTO> createHkjPosition(@Valid @RequestBody HkjPositionDTO hkjPositionDTO) throws URISyntaxException {
        LOG.debug("REST request to save HkjPosition : {}", hkjPositionDTO);
        if (hkjPositionDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjPosition cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjPositionDTO = hkjPositionService.save(hkjPositionDTO);
        return ResponseEntity.created(new URI("/api/hkj-positions/" + hkjPositionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjPositionDTO.getId().toString()))
            .body(hkjPositionDTO);
    }

    /**
     * {@code PUT  /hkj-positions/:id} : Updates an existing hkjPosition.
     *
     * @param id the id of the hkjPositionDTO to save.
     * @param hkjPositionDTO the hkjPositionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjPositionDTO,
     * or with status {@code 400 (Bad Request)} if the hkjPositionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjPositionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjPositionDTO> updateHkjPosition(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HkjPositionDTO hkjPositionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HkjPosition : {}, {}", id, hkjPositionDTO);
        if (hkjPositionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjPositionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjPositionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjPositionDTO = hkjPositionService.update(hkjPositionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjPositionDTO.getId().toString()))
            .body(hkjPositionDTO);
    }

    /**
     * {@code PATCH  /hkj-positions/:id} : Partial updates given fields of an existing hkjPosition, field will ignore if it is null
     *
     * @param id the id of the hkjPositionDTO to save.
     * @param hkjPositionDTO the hkjPositionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjPositionDTO,
     * or with status {@code 400 (Bad Request)} if the hkjPositionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjPositionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjPositionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjPositionDTO> partialUpdateHkjPosition(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HkjPositionDTO hkjPositionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HkjPosition partially : {}, {}", id, hkjPositionDTO);
        if (hkjPositionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjPositionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjPositionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjPositionDTO> result = hkjPositionService.partialUpdate(hkjPositionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjPositionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-positions} : get all the hkjPositions.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjPositions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjPositionDTO>> getAllHkjPositions(
        HkjPositionCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HkjPositions by criteria: {}", criteria);

        Page<HkjPositionDTO> page = hkjPositionQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-positions/count} : count all the hkjPositions.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjPositions(HkjPositionCriteria criteria) {
        LOG.debug("REST request to count HkjPositions by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjPositionQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-positions/:id} : get the "id" hkjPosition.
     *
     * @param id the id of the hkjPositionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjPositionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjPositionDTO> getHkjPosition(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HkjPosition : {}", id);
        Optional<HkjPositionDTO> hkjPositionDTO = hkjPositionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjPositionDTO);
    }

    /**
     * {@code DELETE  /hkj-positions/:id} : delete the "id" hkjPosition.
     *
     * @param id the id of the hkjPositionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjPosition(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HkjPosition : {}", id);
        hkjPositionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
