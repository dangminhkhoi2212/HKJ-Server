package com.server.hkj.service;

import com.server.hkj.domain.*; // for static metamodels
import com.server.hkj.domain.HkjMaterial;
import com.server.hkj.repository.HkjMaterialRepository;
import com.server.hkj.service.criteria.HkjMaterialCriteria;
import com.server.hkj.service.dto.HkjMaterialDTO;
import com.server.hkj.service.mapper.HkjMaterialMapper;
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
 * Service for executing complex queries for {@link HkjMaterial} entities in the database.
 * The main input is a {@link HkjMaterialCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjMaterialDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjMaterialQueryService extends QueryService<HkjMaterial> {

    private static final Logger LOG = LoggerFactory.getLogger(HkjMaterialQueryService.class);

    private final HkjMaterialRepository hkjMaterialRepository;

    private final HkjMaterialMapper hkjMaterialMapper;

    public HkjMaterialQueryService(HkjMaterialRepository hkjMaterialRepository, HkjMaterialMapper hkjMaterialMapper) {
        this.hkjMaterialRepository = hkjMaterialRepository;
        this.hkjMaterialMapper = hkjMaterialMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjMaterialDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjMaterialDTO> findByCriteria(HkjMaterialCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjMaterial> specification = createSpecification(criteria);
        return hkjMaterialRepository.findAll(specification, page).map(hkjMaterialMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjMaterialCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HkjMaterial> specification = createSpecification(criteria);
        return hkjMaterialRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjMaterialCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjMaterial> createSpecification(HkjMaterialCriteria criteria) {
        Specification<HkjMaterial> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjMaterial_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), HkjMaterial_.name));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), HkjMaterial_.quantity));
            }
            if (criteria.getUnit() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUnit(), HkjMaterial_.unit));
            }
            if (criteria.getUnitPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUnitPrice(), HkjMaterial_.unitPrice));
            }
            if (criteria.getSupplier() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSupplier(), HkjMaterial_.supplier));
            }
            if (criteria.getCoverImage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCoverImage(), HkjMaterial_.coverImage));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), HkjMaterial_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjMaterial_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjMaterial_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HkjMaterial_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), HkjMaterial_.lastModifiedDate));
            }
            if (criteria.getImagesId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getImagesId(), root ->
                        root.join(HkjMaterial_.images, JoinType.LEFT).get(HkjMaterialImage_.id)
                    )
                );
            }
            if (criteria.getHkjMaterialUsageId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getHkjMaterialUsageId(), root ->
                        root.join(HkjMaterial_.hkjMaterialUsage, JoinType.LEFT).get(HkjMaterialUsage_.id)
                    )
                );
            }
        }
        return specification;
    }
}
