package com.server.hkj.service;

import com.server.hkj.domain.*; // for static metamodels
import com.server.hkj.domain.HkjTempImage;
import com.server.hkj.repository.HkjTempImageRepository;
import com.server.hkj.service.criteria.HkjTempImageCriteria;
import com.server.hkj.service.dto.HkjTempImageDTO;
import com.server.hkj.service.mapper.HkjTempImageMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;

/**
 * Service for executing complex queries for {@link HkjTempImage} entities in the database.
 * The main input is a {@link HkjTempImageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjTempImageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjTempImageQueryService extends QueryService<HkjTempImage> {

    private static final Logger LOG = LoggerFactory.getLogger(HkjTempImageQueryService.class);

    private final HkjTempImageRepository hkjTempImageRepository;

    private final HkjTempImageMapper hkjTempImageMapper;

    public HkjTempImageQueryService(HkjTempImageRepository hkjTempImageRepository, HkjTempImageMapper hkjTempImageMapper) {
        this.hkjTempImageRepository = hkjTempImageRepository;
        this.hkjTempImageMapper = hkjTempImageMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjTempImageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjTempImageDTO> findByCriteria(HkjTempImageCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjTempImage> specification = createSpecification(criteria);
        return hkjTempImageRepository.findAll(specification, page).map(hkjTempImageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjTempImageCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HkjTempImage> specification = createSpecification(criteria);
        return hkjTempImageRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjTempImageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjTempImage> createSpecification(HkjTempImageCriteria criteria) {
        Specification<HkjTempImage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjTempImage_.id));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), HkjTempImage_.url));
            }
            if (criteria.getIsUsed() != null) {
                specification = specification.and(buildSpecification(criteria.getIsUsed(), HkjTempImage_.isUsed));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), HkjTempImage_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjTempImage_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjTempImage_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HkjTempImage_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), HkjTempImage_.lastModifiedDate));
            }
        }
        return specification;
    }
}
