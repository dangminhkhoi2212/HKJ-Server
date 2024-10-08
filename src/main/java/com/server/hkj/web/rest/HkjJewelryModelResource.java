package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjJewelryModelRepository;
import com.server.hkj.service.HkjJewelryModelQueryService;
import com.server.hkj.service.HkjJewelryModelService;
import com.server.hkj.service.criteria.HkjJewelryModelCriteria;
import com.server.hkj.service.dto.HkjJewelryModelDTO;
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
 * REST controller for managing {@link com.server.hkj.domain.HkjJewelryModel}.
 */
@RestController
@RequestMapping("/api/hkj-jewelry-models")
public class HkjJewelryModelResource {

    private static final Logger LOG = LoggerFactory.getLogger(HkjJewelryModelResource.class);

    private static final String ENTITY_NAME = "hkjJewelryModel";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjJewelryModelService hkjJewelryModelService;

    private final HkjJewelryModelRepository hkjJewelryModelRepository;

    private final HkjJewelryModelQueryService hkjJewelryModelQueryService;

    public HkjJewelryModelResource(
        HkjJewelryModelService hkjJewelryModelService,
        HkjJewelryModelRepository hkjJewelryModelRepository,
        HkjJewelryModelQueryService hkjJewelryModelQueryService
    ) {
        this.hkjJewelryModelService = hkjJewelryModelService;
        this.hkjJewelryModelRepository = hkjJewelryModelRepository;
        this.hkjJewelryModelQueryService = hkjJewelryModelQueryService;
    }

    /**
     * {@code POST  /hkj-jewelry-models} : Create a new hkjJewelryModel.
     *
     * @param hkjJewelryModelDTO the hkjJewelryModelDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjJewelryModelDTO, or with status {@code 400 (Bad Request)} if the hkjJewelryModel has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjJewelryModelDTO> createHkjJewelryModel(@Valid @RequestBody HkjJewelryModelDTO hkjJewelryModelDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save HkjJewelryModel : {}", hkjJewelryModelDTO);
        if (hkjJewelryModelDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjJewelryModel cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjJewelryModelDTO = hkjJewelryModelService.save(hkjJewelryModelDTO);
        return ResponseEntity.created(new URI("/api/hkj-jewelry-models/" + hkjJewelryModelDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjJewelryModelDTO.getId().toString()))
            .body(hkjJewelryModelDTO);
    }

    /**
     * {@code PUT  /hkj-jewelry-models/:id} : Updates an existing hkjJewelryModel.
     *
     * @param id the id of the hkjJewelryModelDTO to save.
     * @param hkjJewelryModelDTO the hkjJewelryModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjJewelryModelDTO,
     * or with status {@code 400 (Bad Request)} if the hkjJewelryModelDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjJewelryModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjJewelryModelDTO> updateHkjJewelryModel(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HkjJewelryModelDTO hkjJewelryModelDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HkjJewelryModel : {}, {}", id, hkjJewelryModelDTO);
        if (hkjJewelryModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjJewelryModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjJewelryModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjJewelryModelDTO = hkjJewelryModelService.update(hkjJewelryModelDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjJewelryModelDTO.getId().toString()))
            .body(hkjJewelryModelDTO);
    }

    /**
     * {@code PATCH  /hkj-jewelry-models/:id} : Partial updates given fields of an existing hkjJewelryModel, field will ignore if it is null
     *
     * @param id the id of the hkjJewelryModelDTO to save.
     * @param hkjJewelryModelDTO the hkjJewelryModelDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjJewelryModelDTO,
     * or with status {@code 400 (Bad Request)} if the hkjJewelryModelDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjJewelryModelDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjJewelryModelDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjJewelryModelDTO> partialUpdateHkjJewelryModel(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HkjJewelryModelDTO hkjJewelryModelDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HkjJewelryModel partially : {}, {}", id, hkjJewelryModelDTO);
        if (hkjJewelryModelDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjJewelryModelDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjJewelryModelRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjJewelryModelDTO> result = hkjJewelryModelService.partialUpdate(hkjJewelryModelDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjJewelryModelDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-jewelry-models} : get all the hkjJewelryModels.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjJewelryModels in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjJewelryModelDTO>> getAllHkjJewelryModels(
        HkjJewelryModelCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HkjJewelryModels by criteria: {}", criteria);

        Page<HkjJewelryModelDTO> page = hkjJewelryModelQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-jewelry-models/count} : count all the hkjJewelryModels.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjJewelryModels(HkjJewelryModelCriteria criteria) {
        LOG.debug("REST request to count HkjJewelryModels by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjJewelryModelQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-jewelry-models/:id} : get the "id" hkjJewelryModel.
     *
     * @param id the id of the hkjJewelryModelDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjJewelryModelDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjJewelryModelDTO> getHkjJewelryModel(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HkjJewelryModel : {}", id);
        Optional<HkjJewelryModelDTO> hkjJewelryModelDTO = hkjJewelryModelService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjJewelryModelDTO);
    }

    /**
     * {@code DELETE  /hkj-jewelry-models/:id} : delete the "id" hkjJewelryModel.
     *
     * @param id the id of the hkjJewelryModelDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjJewelryModel(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HkjJewelryModel : {}", id);
        hkjJewelryModelService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
