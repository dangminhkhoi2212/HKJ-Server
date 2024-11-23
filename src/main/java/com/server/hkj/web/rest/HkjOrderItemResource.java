package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjOrderItemRepository;
import com.server.hkj.service.HkjOrderItemQueryService;
import com.server.hkj.service.HkjOrderItemService;
import com.server.hkj.service.criteria.HkjOrderItemCriteria;
import com.server.hkj.service.dto.HkjOrderItemDTO;
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
 * REST controller for managing {@link com.server.hkj.domain.HkjOrderItem}.
 */
@RestController
@RequestMapping("/api/hkj-order-items")
public class HkjOrderItemResource {

    private static final Logger LOG = LoggerFactory.getLogger(HkjOrderItemResource.class);

    private static final String ENTITY_NAME = "hkjOrderItem";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjOrderItemService hkjOrderItemService;

    private final HkjOrderItemRepository hkjOrderItemRepository;

    private final HkjOrderItemQueryService hkjOrderItemQueryService;

    public HkjOrderItemResource(
        HkjOrderItemService hkjOrderItemService,
        HkjOrderItemRepository hkjOrderItemRepository,
        HkjOrderItemQueryService hkjOrderItemQueryService
    ) {
        this.hkjOrderItemService = hkjOrderItemService;
        this.hkjOrderItemRepository = hkjOrderItemRepository;
        this.hkjOrderItemQueryService = hkjOrderItemQueryService;
    }

    /**
     * {@code POST  /hkj-order-items} : Create a new hkjOrderItem.
     *
     * @param hkjOrderItemDTO the hkjOrderItemDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjOrderItemDTO, or with status {@code 400 (Bad Request)} if the hkjOrderItem has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjOrderItemDTO> createHkjOrderItem(@Valid @RequestBody HkjOrderItemDTO hkjOrderItemDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save HkjOrderItem : {}", hkjOrderItemDTO);
        if (hkjOrderItemDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjOrderItem cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjOrderItemDTO = hkjOrderItemService.save(hkjOrderItemDTO);
        return ResponseEntity.created(new URI("/api/hkj-order-items/" + hkjOrderItemDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjOrderItemDTO.getId().toString()))
            .body(hkjOrderItemDTO);
    }

    /**
     * {@code PUT  /hkj-order-items/:id} : Updates an existing hkjOrderItem.
     *
     * @param id the id of the hkjOrderItemDTO to save.
     * @param hkjOrderItemDTO the hkjOrderItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjOrderItemDTO,
     * or with status {@code 400 (Bad Request)} if the hkjOrderItemDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjOrderItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjOrderItemDTO> updateHkjOrderItem(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HkjOrderItemDTO hkjOrderItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HkjOrderItem : {}, {}", id, hkjOrderItemDTO);
        if (hkjOrderItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjOrderItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjOrderItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjOrderItemDTO = hkjOrderItemService.update(hkjOrderItemDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjOrderItemDTO.getId().toString()))
            .body(hkjOrderItemDTO);
    }

    /**
     * {@code PATCH  /hkj-order-items/:id} : Partial updates given fields of an existing hkjOrderItem, field will ignore if it is null
     *
     * @param id the id of the hkjOrderItemDTO to save.
     * @param hkjOrderItemDTO the hkjOrderItemDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjOrderItemDTO,
     * or with status {@code 400 (Bad Request)} if the hkjOrderItemDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjOrderItemDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjOrderItemDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjOrderItemDTO> partialUpdateHkjOrderItem(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HkjOrderItemDTO hkjOrderItemDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HkjOrderItem partially : {}, {}", id, hkjOrderItemDTO);
        if (hkjOrderItemDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjOrderItemDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjOrderItemRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjOrderItemDTO> result = hkjOrderItemService.partialUpdate(hkjOrderItemDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjOrderItemDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-order-items} : get all the hkjOrderItems.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjOrderItems in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjOrderItemDTO>> getAllHkjOrderItems(
        HkjOrderItemCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HkjOrderItems by criteria: {}", criteria);

        Page<HkjOrderItemDTO> page = hkjOrderItemQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-order-items/count} : count all the hkjOrderItems.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjOrderItems(HkjOrderItemCriteria criteria) {
        LOG.debug("REST request to count HkjOrderItems by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjOrderItemQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-order-items/:id} : get the "id" hkjOrderItem.
     *
     * @param id the id of the hkjOrderItemDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjOrderItemDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjOrderItemDTO> getHkjOrderItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HkjOrderItem : {}", id);
        Optional<HkjOrderItemDTO> hkjOrderItemDTO = hkjOrderItemService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjOrderItemDTO);
    }

    /**
     * {@code DELETE  /hkj-order-items/:id} : delete the "id" hkjOrderItem.
     *
     * @param id the id of the hkjOrderItemDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjOrderItem(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HkjOrderItem : {}", id);
        hkjOrderItemService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
