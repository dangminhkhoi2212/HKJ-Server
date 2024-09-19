package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjOrderImageRepository;
import com.server.hkj.service.HkjOrderImageQueryService;
import com.server.hkj.service.HkjOrderImageService;
import com.server.hkj.service.criteria.HkjOrderImageCriteria;
import com.server.hkj.service.dto.HkjOrderImageDTO;
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
 * REST controller for managing {@link com.server.hkj.domain.HkjOrderImage}.
 */
@RestController
@RequestMapping("/api/hkj-order-images")
public class HkjOrderImageResource {

    private static final Logger log = LoggerFactory.getLogger(HkjOrderImageResource.class);

    private static final String ENTITY_NAME = "hkjOrderImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjOrderImageService hkjOrderImageService;

    private final HkjOrderImageRepository hkjOrderImageRepository;

    private final HkjOrderImageQueryService hkjOrderImageQueryService;

    public HkjOrderImageResource(
        HkjOrderImageService hkjOrderImageService,
        HkjOrderImageRepository hkjOrderImageRepository,
        HkjOrderImageQueryService hkjOrderImageQueryService
    ) {
        this.hkjOrderImageService = hkjOrderImageService;
        this.hkjOrderImageRepository = hkjOrderImageRepository;
        this.hkjOrderImageQueryService = hkjOrderImageQueryService;
    }

    /**
     * {@code POST  /hkj-order-images} : Create a new hkjOrderImage.
     *
     * @param hkjOrderImageDTO the hkjOrderImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjOrderImageDTO, or with status {@code 400 (Bad Request)} if the hkjOrderImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjOrderImageDTO> createHkjOrderImage(@RequestBody HkjOrderImageDTO hkjOrderImageDTO) throws URISyntaxException {
        log.debug("REST request to save HkjOrderImage : {}", hkjOrderImageDTO);
        if (hkjOrderImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjOrderImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjOrderImageDTO = hkjOrderImageService.save(hkjOrderImageDTO);
        return ResponseEntity.created(new URI("/api/hkj-order-images/" + hkjOrderImageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjOrderImageDTO.getId().toString()))
            .body(hkjOrderImageDTO);
    }

    /**
     * {@code PUT  /hkj-order-images/:id} : Updates an existing hkjOrderImage.
     *
     * @param id the id of the hkjOrderImageDTO to save.
     * @param hkjOrderImageDTO the hkjOrderImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjOrderImageDTO,
     * or with status {@code 400 (Bad Request)} if the hkjOrderImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjOrderImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjOrderImageDTO> updateHkjOrderImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HkjOrderImageDTO hkjOrderImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HkjOrderImage : {}, {}", id, hkjOrderImageDTO);
        if (hkjOrderImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjOrderImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjOrderImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjOrderImageDTO = hkjOrderImageService.update(hkjOrderImageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjOrderImageDTO.getId().toString()))
            .body(hkjOrderImageDTO);
    }

    /**
     * {@code PATCH  /hkj-order-images/:id} : Partial updates given fields of an existing hkjOrderImage, field will ignore if it is null
     *
     * @param id the id of the hkjOrderImageDTO to save.
     * @param hkjOrderImageDTO the hkjOrderImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjOrderImageDTO,
     * or with status {@code 400 (Bad Request)} if the hkjOrderImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjOrderImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjOrderImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjOrderImageDTO> partialUpdateHkjOrderImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HkjOrderImageDTO hkjOrderImageDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HkjOrderImage partially : {}, {}", id, hkjOrderImageDTO);
        if (hkjOrderImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjOrderImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjOrderImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjOrderImageDTO> result = hkjOrderImageService.partialUpdate(hkjOrderImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjOrderImageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-order-images} : get all the hkjOrderImages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjOrderImages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjOrderImageDTO>> getAllHkjOrderImages(
        HkjOrderImageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get HkjOrderImages by criteria: {}", criteria);

        Page<HkjOrderImageDTO> page = hkjOrderImageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-order-images/count} : count all the hkjOrderImages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjOrderImages(HkjOrderImageCriteria criteria) {
        log.debug("REST request to count HkjOrderImages by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjOrderImageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-order-images/:id} : get the "id" hkjOrderImage.
     *
     * @param id the id of the hkjOrderImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjOrderImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjOrderImageDTO> getHkjOrderImage(@PathVariable("id") Long id) {
        log.debug("REST request to get HkjOrderImage : {}", id);
        Optional<HkjOrderImageDTO> hkjOrderImageDTO = hkjOrderImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjOrderImageDTO);
    }

    /**
     * {@code DELETE  /hkj-order-images/:id} : delete the "id" hkjOrderImage.
     *
     * @param id the id of the hkjOrderImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjOrderImage(@PathVariable("id") Long id) {
        log.debug("REST request to delete HkjOrderImage : {}", id);
        hkjOrderImageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
