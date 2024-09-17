package com.server.hkj.service;

import com.server.hkj.domain.*; // for static metamodels
import com.server.hkj.domain.HkjJewelryImage;
import com.server.hkj.repository.HkjJewelryImageRepository;
import com.server.hkj.service.criteria.HkjJewelryImageCriteria;
import com.server.hkj.service.dto.HkjJewelryImageDTO;
import com.server.hkj.service.mapper.HkjJewelryImageMapper;
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
 * Service for executing complex queries for {@link HkjJewelryImage} entities in the database.
 * The main input is a {@link HkjJewelryImageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjJewelryImageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjJewelryImageQueryService extends QueryService<HkjJewelryImage> {

    private static final Logger log = LoggerFactory.getLogger(HkjJewelryImageQueryService.class);

    private final HkjJewelryImageRepository hkjJewelryImageRepository;

    private final HkjJewelryImageMapper hkjJewelryImageMapper;

    public HkjJewelryImageQueryService(HkjJewelryImageRepository hkjJewelryImageRepository, HkjJewelryImageMapper hkjJewelryImageMapper) {
        this.hkjJewelryImageRepository = hkjJewelryImageRepository;
        this.hkjJewelryImageMapper = hkjJewelryImageMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjJewelryImageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjJewelryImageDTO> findByCriteria(HkjJewelryImageCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjJewelryImage> specification = createSpecification(criteria);
        return hkjJewelryImageRepository.findAll(specification, page).map(hkjJewelryImageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjJewelryImageCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HkjJewelryImage> specification = createSpecification(criteria);
        return hkjJewelryImageRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjJewelryImageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjJewelryImage> createSpecification(HkjJewelryImageCriteria criteria) {
        Specification<HkjJewelryImage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjJewelryImage_.id));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), HkjJewelryImage_.url));
            }
            if (criteria.getIsSearchImage() != null) {
                specification = specification.and(buildSpecification(criteria.getIsSearchImage(), HkjJewelryImage_.isSearchImage));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), HkjJewelryImage_.description));
            }
            if (criteria.getTags() != null) {
                specification = specification.and(buildStringSpecification(criteria.getTags(), HkjJewelryImage_.tags));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjJewelryImage_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjJewelryImage_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HkjJewelryImage_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getLastModifiedDate(), HkjJewelryImage_.lastModifiedDate)
                );
            }
            if (criteria.getUploadedById() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getUploadedById(),
                        root -> root.join(HkjJewelryImage_.uploadedBy, JoinType.LEFT).get(HkjEmployee_.id)
                    )
                );
            }
            if (criteria.getJewelryModelId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getJewelryModelId(),
                        root -> root.join(HkjJewelryImage_.jewelryModel, JoinType.LEFT).get(HkjJewelryModel_.id)
                    )
                );
            }
        }
        return specification;
    }
}
