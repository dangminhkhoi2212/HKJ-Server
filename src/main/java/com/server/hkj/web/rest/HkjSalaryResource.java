package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjSalaryRepository;
import com.server.hkj.service.HkjSalaryQueryService;
import com.server.hkj.service.HkjSalaryService;
import com.server.hkj.service.criteria.HkjSalaryCriteria;
import com.server.hkj.service.dto.HkjSalaryDTO;
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
 * REST controller for managing {@link com.server.hkj.domain.HkjSalary}.
 */
@RestController
@RequestMapping("/api/hkj-salaries")
public class HkjSalaryResource {

    private static final Logger log = LoggerFactory.getLogger(HkjSalaryResource.class);

    private static final String ENTITY_NAME = "hkjSalary";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjSalaryService hkjSalaryService;

    private final HkjSalaryRepository hkjSalaryRepository;

    private final HkjSalaryQueryService hkjSalaryQueryService;

    public HkjSalaryResource(
        HkjSalaryService hkjSalaryService,
        HkjSalaryRepository hkjSalaryRepository,
        HkjSalaryQueryService hkjSalaryQueryService
    ) {
        this.hkjSalaryService = hkjSalaryService;
        this.hkjSalaryRepository = hkjSalaryRepository;
        this.hkjSalaryQueryService = hkjSalaryQueryService;
    }

    /**
     * {@code POST  /hkj-salaries} : Create a new hkjSalary.
     *
     * @param hkjSalaryDTO the hkjSalaryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjSalaryDTO, or with status {@code 400 (Bad Request)} if the hkjSalary has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjSalaryDTO> createHkjSalary(@RequestBody HkjSalaryDTO hkjSalaryDTO) throws URISyntaxException {
        log.debug("REST request to save HkjSalary : {}", hkjSalaryDTO);
        if (hkjSalaryDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjSalary cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjSalaryDTO = hkjSalaryService.save(hkjSalaryDTO);
        return ResponseEntity.created(new URI("/api/hkj-salaries/" + hkjSalaryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjSalaryDTO.getId().toString()))
            .body(hkjSalaryDTO);
    }

    /**
     * {@code PUT  /hkj-salaries/:id} : Updates an existing hkjSalary.
     *
     * @param id the id of the hkjSalaryDTO to save.
     * @param hkjSalaryDTO the hkjSalaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjSalaryDTO,
     * or with status {@code 400 (Bad Request)} if the hkjSalaryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjSalaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjSalaryDTO> updateHkjSalary(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HkjSalaryDTO hkjSalaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HkjSalary : {}, {}", id, hkjSalaryDTO);
        if (hkjSalaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjSalaryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjSalaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjSalaryDTO = hkjSalaryService.update(hkjSalaryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjSalaryDTO.getId().toString()))
            .body(hkjSalaryDTO);
    }

    /**
     * {@code PATCH  /hkj-salaries/:id} : Partial updates given fields of an existing hkjSalary, field will ignore if it is null
     *
     * @param id the id of the hkjSalaryDTO to save.
     * @param hkjSalaryDTO the hkjSalaryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjSalaryDTO,
     * or with status {@code 400 (Bad Request)} if the hkjSalaryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjSalaryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjSalaryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjSalaryDTO> partialUpdateHkjSalary(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HkjSalaryDTO hkjSalaryDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HkjSalary partially : {}, {}", id, hkjSalaryDTO);
        if (hkjSalaryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjSalaryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjSalaryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjSalaryDTO> result = hkjSalaryService.partialUpdate(hkjSalaryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjSalaryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-salaries} : get all the hkjSalaries.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjSalaries in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjSalaryDTO>> getAllHkjSalaries(
        HkjSalaryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get HkjSalaries by criteria: {}", criteria);

        Page<HkjSalaryDTO> page = hkjSalaryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-salaries/count} : count all the hkjSalaries.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjSalaries(HkjSalaryCriteria criteria) {
        log.debug("REST request to count HkjSalaries by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjSalaryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-salaries/:id} : get the "id" hkjSalary.
     *
     * @param id the id of the hkjSalaryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjSalaryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjSalaryDTO> getHkjSalary(@PathVariable("id") Long id) {
        log.debug("REST request to get HkjSalary : {}", id);
        Optional<HkjSalaryDTO> hkjSalaryDTO = hkjSalaryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjSalaryDTO);
    }

    /**
     * {@code DELETE  /hkj-salaries/:id} : delete the "id" hkjSalary.
     *
     * @param id the id of the hkjSalaryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjSalary(@PathVariable("id") Long id) {
        log.debug("REST request to delete HkjSalary : {}", id);
        hkjSalaryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
