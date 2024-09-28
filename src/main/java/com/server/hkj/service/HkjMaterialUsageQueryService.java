package com.server.hkj.service;

import com.server.hkj.domain.*; // for static metamodels
import com.server.hkj.domain.HkjMaterialUsage;
import com.server.hkj.repository.HkjMaterialUsageRepository;
import com.server.hkj.service.criteria.HkjMaterialUsageCriteria;
import com.server.hkj.service.dto.HkjMaterialUsageDTO;
import com.server.hkj.service.mapper.HkjMaterialUsageMapper;
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
 * Service for executing complex queries for {@link HkjMaterialUsage} entities in the database.
 * The main input is a {@link HkjMaterialUsageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjMaterialUsageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjMaterialUsageQueryService extends QueryService<HkjMaterialUsage> {

    private static final Logger LOG = LoggerFactory.getLogger(HkjMaterialUsageQueryService.class);

    private final HkjMaterialUsageRepository hkjMaterialUsageRepository;

    private final HkjMaterialUsageMapper hkjMaterialUsageMapper;

    public HkjMaterialUsageQueryService(
        HkjMaterialUsageRepository hkjMaterialUsageRepository,
        HkjMaterialUsageMapper hkjMaterialUsageMapper
    ) {
        this.hkjMaterialUsageRepository = hkjMaterialUsageRepository;
        this.hkjMaterialUsageMapper = hkjMaterialUsageMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjMaterialUsageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjMaterialUsageDTO> findByCriteria(HkjMaterialUsageCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjMaterialUsage> specification = createSpecification(criteria);
        return hkjMaterialUsageRepository.findAll(specification, page).map(hkjMaterialUsageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjMaterialUsageCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HkjMaterialUsage> specification = createSpecification(criteria);
        return hkjMaterialUsageRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjMaterialUsageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjMaterialUsage> createSpecification(HkjMaterialUsageCriteria criteria) {
        Specification<HkjMaterialUsage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjMaterialUsage_.id));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), HkjMaterialUsage_.quantity));
            }
            if (criteria.getLossQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLossQuantity(), HkjMaterialUsage_.lossQuantity));
            }
            if (criteria.getUsageDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getUsageDate(), HkjMaterialUsage_.usageDate));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), HkjMaterialUsage_.notes));
            }
            if (criteria.getWeight() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getWeight(), HkjMaterialUsage_.weight));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), HkjMaterialUsage_.price));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), HkjMaterialUsage_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjMaterialUsage_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjMaterialUsage_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HkjMaterialUsage_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getLastModifiedDate(), HkjMaterialUsage_.lastModifiedDate)
                );
            }
            if (criteria.getMaterialId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMaterialId(), root ->
                        root.join(HkjMaterialUsage_.material, JoinType.LEFT).get(HkjMaterial_.id)
                    )
                );
            }
            if (criteria.getHkjTaskId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getHkjTaskId(), root -> root.join(HkjMaterialUsage_.hkjTask, JoinType.LEFT).get(HkjTask_.id)
                    )
                );
            }
        }
        return specification;
    }
}
