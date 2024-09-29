package com.server.hkj.service;

// for static metamodels
import com.server.hkj.domain.HkjHire;
import com.server.hkj.domain.HkjHire_;
import com.server.hkj.domain.HkjPosition_;
import com.server.hkj.domain.User;
import com.server.hkj.domain.UserExtra;
import com.server.hkj.domain.UserExtra_;
import com.server.hkj.domain.User_;
import com.server.hkj.repository.HkjHireRepository;
import com.server.hkj.service.criteria.HkjHireCriteria;
import com.server.hkj.service.dto.HkjHireDTO;
import com.server.hkj.service.mapper.HkjHireMapper;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tech.jhipster.service.QueryService;
import tech.jhipster.service.filter.StringFilter;

/**
 * Service for executing complex queries for {@link HkjHire} entities in the database.
 * The main input is a {@link HkjHireCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjHireDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjHireQueryService extends QueryService<HkjHire> {

    private static final Logger LOG = LoggerFactory.getLogger(HkjHireQueryService.class);

    private final HkjHireRepository hkjHireRepository;

    private final HkjHireMapper hkjHireMapper;

    public HkjHireQueryService(HkjHireRepository hkjHireRepository, HkjHireMapper hkjHireMapper) {
        this.hkjHireRepository = hkjHireRepository;
        this.hkjHireMapper = hkjHireMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjHireDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjHireDTO> findByCriteria(HkjHireCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjHire> specification = createSpecification(criteria);
        return hkjHireRepository.findAll(specification, page).map(hkjHireMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjHireCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HkjHire> specification = createSpecification(criteria);
        return hkjHireRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjHireCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjHire> createSpecification(HkjHireCriteria criteria) {
        Specification<HkjHire> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjHire_.id));
            }
            if (criteria.getBeginDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBeginDate(), HkjHire_.beginDate));
            }
            if (criteria.getEndDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getEndDate(), HkjHire_.endDate));
            }
            if (criteria.getBeginSalary() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getBeginSalary(), HkjHire_.beginSalary));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), HkjHire_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjHire_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjHire_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HkjHire_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), HkjHire_.lastModifiedDate));
            }
            if (criteria.getPositionId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPositionId(), root -> root.join(HkjHire_.position, JoinType.LEFT).get(HkjPosition_.id))
                );
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getEmployeeId(), root -> root.join(HkjHire_.employee, JoinType.LEFT).get(UserExtra_.id))
                );
            }
            if (criteria.getEmployeeName() != null) {
                specification = specification.and(buildEmployeeNameSpecification(criteria.getEmployeeName()));
            }
            if (criteria.getPositionName() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getPositionName(), root ->
                        root.join(HkjHire_.position, JoinType.LEFT).get(HkjPosition_.name)
                    )
                );
            }
        }
        return specification;
    }

    /**
     * Builds a specification for filtering HkjHire entities by employee name.
     *
     * @param employeeName a StringFilter object containing the employee name to filter by
     * @return a Specification object that can be used to filter HkjHire entities by employee name
     */
    public static Specification<HkjHire> buildEmployeeNameSpecification(StringFilter employeeName) {
        return (root, query, criteriaBuilder) -> {
            if (employeeName == null || employeeName.getEquals() == null) {
                return null;
            }

            Join<HkjHire, UserExtra> employeeJoin = root.join(HkjHire_.employee, JoinType.LEFT);
            Join<UserExtra, User> userJoin = employeeJoin.join(UserExtra_.user, JoinType.LEFT);

            Expression<String> fullName = criteriaBuilder.concat(
                criteriaBuilder.concat(userJoin.get(User_.firstName), " "),
                userJoin.get(User_.lastName)
            );

            return criteriaBuilder.like(criteriaBuilder.lower(fullName), "%" + employeeName.getEquals().toLowerCase() + "%");
        };
    }
}
