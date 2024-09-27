package com.server.hkj.service;

import com.server.hkj.domain.*; // for static metamodels
import com.server.hkj.domain.HkjTaskImage;
import com.server.hkj.repository.HkjTaskImageRepository;
import com.server.hkj.service.criteria.HkjTaskImageCriteria;
import com.server.hkj.service.dto.HkjTaskImageDTO;
import com.server.hkj.service.mapper.HkjTaskImageMapper;
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
 * Service for executing complex queries for {@link HkjTaskImage} entities in the database.
 * The main input is a {@link HkjTaskImageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjTaskImageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjTaskImageQueryService extends QueryService<HkjTaskImage> {

    private static final Logger log = LoggerFactory.getLogger(HkjTaskImageQueryService.class);

    private final HkjTaskImageRepository hkjTaskImageRepository;

    private final HkjTaskImageMapper hkjTaskImageMapper;

    public HkjTaskImageQueryService(HkjTaskImageRepository hkjTaskImageRepository, HkjTaskImageMapper hkjTaskImageMapper) {
        this.hkjTaskImageRepository = hkjTaskImageRepository;
        this.hkjTaskImageMapper = hkjTaskImageMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjTaskImageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjTaskImageDTO> findByCriteria(HkjTaskImageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjTaskImage> specification = createSpecification(criteria);
        return hkjTaskImageRepository.findAll(specification, page).map(hkjTaskImageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjTaskImageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HkjTaskImage> specification = createSpecification(criteria);
        return hkjTaskImageRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjTaskImageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjTaskImage> createSpecification(HkjTaskImageCriteria criteria) {
        Specification<HkjTaskImage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjTaskImage_.id));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), HkjTaskImage_.url));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), HkjTaskImage_.description));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), HkjTaskImage_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjTaskImage_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjTaskImage_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HkjTaskImage_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), HkjTaskImage_.lastModifiedDate));
            }
            if (criteria.getTaskId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getTaskId(), root -> root.join(HkjTaskImage_.task, JoinType.LEFT).get(HkjTask_.id))
                );
            }
        }
        return specification;
    }
}
