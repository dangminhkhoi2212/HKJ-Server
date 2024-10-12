package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjTrackSearchImageRepository;
import com.server.hkj.service.HkjTrackSearchImageQueryService;
import com.server.hkj.service.HkjTrackSearchImageService;
import com.server.hkj.service.criteria.HkjTrackSearchImageCriteria;
import com.server.hkj.service.dto.HkjTrackSearchImageDTO;
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
 * REST controller for managing {@link com.server.hkj.domain.HkjTrackSearchImage}.
 */
@RestController
@RequestMapping("/api/hkj-track-search-images")
public class HkjTrackSearchImageResource {

    private static final Logger LOG = LoggerFactory.getLogger(HkjTrackSearchImageResource.class);

    private static final String ENTITY_NAME = "hkjTrackSearchImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjTrackSearchImageService hkjTrackSearchImageService;

    private final HkjTrackSearchImageRepository hkjTrackSearchImageRepository;

    private final HkjTrackSearchImageQueryService hkjTrackSearchImageQueryService;

    public HkjTrackSearchImageResource(
        HkjTrackSearchImageService hkjTrackSearchImageService,
        HkjTrackSearchImageRepository hkjTrackSearchImageRepository,
        HkjTrackSearchImageQueryService hkjTrackSearchImageQueryService
    ) {
        this.hkjTrackSearchImageService = hkjTrackSearchImageService;
        this.hkjTrackSearchImageRepository = hkjTrackSearchImageRepository;
        this.hkjTrackSearchImageQueryService = hkjTrackSearchImageQueryService;
    }

    /**
     * {@code POST  /hkj-track-search-images} : Create a new hkjTrackSearchImage.
     *
     * @param hkjTrackSearchImageDTO the hkjTrackSearchImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjTrackSearchImageDTO, or with status {@code 400 (Bad Request)} if the hkjTrackSearchImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjTrackSearchImageDTO> createHkjTrackSearchImage(@RequestBody HkjTrackSearchImageDTO hkjTrackSearchImageDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save HkjTrackSearchImage : {}", hkjTrackSearchImageDTO);
        if (hkjTrackSearchImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjTrackSearchImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjTrackSearchImageDTO = hkjTrackSearchImageService.save(hkjTrackSearchImageDTO);
        return ResponseEntity.created(new URI("/api/hkj-track-search-images/" + hkjTrackSearchImageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjTrackSearchImageDTO.getId().toString()))
            .body(hkjTrackSearchImageDTO);
    }

    /**
     * {@code PUT  /hkj-track-search-images/:id} : Updates an existing hkjTrackSearchImage.
     *
     * @param id the id of the hkjTrackSearchImageDTO to save.
     * @param hkjTrackSearchImageDTO the hkjTrackSearchImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjTrackSearchImageDTO,
     * or with status {@code 400 (Bad Request)} if the hkjTrackSearchImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjTrackSearchImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjTrackSearchImageDTO> updateHkjTrackSearchImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HkjTrackSearchImageDTO hkjTrackSearchImageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HkjTrackSearchImage : {}, {}", id, hkjTrackSearchImageDTO);
        if (hkjTrackSearchImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjTrackSearchImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjTrackSearchImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjTrackSearchImageDTO = hkjTrackSearchImageService.update(hkjTrackSearchImageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjTrackSearchImageDTO.getId().toString()))
            .body(hkjTrackSearchImageDTO);
    }

    /**
     * {@code PATCH  /hkj-track-search-images/:id} : Partial updates given fields of an existing hkjTrackSearchImage, field will ignore if it is null
     *
     * @param id the id of the hkjTrackSearchImageDTO to save.
     * @param hkjTrackSearchImageDTO the hkjTrackSearchImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjTrackSearchImageDTO,
     * or with status {@code 400 (Bad Request)} if the hkjTrackSearchImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjTrackSearchImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjTrackSearchImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjTrackSearchImageDTO> partialUpdateHkjTrackSearchImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HkjTrackSearchImageDTO hkjTrackSearchImageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HkjTrackSearchImage partially : {}, {}", id, hkjTrackSearchImageDTO);
        if (hkjTrackSearchImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjTrackSearchImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjTrackSearchImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjTrackSearchImageDTO> result = hkjTrackSearchImageService.partialUpdate(hkjTrackSearchImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjTrackSearchImageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-track-search-images} : get all the hkjTrackSearchImages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjTrackSearchImages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjTrackSearchImageDTO>> getAllHkjTrackSearchImages(
        HkjTrackSearchImageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HkjTrackSearchImages by criteria: {}", criteria);

        Page<HkjTrackSearchImageDTO> page = hkjTrackSearchImageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-track-search-images/count} : count all the hkjTrackSearchImages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjTrackSearchImages(HkjTrackSearchImageCriteria criteria) {
        LOG.debug("REST request to count HkjTrackSearchImages by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjTrackSearchImageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-track-search-images/:id} : get the "id" hkjTrackSearchImage.
     *
     * @param id the id of the hkjTrackSearchImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjTrackSearchImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjTrackSearchImageDTO> getHkjTrackSearchImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HkjTrackSearchImage : {}", id);
        Optional<HkjTrackSearchImageDTO> hkjTrackSearchImageDTO = hkjTrackSearchImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjTrackSearchImageDTO);
    }

    /**
     * {@code DELETE  /hkj-track-search-images/:id} : delete the "id" hkjTrackSearchImage.
     *
     * @param id the id of the hkjTrackSearchImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjTrackSearchImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HkjTrackSearchImage : {}", id);
        hkjTrackSearchImageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
