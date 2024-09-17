package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjMaterialRepository;
import com.server.hkj.service.HkjMaterialQueryService;
import com.server.hkj.service.HkjMaterialService;
import com.server.hkj.service.criteria.HkjMaterialCriteria;
import com.server.hkj.service.dto.HkjMaterialDTO;
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
 * REST controller for managing {@link com.server.hkj.domain.HkjMaterial}.
 */
@RestController
@RequestMapping("/api/hkj-materials")
public class HkjMaterialResource {

    private static final Logger log = LoggerFactory.getLogger(HkjMaterialResource.class);

    private static final String ENTITY_NAME = "hkjMaterial";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjMaterialService hkjMaterialService;

    private final HkjMaterialRepository hkjMaterialRepository;

    private final HkjMaterialQueryService hkjMaterialQueryService;

    public HkjMaterialResource(
        HkjMaterialService hkjMaterialService,
        HkjMaterialRepository hkjMaterialRepository,
        HkjMaterialQueryService hkjMaterialQueryService
    ) {
        this.hkjMaterialService = hkjMaterialService;
        this.hkjMaterialRepository = hkjMaterialRepository;
        this.hkjMaterialQueryService = hkjMaterialQueryService;
    }

    /**
     * {@code POST  /hkj-materials} : Create a new hkjMaterial.
     *
     * @param hkjMaterialDTO the hkjMaterialDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjMaterialDTO, or with status {@code 400 (Bad Request)} if the hkjMaterial has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjMaterialDTO> createHkjMaterial(@Valid @RequestBody HkjMaterialDTO hkjMaterialDTO) throws URISyntaxException {
        log.debug("REST request to save HkjMaterial : {}", hkjMaterialDTO);
        if (hkjMaterialDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjMaterial cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjMaterialDTO = hkjMaterialService.save(hkjMaterialDTO);
        return ResponseEntity.created(new URI("/api/hkj-materials/" + hkjMaterialDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjMaterialDTO.getId().toString()))
            .body(hkjMaterialDTO);
    }

    /**
     * {@code PUT  /hkj-materials/:id} : Updates an existing hkjMaterial.
     *
     * @param id the id of the hkjMaterialDTO to save.
     * @param hkjMaterialDTO the hkjMaterialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjMaterialDTO,
     * or with status {@code 400 (Bad Request)} if the hkjMaterialDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjMaterialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjMaterialDTO> updateHkjMaterial(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HkjMaterialDTO hkjMaterialDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HkjMaterial : {}, {}", id, hkjMaterialDTO);
        if (hkjMaterialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjMaterialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjMaterialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjMaterialDTO = hkjMaterialService.update(hkjMaterialDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjMaterialDTO.getId().toString()))
            .body(hkjMaterialDTO);
    }

    /**
     * {@code PATCH  /hkj-materials/:id} : Partial updates given fields of an existing hkjMaterial, field will ignore if it is null
     *
     * @param id the id of the hkjMaterialDTO to save.
     * @param hkjMaterialDTO the hkjMaterialDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjMaterialDTO,
     * or with status {@code 400 (Bad Request)} if the hkjMaterialDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjMaterialDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjMaterialDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjMaterialDTO> partialUpdateHkjMaterial(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HkjMaterialDTO hkjMaterialDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HkjMaterial partially : {}, {}", id, hkjMaterialDTO);
        if (hkjMaterialDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjMaterialDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjMaterialRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjMaterialDTO> result = hkjMaterialService.partialUpdate(hkjMaterialDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjMaterialDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-materials} : get all the hkjMaterials.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjMaterials in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjMaterialDTO>> getAllHkjMaterials(
        HkjMaterialCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get HkjMaterials by criteria: {}", criteria);

        Page<HkjMaterialDTO> page = hkjMaterialQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-materials/count} : count all the hkjMaterials.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjMaterials(HkjMaterialCriteria criteria) {
        log.debug("REST request to count HkjMaterials by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjMaterialQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-materials/:id} : get the "id" hkjMaterial.
     *
     * @param id the id of the hkjMaterialDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjMaterialDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjMaterialDTO> getHkjMaterial(@PathVariable("id") Long id) {
        log.debug("REST request to get HkjMaterial : {}", id);
        Optional<HkjMaterialDTO> hkjMaterialDTO = hkjMaterialService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjMaterialDTO);
    }

    /**
     * {@code DELETE  /hkj-materials/:id} : delete the "id" hkjMaterial.
     *
     * @param id the id of the hkjMaterialDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjMaterial(@PathVariable("id") Long id) {
        log.debug("REST request to delete HkjMaterial : {}", id);
        hkjMaterialService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
