package com.server.hkj.service;

import com.server.hkj.domain.*; // for static metamodels
import com.server.hkj.domain.HkjOrderItem;
import com.server.hkj.repository.HkjOrderItemRepository;
import com.server.hkj.service.criteria.HkjOrderItemCriteria;
import com.server.hkj.service.dto.HkjOrderItemDTO;
import com.server.hkj.service.mapper.HkjOrderItemMapper;
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
 * Service for executing complex queries for {@link HkjOrderItem} entities in the database.
 * The main input is a {@link HkjOrderItemCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjOrderItemDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjOrderItemQueryService extends QueryService<HkjOrderItem> {

    private static final Logger LOG = LoggerFactory.getLogger(HkjOrderItemQueryService.class);

    private final HkjOrderItemRepository hkjOrderItemRepository;

    private final HkjOrderItemMapper hkjOrderItemMapper;

    public HkjOrderItemQueryService(HkjOrderItemRepository hkjOrderItemRepository, HkjOrderItemMapper hkjOrderItemMapper) {
        this.hkjOrderItemRepository = hkjOrderItemRepository;
        this.hkjOrderItemMapper = hkjOrderItemMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjOrderItemDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjOrderItemDTO> findByCriteria(HkjOrderItemCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjOrderItem> specification = createSpecification(criteria);
        return hkjOrderItemRepository.findAll(specification, page).map(hkjOrderItemMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjOrderItemCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HkjOrderItem> specification = createSpecification(criteria);
        return hkjOrderItemRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjOrderItemCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjOrderItem> createSpecification(HkjOrderItemCriteria criteria) {
        Specification<HkjOrderItem> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjOrderItem_.id));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), HkjOrderItem_.quantity));
            }
            if (criteria.getSpecialRequests() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSpecialRequests(), HkjOrderItem_.specialRequests));
            }
            if (criteria.getPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPrice(), HkjOrderItem_.price));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), HkjOrderItem_.isDeleted));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), HkjOrderItem_.notes));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjOrderItem_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjOrderItem_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HkjOrderItem_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), HkjOrderItem_.lastModifiedDate));
            }
            if (criteria.getMaterialId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMaterialId(), root ->
                        root.join(HkjOrderItem_.material, JoinType.LEFT).get(HkjMaterial_.id)
                    )
                );
            }
            if (criteria.getOrderId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderId(), root -> root.join(HkjOrderItem_.order, JoinType.LEFT).get(HkjOrder_.id))
                );
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root ->
                        root.join(HkjOrderItem_.product, JoinType.LEFT).get(HkjJewelryModel_.id)
                    )
                );
            }
            if (criteria.getCategoryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCategoryId(), root ->
                        root.join(HkjOrderItem_.category, JoinType.LEFT).get(HkjCategory_.id)
                    )
                );
            }
        }
        return specification;
    }
}
