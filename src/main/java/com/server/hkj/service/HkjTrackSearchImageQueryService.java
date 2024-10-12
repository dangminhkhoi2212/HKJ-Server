package com.server.hkj.service;

import com.server.hkj.domain.*; // for static metamodels
import com.server.hkj.domain.HkjTrackSearchImage;
import com.server.hkj.repository.HkjTrackSearchImageRepository;
import com.server.hkj.service.criteria.HkjTrackSearchImageCriteria;
import com.server.hkj.service.dto.HkjTrackSearchImageDTO;
import com.server.hkj.service.mapper.HkjTrackSearchImageMapper;
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
 * Service for executing complex queries for {@link HkjTrackSearchImage} entities in the database.
 * The main input is a {@link HkjTrackSearchImageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjTrackSearchImageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjTrackSearchImageQueryService extends QueryService<HkjTrackSearchImage> {

    private static final Logger LOG = LoggerFactory.getLogger(HkjTrackSearchImageQueryService.class);

    private final HkjTrackSearchImageRepository hkjTrackSearchImageRepository;

    private final HkjTrackSearchImageMapper hkjTrackSearchImageMapper;

    public HkjTrackSearchImageQueryService(
        HkjTrackSearchImageRepository hkjTrackSearchImageRepository,
        HkjTrackSearchImageMapper hkjTrackSearchImageMapper
    ) {
        this.hkjTrackSearchImageRepository = hkjTrackSearchImageRepository;
        this.hkjTrackSearchImageMapper = hkjTrackSearchImageMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjTrackSearchImageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjTrackSearchImageDTO> findByCriteria(HkjTrackSearchImageCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjTrackSearchImage> specification = createSpecification(criteria);
        return hkjTrackSearchImageRepository.findAll(specification, page).map(hkjTrackSearchImageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjTrackSearchImageCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HkjTrackSearchImage> specification = createSpecification(criteria);
        return hkjTrackSearchImageRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjTrackSearchImageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjTrackSearchImage> createSpecification(HkjTrackSearchImageCriteria criteria) {
        Specification<HkjTrackSearchImage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjTrackSearchImage_.id));
            }
            if (criteria.getSearchOrder() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSearchOrder(), HkjTrackSearchImage_.searchOrder));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjTrackSearchImage_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjTrackSearchImage_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(
                    buildStringSpecification(criteria.getLastModifiedBy(), HkjTrackSearchImage_.lastModifiedBy)
                );
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getLastModifiedDate(), HkjTrackSearchImage_.lastModifiedDate)
                );
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(), root -> root.join(HkjTrackSearchImage_.user, JoinType.LEFT).get(UserExtra_.id))
                );
            }
            if (criteria.getJewelryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getJewelryId(), root ->
                        root.join(HkjTrackSearchImage_.jewelry, JoinType.LEFT).get(HkjJewelryModel_.id)
                    )
                );
            }
        }
        return specification;
    }
}
