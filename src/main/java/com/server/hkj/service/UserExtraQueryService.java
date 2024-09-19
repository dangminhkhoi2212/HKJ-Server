package com.server.hkj.service;

import com.server.hkj.domain.*; // for static metamodels
import com.server.hkj.domain.UserExtra;
import com.server.hkj.repository.UserExtraRepository;
import com.server.hkj.service.criteria.UserExtraCriteria;
import com.server.hkj.service.dto.UserExtraDTO;
import com.server.hkj.service.mapper.UserExtraMapper;
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
 * Service for executing complex queries for {@link UserExtra} entities in the database.
 * The main input is a {@link UserExtraCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link UserExtraDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class UserExtraQueryService extends QueryService<UserExtra> {

    private static final Logger log = LoggerFactory.getLogger(UserExtraQueryService.class);

    private final UserExtraRepository userExtraRepository;

    private final UserExtraMapper userExtraMapper;

    public UserExtraQueryService(UserExtraRepository userExtraRepository, UserExtraMapper userExtraMapper) {
        this.userExtraRepository = userExtraRepository;
        this.userExtraMapper = userExtraMapper;
    }

    /**
     * Return a {@link Page} of {@link UserExtraDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<UserExtraDTO> findByCriteria(UserExtraCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<UserExtra> specification = createSpecification(criteria);
        return userExtraRepository.findAll(specification, page).map(userExtraMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(UserExtraCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<UserExtra> specification = createSpecification(criteria);
        return userExtraRepository.count(specification);
    }

    /**
     * Function to convert {@link UserExtraCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<UserExtra> createSpecification(UserExtraCriteria criteria) {
        Specification<UserExtra> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), UserExtra_.id));
            }
            if (criteria.getPhone() != null) {
                specification = specification.and(buildStringSpecification(criteria.getPhone(), UserExtra_.phone));
            }
            if (criteria.getAddress() != null) {
                specification = specification.and(buildStringSpecification(criteria.getAddress(), UserExtra_.address));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), UserExtra_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), UserExtra_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), UserExtra_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), UserExtra_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), UserExtra_.lastModifiedDate));
            }
            if (criteria.getUserId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserId(), root -> root.join(UserExtra_.user, JoinType.LEFT).get(User_.id))
                );
            }
            if (criteria.getHkjEmployeeId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getHkjEmployeeId(),
                        root -> root.join(UserExtra_.hkjEmployee, JoinType.LEFT).get(HkjEmployee_.id)
                    )
                );
            }
        }
        return specification;
    }
}
