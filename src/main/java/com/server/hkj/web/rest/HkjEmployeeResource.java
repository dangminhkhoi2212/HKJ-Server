package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjEmployeeRepository;
import com.server.hkj.service.HkjEmployeeQueryService;
import com.server.hkj.service.HkjEmployeeService;
import com.server.hkj.service.criteria.HkjEmployeeCriteria;
import com.server.hkj.service.dto.HkjEmployeeDTO;
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
 * REST controller for managing {@link com.server.hkj.domain.HkjEmployee}.
 */
@RestController
@RequestMapping("/api/hkj-employees")
public class HkjEmployeeResource {

    private static final Logger log = LoggerFactory.getLogger(HkjEmployeeResource.class);

    private static final String ENTITY_NAME = "hkjEmployee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjEmployeeService hkjEmployeeService;

    private final HkjEmployeeRepository hkjEmployeeRepository;

    private final HkjEmployeeQueryService hkjEmployeeQueryService;

    public HkjEmployeeResource(
        HkjEmployeeService hkjEmployeeService,
        HkjEmployeeRepository hkjEmployeeRepository,
        HkjEmployeeQueryService hkjEmployeeQueryService
    ) {
        this.hkjEmployeeService = hkjEmployeeService;
        this.hkjEmployeeRepository = hkjEmployeeRepository;
        this.hkjEmployeeQueryService = hkjEmployeeQueryService;
    }

    /**
     * {@code POST  /hkj-employees} : Create a new hkjEmployee.
     *
     * @param hkjEmployeeDTO the hkjEmployeeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjEmployeeDTO, or with status {@code 400 (Bad Request)} if the hkjEmployee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjEmployeeDTO> createHkjEmployee(@Valid @RequestBody HkjEmployeeDTO hkjEmployeeDTO) throws URISyntaxException {
        log.debug("REST request to save HkjEmployee : {}", hkjEmployeeDTO);
        if (hkjEmployeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjEmployee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjEmployeeDTO = hkjEmployeeService.save(hkjEmployeeDTO);
        return ResponseEntity.created(new URI("/api/hkj-employees/" + hkjEmployeeDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjEmployeeDTO.getId().toString()))
            .body(hkjEmployeeDTO);
    }

    /**
     * {@code PUT  /hkj-employees/:id} : Updates an existing hkjEmployee.
     *
     * @param id the id of the hkjEmployeeDTO to save.
     * @param hkjEmployeeDTO the hkjEmployeeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjEmployeeDTO,
     * or with status {@code 400 (Bad Request)} if the hkjEmployeeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjEmployeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjEmployeeDTO> updateHkjEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HkjEmployeeDTO hkjEmployeeDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HkjEmployee : {}, {}", id, hkjEmployeeDTO);
        if (hkjEmployeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjEmployeeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjEmployeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjEmployeeDTO = hkjEmployeeService.update(hkjEmployeeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjEmployeeDTO.getId().toString()))
            .body(hkjEmployeeDTO);
    }

    /**
     * {@code PATCH  /hkj-employees/:id} : Partial updates given fields of an existing hkjEmployee, field will ignore if it is null
     *
     * @param id the id of the hkjEmployeeDTO to save.
     * @param hkjEmployeeDTO the hkjEmployeeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjEmployeeDTO,
     * or with status {@code 400 (Bad Request)} if the hkjEmployeeDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjEmployeeDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjEmployeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjEmployeeDTO> partialUpdateHkjEmployee(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HkjEmployeeDTO hkjEmployeeDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HkjEmployee partially : {}, {}", id, hkjEmployeeDTO);
        if (hkjEmployeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjEmployeeDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjEmployeeRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjEmployeeDTO> result = hkjEmployeeService.partialUpdate(hkjEmployeeDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjEmployeeDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-employees} : get all the hkjEmployees.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjEmployees in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjEmployeeDTO>> getAllHkjEmployees(
        HkjEmployeeCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get HkjEmployees by criteria: {}", criteria);

        Page<HkjEmployeeDTO> page = hkjEmployeeQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-employees/count} : count all the hkjEmployees.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjEmployees(HkjEmployeeCriteria criteria) {
        log.debug("REST request to count HkjEmployees by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjEmployeeQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-employees/:id} : get the "id" hkjEmployee.
     *
     * @param id the id of the hkjEmployeeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjEmployeeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjEmployeeDTO> getHkjEmployee(@PathVariable("id") Long id) {
        log.debug("REST request to get HkjEmployee : {}", id);
        Optional<HkjEmployeeDTO> hkjEmployeeDTO = hkjEmployeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjEmployeeDTO);
    }

    /**
     * {@code DELETE  /hkj-employees/:id} : delete the "id" hkjEmployee.
     *
     * @param id the id of the hkjEmployeeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjEmployee(@PathVariable("id") Long id) {
        log.debug("REST request to delete HkjEmployee : {}", id);
        hkjEmployeeService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
