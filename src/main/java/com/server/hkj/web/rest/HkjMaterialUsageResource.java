package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjMaterialUsageRepository;
import com.server.hkj.service.HkjMaterialUsageQueryService;
import com.server.hkj.service.HkjMaterialUsageService;
import com.server.hkj.service.criteria.HkjMaterialUsageCriteria;
import com.server.hkj.service.dto.HkjMaterialUsageDTO;
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
 * REST controller for managing {@link com.server.hkj.domain.HkjMaterialUsage}.
 */
@RestController
@RequestMapping("/api/hkj-material-usages")
public class HkjMaterialUsageResource {

    private static final Logger LOG = LoggerFactory.getLogger(HkjMaterialUsageResource.class);

    private static final String ENTITY_NAME = "hkjMaterialUsage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjMaterialUsageService hkjMaterialUsageService;

    private final HkjMaterialUsageRepository hkjMaterialUsageRepository;

    private final HkjMaterialUsageQueryService hkjMaterialUsageQueryService;

    public HkjMaterialUsageResource(
        HkjMaterialUsageService hkjMaterialUsageService,
        HkjMaterialUsageRepository hkjMaterialUsageRepository,
        HkjMaterialUsageQueryService hkjMaterialUsageQueryService
    ) {
        this.hkjMaterialUsageService = hkjMaterialUsageService;
        this.hkjMaterialUsageRepository = hkjMaterialUsageRepository;
        this.hkjMaterialUsageQueryService = hkjMaterialUsageQueryService;
    }

    /**
     * {@code POST  /hkj-material-usages} : Create a new hkjMaterialUsage.
     *
     * @param hkjMaterialUsageDTO the hkjMaterialUsageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjMaterialUsageDTO, or with status {@code 400 (Bad Request)} if the hkjMaterialUsage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjMaterialUsageDTO> createHkjMaterialUsage(@RequestBody HkjMaterialUsageDTO hkjMaterialUsageDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save HkjMaterialUsage : {}", hkjMaterialUsageDTO);
        if (hkjMaterialUsageDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjMaterialUsage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjMaterialUsageDTO = hkjMaterialUsageService.save(hkjMaterialUsageDTO);
        return ResponseEntity.created(new URI("/api/hkj-material-usages/" + hkjMaterialUsageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjMaterialUsageDTO.getId().toString()))
            .body(hkjMaterialUsageDTO);
    }

    /**
     * {@code PUT  /hkj-material-usages/:id} : Updates an existing hkjMaterialUsage.
     *
     * @param id the id of the hkjMaterialUsageDTO to save.
     * @param hkjMaterialUsageDTO the hkjMaterialUsageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjMaterialUsageDTO,
     * or with status {@code 400 (Bad Request)} if the hkjMaterialUsageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjMaterialUsageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjMaterialUsageDTO> updateHkjMaterialUsage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HkjMaterialUsageDTO hkjMaterialUsageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HkjMaterialUsage : {}, {}", id, hkjMaterialUsageDTO);
        if (hkjMaterialUsageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjMaterialUsageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjMaterialUsageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjMaterialUsageDTO = hkjMaterialUsageService.update(hkjMaterialUsageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjMaterialUsageDTO.getId().toString()))
            .body(hkjMaterialUsageDTO);
    }

    /**
     * {@code PATCH  /hkj-material-usages/:id} : Partial updates given fields of an existing hkjMaterialUsage, field will ignore if it is null
     *
     * @param id the id of the hkjMaterialUsageDTO to save.
     * @param hkjMaterialUsageDTO the hkjMaterialUsageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjMaterialUsageDTO,
     * or with status {@code 400 (Bad Request)} if the hkjMaterialUsageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjMaterialUsageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjMaterialUsageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjMaterialUsageDTO> partialUpdateHkjMaterialUsage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HkjMaterialUsageDTO hkjMaterialUsageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HkjMaterialUsage partially : {}, {}", id, hkjMaterialUsageDTO);
        if (hkjMaterialUsageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjMaterialUsageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjMaterialUsageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjMaterialUsageDTO> result = hkjMaterialUsageService.partialUpdate(hkjMaterialUsageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjMaterialUsageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-material-usages} : get all the hkjMaterialUsages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjMaterialUsages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjMaterialUsageDTO>> getAllHkjMaterialUsages(
        HkjMaterialUsageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HkjMaterialUsages by criteria: {}", criteria);

        Page<HkjMaterialUsageDTO> page = hkjMaterialUsageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-material-usages/count} : count all the hkjMaterialUsages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjMaterialUsages(HkjMaterialUsageCriteria criteria) {
        LOG.debug("REST request to count HkjMaterialUsages by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjMaterialUsageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-material-usages/:id} : get the "id" hkjMaterialUsage.
     *
     * @param id the id of the hkjMaterialUsageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjMaterialUsageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjMaterialUsageDTO> getHkjMaterialUsage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HkjMaterialUsage : {}", id);
        Optional<HkjMaterialUsageDTO> hkjMaterialUsageDTO = hkjMaterialUsageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjMaterialUsageDTO);
    }

    /**
     * {@code DELETE  /hkj-material-usages/:id} : delete the "id" hkjMaterialUsage.
     *
     * @param id the id of the hkjMaterialUsageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjMaterialUsage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HkjMaterialUsage : {}", id);
        hkjMaterialUsageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
