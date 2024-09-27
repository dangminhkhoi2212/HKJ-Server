package com.server.hkj.service;

import com.server.hkj.domain.*; // for static metamodels
import com.server.hkj.domain.HkjTemplateStep;
import com.server.hkj.repository.HkjTemplateStepRepository;
import com.server.hkj.service.criteria.HkjTemplateStepCriteria;
import com.server.hkj.service.dto.HkjTemplateStepDTO;
import com.server.hkj.service.mapper.HkjTemplateStepMapper;
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
 * Service for executing complex queries for {@link HkjTemplateStep} entities in the database.
 * The main input is a {@link HkjTemplateStepCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjTemplateStepDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjTemplateStepQueryService extends QueryService<HkjTemplateStep> {

    private static final Logger log = LoggerFactory.getLogger(HkjTemplateStepQueryService.class);

    private final HkjTemplateStepRepository hkjTemplateStepRepository;

    private final HkjTemplateStepMapper hkjTemplateStepMapper;

    public HkjTemplateStepQueryService(HkjTemplateStepRepository hkjTemplateStepRepository, HkjTemplateStepMapper hkjTemplateStepMapper) {
        this.hkjTemplateStepRepository = hkjTemplateStepRepository;
        this.hkjTemplateStepMapper = hkjTemplateStepMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjTemplateStepDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjTemplateStepDTO> findByCriteria(HkjTemplateStepCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjTemplateStep> specification = createSpecification(criteria);
        return hkjTemplateStepRepository.findAll(specification, page).map(hkjTemplateStepMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjTemplateStepCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HkjTemplateStep> specification = createSpecification(criteria);
        return hkjTemplateStepRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjTemplateStepCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjTemplateStep> createSpecification(HkjTemplateStepCriteria criteria) {
        Specification<HkjTemplateStep> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjTemplateStep_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), HkjTemplateStep_.name));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), HkjTemplateStep_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjTemplateStep_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjTemplateStep_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HkjTemplateStep_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getLastModifiedDate(), HkjTemplateStep_.lastModifiedDate)
                );
            }
            if (criteria.getTemplateId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getTemplateId(),
                        root -> root.join(HkjTemplateStep_.template, JoinType.LEFT).get(HkjTemplate_.id)
                    )
                );
            }
        }
        return specification;
    }
}
