package com.server.hkj.service;

import com.server.hkj.domain.*; // for static metamodels
import com.server.hkj.domain.HkjOrderImage;
import com.server.hkj.repository.HkjOrderImageRepository;
import com.server.hkj.service.criteria.HkjOrderImageCriteria;
import com.server.hkj.service.dto.HkjOrderImageDTO;
import com.server.hkj.service.mapper.HkjOrderImageMapper;
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
 * Service for executing complex queries for {@link HkjOrderImage} entities in the database.
 * The main input is a {@link HkjOrderImageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjOrderImageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjOrderImageQueryService extends QueryService<HkjOrderImage> {

    private static final Logger LOG = LoggerFactory.getLogger(HkjOrderImageQueryService.class);

    private final HkjOrderImageRepository hkjOrderImageRepository;

    private final HkjOrderImageMapper hkjOrderImageMapper;

    public HkjOrderImageQueryService(HkjOrderImageRepository hkjOrderImageRepository, HkjOrderImageMapper hkjOrderImageMapper) {
        this.hkjOrderImageRepository = hkjOrderImageRepository;
        this.hkjOrderImageMapper = hkjOrderImageMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjOrderImageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjOrderImageDTO> findByCriteria(HkjOrderImageCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjOrderImage> specification = createSpecification(criteria);
        return hkjOrderImageRepository.findAll(specification, page).map(hkjOrderImageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjOrderImageCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HkjOrderImage> specification = createSpecification(criteria);
        return hkjOrderImageRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjOrderImageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjOrderImage> createSpecification(HkjOrderImageCriteria criteria) {
        Specification<HkjOrderImage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjOrderImage_.id));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), HkjOrderImage_.url));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), HkjOrderImage_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjOrderImage_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjOrderImage_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HkjOrderImage_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), HkjOrderImage_.lastModifiedDate));
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root -> root.join(HkjOrderImage_.order, JoinType.LEFT).get(HkjOrder_.id))
                );
            }
        }
        return specification;
    }
}
