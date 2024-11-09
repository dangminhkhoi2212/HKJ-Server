package com.server.hkj.service;

import com.server.hkj.domain.*; // for static metamodels
import com.server.hkj.domain.HkjCart;
import com.server.hkj.repository.HkjCartRepository;
import com.server.hkj.service.criteria.HkjCartCriteria;
import com.server.hkj.service.dto.HkjCartDTO;
import com.server.hkj.service.mapper.HkjCartMapper;
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
 * Service for executing complex queries for {@link HkjCart} entities in the database.
 * The main input is a {@link HkjCartCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjCartDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjCartQueryService extends QueryService<HkjCart> {

    private static final Logger LOG = LoggerFactory.getLogger(HkjCartQueryService.class);

    private final HkjCartRepository hkjCartRepository;

    private final HkjCartMapper hkjCartMapper;

    public HkjCartQueryService(HkjCartRepository hkjCartRepository, HkjCartMapper hkjCartMapper) {
        this.hkjCartRepository = hkjCartRepository;
        this.hkjCartMapper = hkjCartMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjCartDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjCartDTO> findByCriteria(HkjCartCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjCart> specification = createSpecification(criteria);
        return hkjCartRepository.findAll(specification, page).map(hkjCartMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjCartCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HkjCart> specification = createSpecification(criteria);
        return hkjCartRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjCartCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjCart> createSpecification(HkjCartCriteria criteria) {
        Specification<HkjCart> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjCart_.id));
            }
            if (criteria.getQuantity() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getQuantity(), HkjCart_.quantity));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), HkjCart_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjCart_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjCart_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HkjCart_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), HkjCart_.lastModifiedDate));
            }
            if (criteria.getProductId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProductId(), root -> root.join(HkjCart_.products, JoinType.LEFT).get(HkjJewelryModel_.id)
                    )
                );
            }
            if (criteria.getCustomerId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getCustomerId(), root -> root.join(HkjCart_.customer, JoinType.LEFT).get(UserExtra_.id))
                );
            }
        }
        return specification;
    }
}
