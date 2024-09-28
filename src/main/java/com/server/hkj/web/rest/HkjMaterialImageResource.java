package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjMaterialImageRepository;
import com.server.hkj.service.HkjMaterialImageQueryService;
import com.server.hkj.service.HkjMaterialImageService;
import com.server.hkj.service.criteria.HkjMaterialImageCriteria;
import com.server.hkj.service.dto.HkjMaterialImageDTO;
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
 * REST controller for managing {@link com.server.hkj.domain.HkjMaterialImage}.
 */
@RestController
@RequestMapping("/api/hkj-material-images")
public class HkjMaterialImageResource {

    private static final Logger LOG = LoggerFactory.getLogger(HkjMaterialImageResource.class);

    private static final String ENTITY_NAME = "hkjMaterialImage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjMaterialImageService hkjMaterialImageService;

    private final HkjMaterialImageRepository hkjMaterialImageRepository;

    private final HkjMaterialImageQueryService hkjMaterialImageQueryService;

    public HkjMaterialImageResource(
        HkjMaterialImageService hkjMaterialImageService,
        HkjMaterialImageRepository hkjMaterialImageRepository,
        HkjMaterialImageQueryService hkjMaterialImageQueryService
    ) {
        this.hkjMaterialImageService = hkjMaterialImageService;
        this.hkjMaterialImageRepository = hkjMaterialImageRepository;
        this.hkjMaterialImageQueryService = hkjMaterialImageQueryService;
    }

    /**
     * {@code POST  /hkj-material-images} : Create a new hkjMaterialImage.
     *
     * @param hkjMaterialImageDTO the hkjMaterialImageDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjMaterialImageDTO, or with status {@code 400 (Bad Request)} if the hkjMaterialImage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjMaterialImageDTO> createHkjMaterialImage(@RequestBody HkjMaterialImageDTO hkjMaterialImageDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save HkjMaterialImage : {}", hkjMaterialImageDTO);
        if (hkjMaterialImageDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjMaterialImage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjMaterialImageDTO = hkjMaterialImageService.save(hkjMaterialImageDTO);
        return ResponseEntity.created(new URI("/api/hkj-material-images/" + hkjMaterialImageDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjMaterialImageDTO.getId().toString()))
            .body(hkjMaterialImageDTO);
    }

    /**
     * {@code PUT  /hkj-material-images/:id} : Updates an existing hkjMaterialImage.
     *
     * @param id the id of the hkjMaterialImageDTO to save.
     * @param hkjMaterialImageDTO the hkjMaterialImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjMaterialImageDTO,
     * or with status {@code 400 (Bad Request)} if the hkjMaterialImageDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjMaterialImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjMaterialImageDTO> updateHkjMaterialImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HkjMaterialImageDTO hkjMaterialImageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HkjMaterialImage : {}, {}", id, hkjMaterialImageDTO);
        if (hkjMaterialImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjMaterialImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjMaterialImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjMaterialImageDTO = hkjMaterialImageService.update(hkjMaterialImageDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjMaterialImageDTO.getId().toString()))
            .body(hkjMaterialImageDTO);
    }

    /**
     * {@code PATCH  /hkj-material-images/:id} : Partial updates given fields of an existing hkjMaterialImage, field will ignore if it is null
     *
     * @param id the id of the hkjMaterialImageDTO to save.
     * @param hkjMaterialImageDTO the hkjMaterialImageDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjMaterialImageDTO,
     * or with status {@code 400 (Bad Request)} if the hkjMaterialImageDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjMaterialImageDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjMaterialImageDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjMaterialImageDTO> partialUpdateHkjMaterialImage(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HkjMaterialImageDTO hkjMaterialImageDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HkjMaterialImage partially : {}, {}", id, hkjMaterialImageDTO);
        if (hkjMaterialImageDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjMaterialImageDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjMaterialImageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjMaterialImageDTO> result = hkjMaterialImageService.partialUpdate(hkjMaterialImageDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjMaterialImageDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-material-images} : get all the hkjMaterialImages.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjMaterialImages in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjMaterialImageDTO>> getAllHkjMaterialImages(
        HkjMaterialImageCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HkjMaterialImages by criteria: {}", criteria);

        Page<HkjMaterialImageDTO> page = hkjMaterialImageQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-material-images/count} : count all the hkjMaterialImages.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjMaterialImages(HkjMaterialImageCriteria criteria) {
        LOG.debug("REST request to count HkjMaterialImages by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjMaterialImageQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-material-images/:id} : get the "id" hkjMaterialImage.
     *
     * @param id the id of the hkjMaterialImageDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjMaterialImageDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjMaterialImageDTO> getHkjMaterialImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HkjMaterialImage : {}", id);
        Optional<HkjMaterialImageDTO> hkjMaterialImageDTO = hkjMaterialImageService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjMaterialImageDTO);
    }

    /**
     * {@code DELETE  /hkj-material-images/:id} : delete the "id" hkjMaterialImage.
     *
     * @param id the id of the hkjMaterialImageDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjMaterialImage(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HkjMaterialImage : {}", id);
        hkjMaterialImageService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
