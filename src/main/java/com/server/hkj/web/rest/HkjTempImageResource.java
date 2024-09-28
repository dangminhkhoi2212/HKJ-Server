package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjTempImageRepository;
import com.server.hkj.service.HkjTempImageQueryService;
import com.server.hkj.service.HkjTempImageService;
import com.server.hkj.service.criteria.HkjTempImageCriteria;
import com.server.hkj.service.dto.HkjTempImageDTO;
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
 * REST controller for managing {@link com.server.hkj.domain.HkjTempImage}.
 */
@RestController
@RequestMapping("/api/hkj-temp-images")
public class HkjTempImageResource {

    private static final Logger LOG = LoggerFactory.getLogger(HkjTempImageResource.class);

    private static final String ENTITY_NAME = "hkjTempImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjTempImageService hkjTempImageService;

    private final HkjTempImageRepository hkjTempImageRepository;

    private final HkjTempImageQueryService hkjTempImageQueryService;

    public HkjTempImageResource(
        HkjTempImageService hkjTempImageService,
        HkjTempImageRepository hkjTempImageRepository,
        HkjTempImageQueryService hkjTempImageQueryService
    ) {
        this.hkjTempImageService = hkjTempImageService;
        this.hkjTempImageRepository = hkjTempImageRepository;
        this.hkjTempImageQueryService = hkjTempImageQueryService;
    }

    /**
     * {@code POST  /hkj-temp-images} : Create a new hkjTempImage.
     *
     * @param hkjTempImageDTO the hkjTempImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjTempImageDTO, or with status {@code 400 (Bad Request)} if the hkjTempImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjTempImageDTO> createHkjTempImage(@Valid @RequestBody HkjTempImageDTO hkjTempImageDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save HkjTempImage : {}", hkjTempImageDTO);
        if (hkjTempImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjTempImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjTempImageDTO = hkjTempImageService.save(hkjTempImageDTO);
        return ResponseEntity.created(new URI("/api/hkj-temp-images/" + hkjTempImageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjTempImageDTO.getId().toString()))
            .body(hkjTempImageDTO);
    }

    /**
     * {@code PUT  /hkj-temp-images/:id} : Updates an existing hkjTempImage.
     *
     * @param id the id of the hkjTempImageDTO to save.
     * @param hkjTempImageDTO the hkjTempImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjTempImageDTO,
     * or with status {@code 400 (Bad Request)} if the hkjTempImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjTempImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjTempImageDTO> updateHkjTempImage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HkjTempImageDTO hkjTempImageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HkjTempImage : {}, {}", id, hkjTempImageDTO);
        if (hkjTempImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjTempImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjTempImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjTempImageDTO = hkjTempImageService.update(hkjTempImageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjTempImageDTO.getId().toString()))
            .body(hkjTempImageDTO);
    }

    /**
     * {@code PATCH  /hkj-temp-images/:id} : Partial updates given fields of an existing hkjTempImage, field will ignore if it is null
     *
     * @param id the id of the hkjTempImageDTO to save.
     * @param hkjTempImageDTO the hkjTempImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjTempImageDTO,
     * or with status {@code 400 (Bad Request)} if the hkjTempImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjTempImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjTempImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjTempImageDTO> partialUpdateHkjTempImage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HkjTempImageDTO hkjTempImageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HkjTempImage partially : {}, {}", id, hkjTempImageDTO);
        if (hkjTempImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjTempImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjTempImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjTempImageDTO> result = hkjTempImageService.partialUpdate(hkjTempImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjTempImageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-temp-images} : get all the hkjTempImages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjTempImages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjTempImageDTO>> getAllHkjTempImages(
        HkjTempImageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HkjTempImages by criteria: {}", criteria);

        Page<HkjTempImageDTO> page = hkjTempImageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-temp-images/count} : count all the hkjTempImages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjTempImages(HkjTempImageCriteria criteria) {
        LOG.debug("REST request to count HkjTempImages by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjTempImageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-temp-images/:id} : get the "id" hkjTempImage.
     *
     * @param id the id of the hkjTempImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjTempImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjTempImageDTO> getHkjTempImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HkjTempImage : {}", id);
        Optional<HkjTempImageDTO> hkjTempImageDTO = hkjTempImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjTempImageDTO);
    }

    /**
     * {@code DELETE  /hkj-temp-images/:id} : delete the "id" hkjTempImage.
     *
     * @param id the id of the hkjTempImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjTempImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HkjTempImage : {}", id);
        hkjTempImageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
