package com.server.hkj.service;

import com.server.hkj.domain.*; // for static metamodels
import com.server.hkj.domain.HkjProject;
import com.server.hkj.repository.HkjProjectRepository;
import com.server.hkj.service.criteria.HkjProjectCriteria;
import com.server.hkj.service.dto.HkjProjectDTO;
import com.server.hkj.service.mapper.HkjProjectMapper;
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
 * Service for executing complex queries for {@link HkjProject} entities in the database.
 * The main input is a {@link HkjProjectCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjProjectDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjProjectQueryService extends QueryService<HkjProject> {

    private static final Logger log = LoggerFactory.getLogger(HkjProjectQueryService.class);

    private final HkjProjectRepository hkjProjectRepository;

    private final HkjProjectMapper hkjProjectMapper;

    public HkjProjectQueryService(HkjProjectRepository hkjProjectRepository, HkjProjectMapper hkjProjectMapper) {
        this.hkjProjectRepository = hkjProjectRepository;
        this.hkjProjectMapper = hkjProjectMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjProjectDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjProjectDTO> findByCriteria(HkjProjectCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjProject> specification = createSpecification(criteria);
        return hkjProjectRepository.findAll(specification, page).map(hkjProjectMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjProjectCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HkjProject> specification = createSpecification(criteria);
        return hkjProjectRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjProjectCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjProject> createSpecification(HkjProjectCriteria criteria) {
        Specification<HkjProject> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjProject_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), HkjProject_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), HkjProject_.description));
            }
            if (criteria.getStartDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getStartDate(), HkjProject_.startDate));
            }
            if (criteria.getExpectDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpectDate(), HkjProject_.expectDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), HkjProject_.endDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), HkjProject_.status));
            }
            if (criteria.getPriority() != null) {
                specification = specification.and(buildSpecification(criteria.getPriority(), HkjProject_.priority));
            }
            if (criteria.getBudget() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBudget(), HkjProject_.budget));
            }
            if (criteria.getActualCost() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActualCost(), HkjProject_.actualCost));
            }
            if (criteria.getQualityCheck() != null) {
                specification = specification.and(buildSpecification(criteria.getQualityCheck(), HkjProject_.qualityCheck));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), HkjProject_.notes));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjProject_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjProject_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HkjProject_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), HkjProject_.lastModifiedDate));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getCategoryId(),
                        root -> root.join(HkjProject_.category, JoinType.LEFT).get(HkjCategory_.id)
                    )
                );
            }
            if (criteria.getTasksId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTasksId(), root -> root.join(HkjProject_.tasks, JoinType.LEFT).get(HkjTask_.id))
                );
            }
            if (criteria.getManagerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getManagerId(), root -> root.join(HkjProject_.manager, JoinType.LEFT).get(HkjEmployee_.id))
                );
            }
            if (criteria.getHkjOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getHkjOrderId(), root -> root.join(HkjProject_.hkjOrder, JoinType.LEFT).get(HkjOrder_.id))
                );
            }
        }
        return specification;
    }
}
