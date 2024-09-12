package com.server.hkj.web.rest;

import com.server.hkj.repository.UserExtraRepository;
import com.server.hkj.service.UserExtraQueryService;
import com.server.hkj.service.UserExtraService;
import com.server.hkj.service.criteria.UserExtraCriteria;
import com.server.hkj.service.dto.UserExtraDTO;
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
 * REST controller for managing {@link com.server.hkj.domain.UserExtra}.
 */
@RestController
@RequestMapping("/api/user-extras")
public class UserExtraResource {

    private static final Logger log = LoggerFactory.getLogger(UserExtraResource.class);

    private static final String ENTITY_NAME = "userExtra";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserExtraService userExtraService;

    private final UserExtraRepository userExtraRepository;

    private final UserExtraQueryService userExtraQueryService;

    public UserExtraResource(
        UserExtraService userExtraService,
        UserExtraRepository userExtraRepository,
        UserExtraQueryService userExtraQueryService
    ) {
        this.userExtraService = userExtraService;
        this.userExtraRepository = userExtraRepository;
        this.userExtraQueryService = userExtraQueryService;
    }

    /**
     * {@code POST  /user-extras} : Create a new userExtra.
     *
     * @param userExtraDTO the userExtraDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userExtraDTO, or with status {@code 400 (Bad Request)} if the userExtra has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserExtraDTO> createUserExtra(@Valid @RequestBody UserExtraDTO userExtraDTO) throws URISyntaxException {
        log.debug("REST request to save UserExtra : {}", userExtraDTO);
        if (userExtraDTO.getId() != null) {
            throw new BadRequestAlertException("A new userExtra cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userExtraDTO = userExtraService.save(userExtraDTO);
        return ResponseEntity.created(new URI("/api/user-extras/" + userExtraDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, userExtraDTO.getId().toString()))
            .body(userExtraDTO);
    }

    /**
     * {@code PUT  /user-extras/:id} : Updates an existing userExtra.
     *
     * @param id the id of the userExtraDTO to save.
     * @param userExtraDTO the userExtraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userExtraDTO,
     * or with status {@code 400 (Bad Request)} if the userExtraDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userExtraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserExtraDTO> updateUserExtra(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody UserExtraDTO userExtraDTO
    ) throws URISyntaxException {
        log.debug("REST request to update UserExtra : {}, {}", id, userExtraDTO);
        if (userExtraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userExtraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userExtraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userExtraDTO = userExtraService.update(userExtraDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userExtraDTO.getId().toString()))
            .body(userExtraDTO);
    }

    /**
     * {@code PATCH  /user-extras/:id} : Partial updates given fields of an existing userExtra, field will ignore if it is null
     *
     * @param id the id of the userExtraDTO to save.
     * @param userExtraDTO the userExtraDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userExtraDTO,
     * or with status {@code 400 (Bad Request)} if the userExtraDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userExtraDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userExtraDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserExtraDTO> partialUpdateUserExtra(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody UserExtraDTO userExtraDTO
    ) throws URISyntaxException {
        log.debug("REST request to partial update UserExtra partially : {}, {}", id, userExtraDTO);
        if (userExtraDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userExtraDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userExtraRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserExtraDTO> result = userExtraService.partialUpdate(userExtraDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, userExtraDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-extras} : get all the userExtras.
     *
     * @param pageable the pagination information.
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userExtras in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserExtraDTO>> getAllUserExtras(
        UserExtraCriteria criteria,
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get UserExtras by criteria: {}", criteria);

        Page<UserExtraDTO> page = userExtraQueryService.findByCriteria(criteria, pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-extras/count} : count all the userExtras.
     *
     * @param criteria the criteria which the requested entities should match.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the count in body.
     */
    @GetMapping("/count")
    public ResponseEntity<Long> countUserExtras(UserExtraCriteria criteria) {
        log.debug("REST request to count UserExtras by criteria: {}", criteria);
        return ResponseEntity.ok().body(userExtraQueryService.countByCriteria(criteria));
    }

    /**
     * {@code GET  /user-extras/:id} : get the "id" userExtra.
     *
     * @param id the id of the userExtraDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userExtraDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserExtraDTO> getUserExtra(@PathVariable("id") Long id) {
        log.debug("REST request to get UserExtra : {}", id);
        Optional<UserExtraDTO> userExtraDTO = userExtraService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userExtraDTO);
    }

    /**
     * {@code DELETE  /user-extras/:id} : delete the "id" userExtra.
     *
     * @param id the id of the userExtraDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserExtra(@PathVariable("id") Long id) {
        log.debug("REST request to delete UserExtra : {}", id);
        userExtraService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
