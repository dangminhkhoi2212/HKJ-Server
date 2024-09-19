package com.server.hkj.service;

import com.server.hkj.domain.*; // for static metamodels
import com.server.hkj.domain.HkjEmployee;
import com.server.hkj.repository.HkjEmployeeRepository;
import com.server.hkj.service.criteria.HkjEmployeeCriteria;
import com.server.hkj.service.dto.HkjEmployeeDTO;
import com.server.hkj.service.mapper.HkjEmployeeMapper;
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
 * Service for executing complex queries for {@link HkjEmployee} entities in the database.
 * The main input is a {@link HkjEmployeeCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjEmployeeDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjEmployeeQueryService extends QueryService<HkjEmployee> {

    private static final Logger log = LoggerFactory.getLogger(HkjEmployeeQueryService.class);

    private final HkjEmployeeRepository hkjEmployeeRepository;

    private final HkjEmployeeMapper hkjEmployeeMapper;

    public HkjEmployeeQueryService(HkjEmployeeRepository hkjEmployeeRepository, HkjEmployeeMapper hkjEmployeeMapper) {
        this.hkjEmployeeRepository = hkjEmployeeRepository;
        this.hkjEmployeeMapper = hkjEmployeeMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjEmployeeDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjEmployeeDTO> findByCriteria(HkjEmployeeCriteria criteria, Pageable page) {
        log.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjEmployee> specification = createSpecification(criteria);
        return hkjEmployeeRepository.findAll(specification, page).map(hkjEmployeeMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjEmployeeCriteria criteria) {
        log.debug("count by criteria : {}", criteria);
        final Specification<HkjEmployee> specification = createSpecification(criteria);
        return hkjEmployeeRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjEmployeeCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjEmployee> createSpecification(HkjEmployeeCriteria criteria) {
        Specification<HkjEmployee> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjEmployee_.id));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), HkjEmployee_.notes));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), HkjEmployee_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjEmployee_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjEmployee_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HkjEmployee_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), HkjEmployee_.lastModifiedDate));
            }
            if (criteria.getUserExtraId() != null) {
                specification = specification.and(
                    buildSpecification(
                        criteria.getUserExtraId(),
                        root -> root.join(HkjEmployee_.userExtra, JoinType.LEFT).get(UserExtra_.id)
                    )
                );
            }
            if (criteria.getSalarysId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getSalarysId(), root -> root.join(HkjEmployee_.salarys, JoinType.LEFT).get(HkjSalary_.id))
                );
            }
            if (criteria.getHkjHireId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getHkjHireId(), root -> root.join(HkjEmployee_.hkjHire, JoinType.LEFT).get(HkjHire_.id))
                );
            }
        }
        return specification;
    }
}
