package com.server.hkj.service;

import com.server.hkj.domain.*; // for static metamodels
import com.server.hkj.domain.HkjPosition;
import com.server.hkj.repository.HkjPositionRepository;
import com.server.hkj.service.criteria.HkjPositionCriteria;
import com.server.hkj.service.dto.HkjPositionDTO;
import com.server.hkj.service.mapper.HkjPositionMapper;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link HkjPosition} entities in the database.
 * The main input is a {@link HkjPositionCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjPositionDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjPositionQueryService extends QueryService<HkjPosition> {

    private static final Logger log = LoggerFactory.getLogger(HkjPositionQueryService.class);

    private final HkjPositionRepository hkjPositionRepository;

    private final HkjPositionMapper hkjPositionMapper;

    public HkjPositionQueryService(HkjPositionRepository hkjPositionRepository, HkjPositionMapper hkjPositionMapper) {
        this.hkjPositionRepository = hkjPositionRepository;
        this.hkjPositionMapper = hkjPositionMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjPositionDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjPositionDTO> findByCriteria(HkjPositionCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjPosition> specification = createSpecification(criteria);
        return hkjPositionRepository.findAll(specification, page).map(hkjPositionMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjPositionCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HkjPosition> specification = createSpecification(criteria);
        return hkjPositionRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjPositionCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjPosition> createSpecification(HkjPositionCriteria criteria) {
        Specification<HkjPosition> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjPosition_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), HkjPosition_.name));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), HkjPosition_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjPosition_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjPosition_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HkjPosition_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), HkjPosition_.lastModifiedDate));
            }
            if (criteria.getHireId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getHireId(), root -> root.join(HkjPosition_.hires, JoinType.LEFT).get(HkjHire_.id))
                );
            }
        }
        return specification;
    }
}
