package com.server.hkj.service;

import com.server.hkj.domain.*; // for static metamodels
import com.server.hkj.domain.HkjCategory;
import com.server.hkj.repository.HkjCategoryRepository;
import com.server.hkj.service.criteria.HkjCategoryCriteria;
import com.server.hkj.service.dto.HkjCategoryDTO;
import com.server.hkj.service.mapper.HkjCategoryMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link HkjCategory} entities in the database.
 * The main input is a {@link HkjCategoryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjCategoryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjCategoryQueryService extends QueryService<HkjCategory> {

    private static final Logger LOG = LoggerFactory.getLogger(HkjCategoryQueryService.class);

    private final HkjCategoryRepository hkjCategoryRepository;

    private final HkjCategoryMapper hkjCategoryMapper;

    public HkjCategoryQueryService(HkjCategoryRepository hkjCategoryRepository, HkjCategoryMapper hkjCategoryMapper) {
        this.hkjCategoryRepository = hkjCategoryRepository;
        this.hkjCategoryMapper = hkjCategoryMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjCategoryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjCategoryDTO> findByCriteria(HkjCategoryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjCategory> specification = createSpecification(criteria);
        return hkjCategoryRepository.findAll(specification, page).map(hkjCategoryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjCategoryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HkjCategory> specification = createSpecification(criteria);
        return hkjCategoryRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjCategoryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjCategory> createSpecification(HkjCategoryCriteria criteria) {
        Specification<HkjCategory> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjCategory_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), HkjCategory_.name));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), HkjCategory_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjCategory_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjCategory_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HkjCategory_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), HkjCategory_.lastModifiedDate));
            }
        }
        return specification;
    }
}
