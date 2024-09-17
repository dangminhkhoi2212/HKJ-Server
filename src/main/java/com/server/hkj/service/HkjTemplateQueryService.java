package com.server.hkj.service;

import com.server.hkj.domain.*; // for static metamodels
import com.server.hkj.domain.HkjTemplate;
import com.server.hkj.repository.HkjTemplateRepository;
import com.server.hkj.service.criteria.HkjTemplateCriteria;
import com.server.hkj.service.dto.HkjTemplateDTO;
import com.server.hkj.service.mapper.HkjTemplateMapper;
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
 * Service for executing complex queries for {@link HkjTemplate} entities in the database.
 * The main input is a {@link HkjTemplateCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjTemplateDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjTemplateQueryService extends QueryService<HkjTemplate> {

    private static final Logger log = LoggerFactory.getLogger(HkjTemplateQueryService.class);

    private final HkjTemplateRepository hkjTemplateRepository;

    private final HkjTemplateMapper hkjTemplateMapper;

    public HkjTemplateQueryService(HkjTemplateRepository hkjTemplateRepository, HkjTemplateMapper hkjTemplateMapper) {
        this.hkjTemplateRepository = hkjTemplateRepository;
        this.hkjTemplateMapper = hkjTemplateMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjTemplateDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjTemplateDTO> findByCriteria(HkjTemplateCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjTemplate> specification = createSpecification(criteria);
        return hkjTemplateRepository.findAll(specification, page).map(hkjTemplateMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjTemplateCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HkjTemplate> specification = createSpecification(criteria);
        return hkjTemplateRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjTemplateCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjTemplate> createSpecification(HkjTemplateCriteria criteria) {
        Specification<HkjTemplate> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjTemplate_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), HkjTemplate_.name));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjTemplate_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjTemplate_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HkjTemplate_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), HkjTemplate_.lastModifiedDate));
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getCategoryId(),
                        root -> root.join(HkjTemplate_.category, JoinType.LEFT).get(HkjCategory_.id)
                    )
                );
            }
            if (criteria.getStepsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getStepsId(), root -> root.join(HkjTemplate_.steps, JoinType.LEFT).get(HkjTemplateStep_.id))
                );
            }
        }
        return specification;
    }
}
