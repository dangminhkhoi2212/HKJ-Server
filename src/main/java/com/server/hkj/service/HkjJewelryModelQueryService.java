package com.server.hkj.service;

import com.server.hkj.domain.*; // for static metamodels
import com.server.hkj.domain.HkjJewelryModel;
import com.server.hkj.repository.HkjJewelryModelRepository;
import com.server.hkj.service.criteria.HkjJewelryModelCriteria;
import com.server.hkj.service.dto.HkjJewelryModelDTO;
import com.server.hkj.service.mapper.HkjJewelryModelMapper;
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
 * Service for executing complex queries for {@link HkjJewelryModel} entities in the database.
 * The main input is a {@link HkjJewelryModelCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjJewelryModelDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjJewelryModelQueryService extends QueryService<HkjJewelryModel> {

    private static final Logger LOG = LoggerFactory.getLogger(HkjJewelryModelQueryService.class);

    private final HkjJewelryModelRepository hkjJewelryModelRepository;

    private final HkjJewelryModelMapper hkjJewelryModelMapper;

    public HkjJewelryModelQueryService(HkjJewelryModelRepository hkjJewelryModelRepository, HkjJewelryModelMapper hkjJewelryModelMapper) {
        this.hkjJewelryModelRepository = hkjJewelryModelRepository;
        this.hkjJewelryModelMapper = hkjJewelryModelMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjJewelryModelDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjJewelryModelDTO> findByCriteria(HkjJewelryModelCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjJewelryModel> specification = createSpecification(criteria);
        return hkjJewelryModelRepository.findAll(specification, page).map(hkjJewelryModelMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjJewelryModelCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HkjJewelryModel> specification = createSpecification(criteria);
        return hkjJewelryModelRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjJewelryModelCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjJewelryModel> createSpecification(HkjJewelryModelCriteria criteria) {
        Specification<HkjJewelryModel> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjJewelryModel_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), HkjJewelryModel_.name));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), HkjJewelryModel_.description));
            }
            if (criteria.getCoverImage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCoverImage(), HkjJewelryModel_.coverImage));
            }
            if (criteria.getIsCustom() != null) {
                specification = specification.and(buildSpecification(criteria.getIsCustom(), HkjJewelryModel_.isCustom));
            }
            if (criteria.getWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeight(), HkjJewelryModel_.weight));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), HkjJewelryModel_.price));
            }
            if (criteria.getColor() != null) {
                specification = specification.and(buildStringSpecification(criteria.getColor(), HkjJewelryModel_.color));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), HkjJewelryModel_.notes));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), HkjJewelryModel_.isDeleted));
            }
            if (criteria.getActive() != null) {
                specification = specification.and(buildSpecification(criteria.getActive(), HkjJewelryModel_.active));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjJewelryModel_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjJewelryModel_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HkjJewelryModel_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getLastModifiedDate(), HkjJewelryModel_.lastModifiedDate)
                );
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProjectId(), root ->
                        root.join(HkjJewelryModel_.project, JoinType.LEFT).get(HkjProject_.id)
                    )
                );
            }
            if (criteria.getImagesId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getImagesId(), root ->
                        root.join(HkjJewelryModel_.images, JoinType.LEFT).get(HkjJewelryImage_.id)
                    )
                );
            }
        }
        return specification;
    }
}
