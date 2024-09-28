package com.server.hkj.web.rest;

import com.server.hkj.repository.HkjOrderRepository;
import com.server.hkj.service.HkjOrderQueryService;
import com.server.hkj.service.HkjOrderService;
import com.server.hkj.service.criteria.HkjOrderCriteria;
import com.server.hkj.service.dto.HkjOrderDTO;
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
 * REST controller for managing {@link com.server.hkj.domain.HkjOrder}.
 */
@RestController
@RequestMapping("/api/hkj-orders")
public class HkjOrderResource {

    private static final Logger LOG = LoggerFactory.getLogger(HkjOrderResource.class);

    private static final String ENTITY_NAME = "hkjOrder";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final HkjOrderService hkjOrderService;

    private final HkjOrderRepository hkjOrderRepository;

    private final HkjOrderQueryService hkjOrderQueryService;

    public HkjOrderResource(
        HkjOrderService hkjOrderService,
        HkjOrderRepository hkjOrderRepository,
        HkjOrderQueryService hkjOrderQueryService
    ) {
        this.hkjOrderService = hkjOrderService;
        this.hkjOrderRepository = hkjOrderRepository;
        this.hkjOrderQueryService = hkjOrderQueryService;
    }

    /**
     * {@code POST  /hkj-orders} : Create a new hkjOrder.
     *
     * @param hkjOrderDTO the hkjOrderDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new hkjOrderDTO, or with status {@code 400 (Bad Request)} if the hkjOrder has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<HkjOrderDTO> createHkjOrder(@Valid @RequestBody HkjOrderDTO hkjOrderDTO) throws URISyntaxException {
        LOG.debug("REST request to save HkjOrder : {}", hkjOrderDTO);
        if (hkjOrderDTO.getId() != null) {
            throw new BadRequestAlertException("A new hkjOrder cannot already have an ID", ENTITY_NAME, "idexists");
        }
        hkjOrderDTO = hkjOrderService.save(hkjOrderDTO);
        return ResponseEntity.created(new URI("/api/hkj-orders/" + hkjOrderDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, hkjOrderDTO.getId().toString()))
            .body(hkjOrderDTO);
    }

    /**
     * {@code PUT  /hkj-orders/:id} : Updates an existing hkjOrder.
     *
     * @param id the id of the hkjOrderDTO to save.
     * @param hkjOrderDTO the hkjOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjOrderDTO,
     * or with status {@code 400 (Bad Request)} if the hkjOrderDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the hkjOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<HkjOrderDTO> updateHkjOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody HkjOrderDTO hkjOrderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update HkjOrder : {}, {}", id, hkjOrderDTO);
        if (hkjOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        hkjOrderDTO = hkjOrderService.update(hkjOrderDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjOrderDTO.getId().toString()))
            .body(hkjOrderDTO);
    }

    /**
     * {@code PATCH  /hkj-orders/:id} : Partial updates given fields of an existing hkjOrder, field will ignore if it is null
     *
     * @param id the id of the hkjOrderDTO to save.
     * @param hkjOrderDTO the hkjOrderDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated hkjOrderDTO,
     * or with status {@code 400 (Bad Request)} if the hkjOrderDTO is not valid,
     * or with status {@code 404 (Not Found)} if the hkjOrderDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the hkjOrderDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<HkjOrderDTO> partialUpdateHkjOrder(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody HkjOrderDTO hkjOrderDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update HkjOrder partially : {}, {}", id, hkjOrderDTO);
        if (hkjOrderDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, hkjOrderDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!hkjOrderRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<HkjOrderDTO> result = hkjOrderService.partialUpdate(hkjOrderDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, hkjOrderDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /hkj-orders} : get all the hkjOrders.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of hkjOrders in body.
     */
    @GetMapping("")
    public ResponseEntity<List<HkjOrderDTO>> getAllHkjOrders(
        HkjOrderCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get HkjOrders by criteria: {}", criteria);

        Page<HkjOrderDTO> page = hkjOrderQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /hkj-orders/count} : count all the hkjOrders.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countHkjOrders(HkjOrderCriteria criteria) {
        LOG.debug("REST request to count HkjOrders by criteria: {}", criteria);
        return ResponseEntity.ok().body(hkjOrderQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /hkj-orders/:id} : get the "id" hkjOrder.
     *
     * @param id the id of the hkjOrderDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the hkjOrderDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<HkjOrderDTO> getHkjOrder(@PathVariable("id") Long id) {
        LOG.debug("REST request to get HkjOrder : {}", id);
        Optional<HkjOrderDTO> hkjOrderDTO = hkjOrderService.findOne(id);
        return ResponseUtil.wrapOrNotFound(hkjOrderDTO);
    }

    /**
     * {@code DELETE  /hkj-orders/:id} : delete the "id" hkjOrder.
     *
     * @param id the id of the hkjOrderDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteHkjOrder(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete HkjOrder : {}", id);
        hkjOrderService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
