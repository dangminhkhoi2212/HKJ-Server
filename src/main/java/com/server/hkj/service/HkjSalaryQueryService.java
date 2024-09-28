package com.server.hkj.service;

import com.server.hkj.domain.*; // for static metamodels
import com.server.hkj.domain.HkjSalary;
import com.server.hkj.repository.HkjSalaryRepository;
import com.server.hkj.service.criteria.HkjSalaryCriteria;
import com.server.hkj.service.dto.HkjSalaryDTO;
import com.server.hkj.service.mapper.HkjSalaryMapper;
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
 * Service for executing complex queries for {@link HkjSalary} entities in the database.
 * The main input is a {@link HkjSalaryCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjSalaryDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjSalaryQueryService extends QueryService<HkjSalary> {

    private static final Logger LOG = LoggerFactory.getLogger(HkjSalaryQueryService.class);

    private final HkjSalaryRepository hkjSalaryRepository;

    private final HkjSalaryMapper hkjSalaryMapper;

    public HkjSalaryQueryService(HkjSalaryRepository hkjSalaryRepository, HkjSalaryMapper hkjSalaryMapper) {
        this.hkjSalaryRepository = hkjSalaryRepository;
        this.hkjSalaryMapper = hkjSalaryMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjSalaryDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjSalaryDTO> findByCriteria(HkjSalaryCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjSalary> specification = createSpecification(criteria);
        return hkjSalaryRepository.findAll(specification, page).map(hkjSalaryMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjSalaryCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HkjSalary> specification = createSpecification(criteria);
        return hkjSalaryRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjSalaryCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjSalary> createSpecification(HkjSalaryCriteria criteria) {
        Specification<HkjSalary> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjSalary_.id));
            }
            if (criteria.getSalary() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getSalary(), HkjSalary_.salary));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), HkjSalary_.notes));
            }
            if (criteria.getPayDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPayDate(), HkjSalary_.payDate));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), HkjSalary_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjSalary_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjSalary_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HkjSalary_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), HkjSalary_.lastModifiedDate));
            }
            if (criteria.getUserExtraId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getUserExtraId(), root -> root.join(HkjSalary_.userExtra, JoinType.LEFT).get(UserExtra_.id))
                );
            }
        }
        return specification;
    }
}
