package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjCartRepository;
import com.server.hkj.service.HkjCartQueryService;
import com.server.hkj.service.HkjCartService;
import com.server.hkj.service.criteria.HkjCartCriteria;
import com.server.hkj.service.dto.HkjCartDTO;
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
 * REST controller for managing {@link com.server.hkj.domain.HkjCart}.
 */
@RestController
@RequestMapping("/api/hkj-carts")
public class HkjCartResource {

    private static final Logger LOG = LoggerFactory.getLogger(HkjCartResource.class);

    private static final String ENTITY_NAME = "hkjCart";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjCartService hkjCartService;

    private final HkjCartRepository hkjCartRepository;

    private final HkjCartQueryService hkjCartQueryService;

    public HkjCartResource(HkjCartService hkjCartService, HkjCartRepository hkjCartRepository, HkjCartQueryService hkjCartQueryService) {
        this.hkjCartService = hkjCartService;
        this.hkjCartRepository = hkjCartRepository;
        this.hkjCartQueryService = hkjCartQueryService;
    }

    /**
     * {@code POST  /hkj-carts} : Create a new hkjCart.
     *
     * @param hkjCartDTO the hkjCartDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjCartDTO, or with status {@code 400 (Bad Request)} if the hkjCart has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjCartDTO> createHkjCart(@RequestBody HkjCartDTO hkjCartDTO) throws URISyntaxException {
        LOG.debug("REST request to save HkjCart : {}", hkjCartDTO);
        if (hkjCartDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjCart cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjCartDTO = hkjCartService.save(hkjCartDTO);
        return ResponseEntity.created(new URI("/api/hkj-carts/" + hkjCartDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjCartDTO.getId().toString()))
            .body(hkjCartDTO);
    }

    /**
     * {@code PUT  /hkj-carts/:id} : Updates an existing hkjCart.
     *
     * @param id the id of the hkjCartDTO to save.
     * @param hkjCartDTO the hkjCartDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjCartDTO,
     * or with status {@code 400 (Bad Request)} if the hkjCartDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjCartDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjCartDTO> updateHkjCart(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HkjCartDTO hkjCartDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HkjCart : {}, {}", id, hkjCartDTO);
        if (hkjCartDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjCartDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjCartRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjCartDTO = hkjCartService.update(hkjCartDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjCartDTO.getId().toString()))
            .body(hkjCartDTO);
    }

    /**
     * {@code PATCH  /hkj-carts/:id} : Partial updates given fields of an existing hkjCart, field will ignore if it is null
     *
     * @param id the id of the hkjCartDTO to save.
     * @param hkjCartDTO the hkjCartDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjCartDTO,
     * or with status {@code 400 (Bad Request)} if the hkjCartDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjCartDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjCartDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjCartDTO> partialUpdateHkjCart(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody HkjCartDTO hkjCartDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HkjCart partially : {}, {}", id, hkjCartDTO);
        if (hkjCartDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjCartDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjCartRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjCartDTO> result = hkjCartService.partialUpdate(hkjCartDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjCartDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-carts} : get all the hkjCarts.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjCarts in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjCartDTO>> getAllHkjCarts(
        HkjCartCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HkjCarts by criteria: {}", criteria);

        Page<HkjCartDTO> page = hkjCartQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-carts/count} : count all the hkjCarts.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjCarts(HkjCartCriteria criteria) {
        LOG.debug("REST request to count HkjCarts by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjCartQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-carts/:id} : get the "id" hkjCart.
     *
     * @param id the id of the hkjCartDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjCartDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjCartDTO> getHkjCart(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HkjCart : {}", id);
        Optional<HkjCartDTO> hkjCartDTO = hkjCartService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjCartDTO);
    }

    /**
     * {@code DELETE  /hkj-carts/:id} : delete the "id" hkjCart.
     *
     * @param id the id of the hkjCartDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjCart(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HkjCart : {}", id);
        hkjCartService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
