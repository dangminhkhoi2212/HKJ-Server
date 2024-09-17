package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjTaskImageRepository;
import com.server.hkj.service.HkjTaskImageQueryService;
import com.server.hkj.service.HkjTaskImageService;
import com.server.hkj.service.criteria.HkjTaskImageCriteria;
import com.server.hkj.service.dto.HkjTaskImageDTO;
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
 * REST controller for managing {@link com.server.hkj.domain.HkjTaskImage}.
 */
@RestController
@RequestMapping("/api/hkj-task-images")
public class HkjTaskImageResource {

    private static final Logger log = LoggerFactory.getLogger(HkjTaskImageResource.class);

    private static final String ENTITY_NAME = "hkjTaskImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjTaskImageService hkjTaskImageService;

    private final HkjTaskImageRepository hkjTaskImageRepository;

    private final HkjTaskImageQueryService hkjTaskImageQueryService;

    public HkjTaskImageResource(
        HkjTaskImageService hkjTaskImageService,
        HkjTaskImageRepository hkjTaskImageRepository,
        HkjTaskImageQueryService hkjTaskImageQueryService
    ) {
        this.hkjTaskImageService = hkjTaskImageService;
        this.hkjTaskImageRepository = hkjTaskImageRepository;
        this.hkjTaskImageQueryService = hkjTaskImageQueryService;
    }

    /**
     * {@code POST  /hkj-task-images} : Create a new hkjTaskImage.
     *
     * @param hkjTaskImageDTO the hkjTaskImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjTaskImageDTO, or with status {@code 400 (Bad Request)} if the hkjTaskImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjTaskImageDTO> createHkjTaskImage(@Valid @RequestBody HkjTaskImageDTO hkjTaskImageDTO)
        throws URISyntaxException {
        log.debug("REST request to save HkjTaskImage : {}", hkjTaskImageDTO);
        if (hkjTaskImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjTaskImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjTaskImageDTO = hkjTaskImageService.save(hkjTaskImageDTO);
        return ResponseEntity.created(new URI("/api/hkj-task-images/" + hkjTaskImageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjTaskImageDTO.getId().toString()))
            .body(hkjTaskImageDTO);
    }

    /**
     * {@code PUT  /hkj-task-images/:id} : Updates an existing hkjTaskImage.
     *
     * @param id the id of the hkjTaskImageDTO to save.
     * @param hkjTaskImageDTO the hkjTaskImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjTaskImageDTO,
     * or with status {@code 400 (Bad Request)} if the hkjTaskImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjTaskImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjTaskImageDTO> updateHkjTaskImage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HkjTaskImageDTO hkjTaskImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HkjTaskImage : {}, {}", id, hkjTaskImageDTO);
        if (hkjTaskImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjTaskImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjTaskImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjTaskImageDTO = hkjTaskImageService.update(hkjTaskImageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjTaskImageDTO.getId().toString()))
            .body(hkjTaskImageDTO);
    }

    /**
     * {@code PATCH  /hkj-task-images/:id} : Partial updates given fields of an existing hkjTaskImage, field will ignore if it is null
     *
     * @param id the id of the hkjTaskImageDTO to save.
     * @param hkjTaskImageDTO the hkjTaskImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjTaskImageDTO,
     * or with status {@code 400 (Bad Request)} if the hkjTaskImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjTaskImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjTaskImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjTaskImageDTO> partialUpdateHkjTaskImage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HkjTaskImageDTO hkjTaskImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HkjTaskImage partially : {}, {}", id, hkjTaskImageDTO);
        if (hkjTaskImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjTaskImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjTaskImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjTaskImageDTO> result = hkjTaskImageService.partialUpdate(hkjTaskImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjTaskImageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-task-images} : get all the hkjTaskImages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjTaskImages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjTaskImageDTO>> getAllHkjTaskImages(
        HkjTaskImageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get HkjTaskImages by criteria: {}", criteria);

        Page<HkjTaskImageDTO> page = hkjTaskImageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-task-images/count} : count all the hkjTaskImages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjTaskImages(HkjTaskImageCriteria criteria) {
        log.debug("REST request to count HkjTaskImages by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjTaskImageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-task-images/:id} : get the "id" hkjTaskImage.
     *
     * @param id the id of the hkjTaskImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjTaskImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjTaskImageDTO> getHkjTaskImage(@PathVariable("id") Long id) {
        log.debug("REST request to get HkjTaskImage : {}", id);
        Optional<HkjTaskImageDTO> hkjTaskImageDTO = hkjTaskImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjTaskImageDTO);
    }

    /**
     * {@code DELETE  /hkj-task-images/:id} : delete the "id" hkjTaskImage.
     *
     * @param id the id of the hkjTaskImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjTaskImage(@PathVariable("id") Long id) {
        log.debug("REST request to delete HkjTaskImage : {}", id);
        hkjTaskImageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
