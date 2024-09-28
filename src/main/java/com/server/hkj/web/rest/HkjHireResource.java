package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjHireRepository;
import com.server.hkj.service.HkjHireQueryService;
import com.server.hkj.service.HkjHireService;
import com.server.hkj.service.criteria.HkjHireCriteria;
import com.server.hkj.service.dto.HkjHireDTO;
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
 * REST controller for managing {@link com.server.hkj.domain.HkjHire}.
 */
@RestController
@RequestMapping("/api/hkj-hires")
public class HkjHireResource {

    private static final Logger LOG = LoggerFactory.getLogger(HkjHireResource.class);

    private static final String ENTITY_NAME = "hkjHire";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjHireService hkjHireService;

    private final HkjHireRepository hkjHireRepository;

    private final HkjHireQueryService hkjHireQueryService;

    public HkjHireResource(HkjHireService hkjHireService, HkjHireRepository hkjHireRepository, HkjHireQueryService hkjHireQueryService) {
        this.hkjHireService = hkjHireService;
        this.hkjHireRepository = hkjHireRepository;
        this.hkjHireQueryService = hkjHireQueryService;
    }

    /**
     * {@code POST  /hkj-hires} : Create a new hkjHire.
     *
     * @param hkjHireDTO the hkjHireDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjHireDTO, or with status {@code 400 (Bad Request)} if the hkjHire has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjHireDTO> createHkjHire(@Valid @RequestBody HkjHireDTO hkjHireDTO) throws URISyntaxException {
        LOG.debug("REST request to save HkjHire : {}", hkjHireDTO);
        if (hkjHireDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjHire cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjHireDTO = hkjHireService.save(hkjHireDTO);
        return ResponseEntity.created(new URI("/api/hkj-hires/" + hkjHireDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjHireDTO.getId().toString()))
            .body(hkjHireDTO);
    }

    /**
     * {@code PUT  /hkj-hires/:id} : Updates an existing hkjHire.
     *
     * @param id the id of the hkjHireDTO to save.
     * @param hkjHireDTO the hkjHireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjHireDTO,
     * or with status {@code 400 (Bad Request)} if the hkjHireDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjHireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjHireDTO> updateHkjHire(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HkjHireDTO hkjHireDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HkjHire : {}, {}", id, hkjHireDTO);
        if (hkjHireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjHireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjHireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjHireDTO = hkjHireService.update(hkjHireDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjHireDTO.getId().toString()))
            .body(hkjHireDTO);
    }

    /**
     * {@code PATCH  /hkj-hires/:id} : Partial updates given fields of an existing hkjHire, field will ignore if it is null
     *
     * @param id the id of the hkjHireDTO to save.
     * @param hkjHireDTO the hkjHireDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjHireDTO,
     * or with status {@code 400 (Bad Request)} if the hkjHireDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjHireDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjHireDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjHireDTO> partialUpdateHkjHire(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HkjHireDTO hkjHireDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HkjHire partially : {}, {}", id, hkjHireDTO);
        if (hkjHireDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjHireDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjHireRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjHireDTO> result = hkjHireService.partialUpdate(hkjHireDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjHireDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-hires} : get all the hkjHires.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjHires in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjHireDTO>> getAllHkjHires(
        HkjHireCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HkjHires by criteria: {}", criteria);

        Page<HkjHireDTO> page = hkjHireQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-hires/count} : count all the hkjHires.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjHires(HkjHireCriteria criteria) {
        LOG.debug("REST request to count HkjHires by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjHireQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-hires/:id} : get the "id" hkjHire.
     *
     * @param id the id of the hkjHireDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjHireDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjHireDTO> getHkjHire(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HkjHire : {}", id);
        Optional<HkjHireDTO> hkjHireDTO = hkjHireService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjHireDTO);
    }

    /**
     * {@code DELETE  /hkj-hires/:id} : delete the "id" hkjHire.
     *
     * @param id the id of the hkjHireDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjHire(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HkjHire : {}", id);
        hkjHireService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
