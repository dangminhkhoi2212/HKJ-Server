package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjTemplateRepository;
import com.server.hkj.service.HkjTemplateQueryService;
import com.server.hkj.service.HkjTemplateService;
import com.server.hkj.service.criteria.HkjTemplateCriteria;
import com.server.hkj.service.dto.HkjTemplateDTO;
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
 * REST controller for managing {@link com.server.hkj.domain.HkjTemplate}.
 */
@RestController
@RequestMapping("/api/hkj-templates")
public class HkjTemplateResource {

    private static final Logger log = LoggerFactory.getLogger(HkjTemplateResource.class);

    private static final String ENTITY_NAME = "hkjTemplate";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjTemplateService hkjTemplateService;

    private final HkjTemplateRepository hkjTemplateRepository;

    private final HkjTemplateQueryService hkjTemplateQueryService;

    public HkjTemplateResource(
        HkjTemplateService hkjTemplateService,
        HkjTemplateRepository hkjTemplateRepository,
        HkjTemplateQueryService hkjTemplateQueryService
    ) {
        this.hkjTemplateService = hkjTemplateService;
        this.hkjTemplateRepository = hkjTemplateRepository;
        this.hkjTemplateQueryService = hkjTemplateQueryService;
    }

    /**
     * {@code POST  /hkj-templates} : Create a new hkjTemplate.
     *
     * @param hkjTemplateDTO the hkjTemplateDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjTemplateDTO, or with status {@code 400 (Bad Request)} if the hkjTemplate has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjTemplateDTO> createHkjTemplate(@RequestBody HkjTemplateDTO hkjTemplateDTO) throws URISyntaxException {
        log.debug("REST request to save HkjTemplate : {}", hkjTemplateDTO);
        if (hkjTemplateDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjTemplate cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjTemplateDTO = hkjTemplateService.save(hkjTemplateDTO);
        return ResponseEntity.created(new URI("/api/hkj-templates/" + hkjTemplateDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjTemplateDTO.getId().toString()))
            .body(hkjTemplateDTO);
    }

    /**
     * {@code PUT  /hkj-templates/:id} : Updates an existing hkjTemplate.
     *
     * @param id the id of the hkjTemplateDTO to save.
     * @param hkjTemplateDTO the hkjTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the hkjTemplateDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjTemplateDTO> updateHkjTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HkjTemplateDTO hkjTemplateDTO
    ) throws URISyntaxException {
        log.debug("REST request to update HkjTemplate : {}, {}", id, hkjTemplateDTO);
        if (hkjTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjTemplateDTO = hkjTemplateService.update(hkjTemplateDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjTemplateDTO.getId().toString()))
            .body(hkjTemplateDTO);
    }

    /**
     * {@code PATCH  /hkj-templates/:id} : Partial updates given fields of an existing hkjTemplate, field will ignore if it is null
     *
     * @param id the id of the hkjTemplateDTO to save.
     * @param hkjTemplateDTO the hkjTemplateDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjTemplateDTO,
     * or with status {@code 400 (Bad Request)} if the hkjTemplateDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjTemplateDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjTemplateDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjTemplateDTO> partialUpdateHkjTemplate(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HkjTemplateDTO hkjTemplateDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update HkjTemplate partially : {}, {}", id, hkjTemplateDTO);
        if (hkjTemplateDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjTemplateDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjTemplateRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjTemplateDTO> result = hkjTemplateService.partialUpdate(hkjTemplateDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjTemplateDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-templates} : get all the hkjTemplates.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjTemplates in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjTemplateDTO>> getAllHkjTemplates(
        HkjTemplateCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get HkjTemplates by criteria: {}", criteria);

        Page<HkjTemplateDTO> page = hkjTemplateQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-templates/count} : count all the hkjTemplates.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjTemplates(HkjTemplateCriteria criteria) {
        log.debug("REST request to count HkjTemplates by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjTemplateQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-templates/:id} : get the "id" hkjTemplate.
     *
     * @param id the id of the hkjTemplateDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjTemplateDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjTemplateDTO> getHkjTemplate(@PathVariable("id") Long id) {
        log.debug("REST request to get HkjTemplate : {}", id);
        Optional<HkjTemplateDTO> hkjTemplateDTO = hkjTemplateService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjTemplateDTO);
    }

    /**
     * {@code DELETE  /hkj-templates/:id} : delete the "id" hkjTemplate.
     *
     * @param id the id of the hkjTemplateDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjTemplate(@PathVariable("id") Long id) {
        log.debug("REST request to delete HkjTemplate : {}", id);
        hkjTemplateService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
