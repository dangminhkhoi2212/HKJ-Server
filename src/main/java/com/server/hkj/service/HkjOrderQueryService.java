package com.server.hkj.service;

import com.server.hkj.domain.*; // for static metamodels
import com.server.hkj.domain.HkjOrder;
import com.server.hkj.repository.HkjOrderRepository;
import com.server.hkj.service.criteria.HkjOrderCriteria;
import com.server.hkj.service.dto.HkjOrderDTO;
import com.server.hkj.service.mapper.HkjOrderMapper;
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
 * Service for executing complex queries for {@link HkjOrder} entities in the database.
 * The main input is a {@link HkjOrderCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjOrderDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjOrderQueryService extends QueryService<HkjOrder> {

    private static final Logger LOG = LoggerFactory.getLogger(HkjOrderQueryService.class);

    private final HkjOrderRepository hkjOrderRepository;

    private final HkjOrderMapper hkjOrderMapper;

    public HkjOrderQueryService(HkjOrderRepository hkjOrderRepository, HkjOrderMapper hkjOrderMapper) {
        this.hkjOrderRepository = hkjOrderRepository;
        this.hkjOrderMapper = hkjOrderMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjOrderDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjOrderDTO> findByCriteria(HkjOrderCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjOrder> specification = createSpecification(criteria);
        return hkjOrderRepository.findAll(specification, page).map(hkjOrderMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjOrderCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HkjOrder> specification = createSpecification(criteria);
        return hkjOrderRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjOrderCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjOrder> createSpecification(HkjOrderCriteria criteria) {
        Specification<HkjOrder> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjOrder_.id));
            }
            if (criteria.getOrderDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getOrderDate(), HkjOrder_.orderDate));
            }
            if (criteria.getExpectedDeliveryDate() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getExpectedDeliveryDate(), HkjOrder_.expectedDeliveryDate)
                );
            }
            if (criteria.getActualDeliveryDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getActualDeliveryDate(), HkjOrder_.actualDeliveryDate));
            }
            if (criteria.getSpecialRequests() != null) {
                specification = specification.and(buildStringSpecification(criteria.getSpecialRequests(), HkjOrder_.specialRequests));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), HkjOrder_.status));
            }
            if (criteria.getCustomerRating() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCustomerRating(), HkjOrder_.customerRating));
            }
            if (criteria.getTotalPrice() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getTotalPrice(), HkjOrder_.totalPrice));
            }
            if (criteria.getDepositAmount() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getDepositAmount(), HkjOrder_.depositAmount));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), HkjOrder_.notes));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), HkjOrder_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjOrder_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjOrder_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HkjOrder_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), HkjOrder_.lastModifiedDate));
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProjectId(), root -> root.join(HkjOrder_.project, JoinType.LEFT).get(HkjProject_.id))
                );
            }
            if (criteria.getOrderImagesId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getOrderImagesId(), root ->
                        root.join(HkjOrder_.orderImages, JoinType.LEFT).get(HkjOrderImage_.id)
                    )
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root -> root.join(HkjOrder_.customer, JoinType.LEFT).get(UserExtra_.id))
                );
            }
            if (criteria.getJewelryId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getJewelryId(), root -> root.join(HkjOrder_.jewelry, JoinType.LEFT).get(HkjJewelryModel_.id)
                    )
                );
            }
        }
        return specification;
    }
}
