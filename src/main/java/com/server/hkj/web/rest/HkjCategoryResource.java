package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjCategoryRepository;
import com.server.hkj.service.HkjCategoryQueryService;
import com.server.hkj.service.HkjCategoryService;
import com.server.hkj.service.criteria.HkjCategoryCriteria;
import com.server.hkj.service.dto.HkjCategoryDTO;
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
 * REST controller for managing {@link com.server.hkj.domain.HkjCategory}.
 */
@RestController
@RequestMapping("/api/hkj-categories")
public class HkjCategoryResource {

    private static final Logger LOG = LoggerFactory.getLogger(HkjCategoryResource.class);

    private static final String ENTITY_NAME = "hkjCategory";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjCategoryService hkjCategoryService;

    private final HkjCategoryRepository hkjCategoryRepository;

    private final HkjCategoryQueryService hkjCategoryQueryService;

    public HkjCategoryResource(
        HkjCategoryService hkjCategoryService,
        HkjCategoryRepository hkjCategoryRepository,
        HkjCategoryQueryService hkjCategoryQueryService
    ) {
        this.hkjCategoryService = hkjCategoryService;
        this.hkjCategoryRepository = hkjCategoryRepository;
        this.hkjCategoryQueryService = hkjCategoryQueryService;
    }

    /**
     * {@code POST  /hkj-categories} : Create a new hkjCategory.
     *
     * @param hkjCategoryDTO the hkjCategoryDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjCategoryDTO, or with status {@code 400 (Bad Request)} if the hkjCategory has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjCategoryDTO> createHkjCategory(@RequestBody HkjCategoryDTO hkjCategoryDTO) throws URISyntaxException {
        LOG.debug("REST request to save HkjCategory : {}", hkjCategoryDTO);
        if (hkjCategoryDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjCategory cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjCategoryDTO = hkjCategoryService.save(hkjCategoryDTO);
        return ResponseEntity.created(new URI("/api/hkj-categories/" + hkjCategoryDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjCategoryDTO.getId().toString()))
            .body(hkjCategoryDTO);
    }

    /**
     * {@code PUT  /hkj-categories/:id} : Updates an existing hkjCategory.
     *
     * @param id the id of the hkjCategoryDTO to save.
     * @param hkjCategoryDTO the hkjCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the hkjCategoryDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjCategoryDTO> updateHkjCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HkjCategoryDTO hkjCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HkjCategory : {}, {}", id, hkjCategoryDTO);
        if (hkjCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjCategoryDTO = hkjCategoryService.update(hkjCategoryDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjCategoryDTO.getId().toString()))
            .body(hkjCategoryDTO);
    }

    /**
     * {@code PATCH  /hkj-categories/:id} : Partial updates given fields of an existing hkjCategory, field will ignore if it is null
     *
     * @param id the id of the hkjCategoryDTO to save.
     * @param hkjCategoryDTO the hkjCategoryDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjCategoryDTO,
     * or with status {@code 400 (Bad Request)} if the hkjCategoryDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjCategoryDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjCategoryDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjCategoryDTO> partialUpdateHkjCategory(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HkjCategoryDTO hkjCategoryDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HkjCategory partially : {}, {}", id, hkjCategoryDTO);
        if (hkjCategoryDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjCategoryDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjCategoryRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjCategoryDTO> result = hkjCategoryService.partialUpdate(hkjCategoryDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjCategoryDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-categories} : get all the hkjCategories.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjCategories in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjCategoryDTO>> getAllHkjCategories(
        HkjCategoryCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HkjCategories by criteria: {}", criteria);

        Page<HkjCategoryDTO> page = hkjCategoryQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-categories/count} : count all the hkjCategories.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjCategories(HkjCategoryCriteria criteria) {
        LOG.debug("REST request to count HkjCategories by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjCategoryQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-categories/:id} : get the "id" hkjCategory.
     *
     * @param id the id of the hkjCategoryDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjCategoryDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjCategoryDTO> getHkjCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HkjCategory : {}", id);
        Optional<HkjCategoryDTO> hkjCategoryDTO = hkjCategoryService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjCategoryDTO);
    }

    /**
     * {@code DELETE  /hkj-categories/:id} : delete the "id" hkjCategory.
     *
     * @param id the id of the hkjCategoryDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjCategory(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HkjCategory : {}", id);
        hkjCategoryService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
