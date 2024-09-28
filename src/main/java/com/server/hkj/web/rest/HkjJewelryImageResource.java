package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjJewelryImageRepository;
import com.server.hkj.service.HkjJewelryImageQueryService;
import com.server.hkj.service.HkjJewelryImageService;
import com.server.hkj.service.criteria.HkjJewelryImageCriteria;
import com.server.hkj.service.dto.HkjJewelryImageDTO;
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
 * REST controller for managing {@link com.server.hkj.domain.HkjJewelryImage}.
 */
@RestController
@RequestMapping("/api/hkj-jewelry-images")
public class HkjJewelryImageResource {

    private static final Logger LOG = LoggerFactory.getLogger(HkjJewelryImageResource.class);

    private static final String ENTITY_NAME = "hkjJewelryImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjJewelryImageService hkjJewelryImageService;

    private final HkjJewelryImageRepository hkjJewelryImageRepository;

    private final HkjJewelryImageQueryService hkjJewelryImageQueryService;

    public HkjJewelryImageResource(
        HkjJewelryImageService hkjJewelryImageService,
        HkjJewelryImageRepository hkjJewelryImageRepository,
        HkjJewelryImageQueryService hkjJewelryImageQueryService
    ) {
        this.hkjJewelryImageService = hkjJewelryImageService;
        this.hkjJewelryImageRepository = hkjJewelryImageRepository;
        this.hkjJewelryImageQueryService = hkjJewelryImageQueryService;
    }

    /**
     * {@code POST  /hkj-jewelry-images} : Create a new hkjJewelryImage.
     *
     * @param hkjJewelryImageDTO the hkjJewelryImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjJewelryImageDTO, or with status {@code 400 (Bad Request)} if the hkjJewelryImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjJewelryImageDTO> createHkjJewelryImage(@Valid @RequestBody HkjJewelryImageDTO hkjJewelryImageDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save HkjJewelryImage : {}", hkjJewelryImageDTO);
        if (hkjJewelryImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjJewelryImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjJewelryImageDTO = hkjJewelryImageService.save(hkjJewelryImageDTO);
        return ResponseEntity.created(new URI("/api/hkj-jewelry-images/" + hkjJewelryImageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjJewelryImageDTO.getId().toString()))
            .body(hkjJewelryImageDTO);
    }

    /**
     * {@code PUT  /hkj-jewelry-images/:id} : Updates an existing hkjJewelryImage.
     *
     * @param id the id of the hkjJewelryImageDTO to save.
     * @param hkjJewelryImageDTO the hkjJewelryImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjJewelryImageDTO,
     * or with status {@code 400 (Bad Request)} if the hkjJewelryImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjJewelryImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjJewelryImageDTO> updateHkjJewelryImage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HkjJewelryImageDTO hkjJewelryImageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HkjJewelryImage : {}, {}", id, hkjJewelryImageDTO);
        if (hkjJewelryImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjJewelryImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjJewelryImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjJewelryImageDTO = hkjJewelryImageService.update(hkjJewelryImageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjJewelryImageDTO.getId().toString()))
            .body(hkjJewelryImageDTO);
    }

    /**
     * {@code PATCH  /hkj-jewelry-images/:id} : Partial updates given fields of an existing hkjJewelryImage, field will ignore if it is null
     *
     * @param id the id of the hkjJewelryImageDTO to save.
     * @param hkjJewelryImageDTO the hkjJewelryImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjJewelryImageDTO,
     * or with status {@code 400 (Bad Request)} if the hkjJewelryImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjJewelryImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjJewelryImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjJewelryImageDTO> partialUpdateHkjJewelryImage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HkjJewelryImageDTO hkjJewelryImageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HkjJewelryImage partially : {}, {}", id, hkjJewelryImageDTO);
        if (hkjJewelryImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjJewelryImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjJewelryImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjJewelryImageDTO> result = hkjJewelryImageService.partialUpdate(hkjJewelryImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjJewelryImageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-jewelry-images} : get all the hkjJewelryImages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjJewelryImages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjJewelryImageDTO>> getAllHkjJewelryImages(
        HkjJewelryImageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HkjJewelryImages by criteria: {}", criteria);

        Page<HkjJewelryImageDTO> page = hkjJewelryImageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-jewelry-images/count} : count all the hkjJewelryImages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjJewelryImages(HkjJewelryImageCriteria criteria) {
        LOG.debug("REST request to count HkjJewelryImages by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjJewelryImageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-jewelry-images/:id} : get the "id" hkjJewelryImage.
     *
     * @param id the id of the hkjJewelryImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjJewelryImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjJewelryImageDTO> getHkjJewelryImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HkjJewelryImage : {}", id);
        Optional<HkjJewelryImageDTO> hkjJewelryImageDTO = hkjJewelryImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjJewelryImageDTO);
    }

    /**
     * {@code DELETE  /hkj-jewelry-images/:id} : delete the "id" hkjJewelryImage.
     *
     * @param id the id of the hkjJewelryImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjJewelryImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HkjJewelryImage : {}", id);
        hkjJewelryImageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
