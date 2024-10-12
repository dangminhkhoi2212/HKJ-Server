package com.server.hkj.service;

import com.server.hkj.domain.*; // for static metamodels
import com.server.hkj.domain.HkjTask;
import com.server.hkj.repository.HkjTaskRepository;
import com.server.hkj.service.criteria.HkjTaskCriteria;
import com.server.hkj.service.dto.HkjTaskDTO;
import com.server.hkj.service.mapper.HkjTaskMapper;
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
 * Service for executing complex queries for {@link HkjTask} entities in the database.
 * The main input is a {@link HkjTaskCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjTaskDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjTaskQueryService extends QueryService<HkjTask> {

    private static final Logger LOG = LoggerFactory.getLogger(HkjTaskQueryService.class);

    private final HkjTaskRepository hkjTaskRepository;

    private final HkjTaskMapper hkjTaskMapper;

    public HkjTaskQueryService(HkjTaskRepository hkjTaskRepository, HkjTaskMapper hkjTaskMapper) {
        this.hkjTaskRepository = hkjTaskRepository;
        this.hkjTaskMapper = hkjTaskMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjTaskDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjTaskDTO> findByCriteria(HkjTaskCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjTask> specification = createSpecification(criteria);
        return hkjTaskRepository.findAll(specification, page).map(hkjTaskMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjTaskCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HkjTask> specification = createSpecification(criteria);
        return hkjTaskRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjTaskCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjTask> createSpecification(HkjTaskCriteria criteria) {
        Specification<HkjTask> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjTask_.id));
            }
            if (criteria.getName() != null) {
                specification = specification.and(buildStringSpecification(criteria.getName(), HkjTask_.name));
            }
            if (criteria.getCoverImage() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCoverImage(), HkjTask_.coverImage));
            }
            if (criteria.getDescription() != null) {
                specification = specification.and(buildStringSpecification(criteria.getDescription(), HkjTask_.description));
            }
            if (criteria.getAssignedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getAssignedDate(), HkjTask_.assignedDate));
            }
            if (criteria.getExpectDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getExpectDate(), HkjTask_.expectDate));
            }
            if (criteria.getCompletedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCompletedDate(), HkjTask_.completedDate));
            }
            if (criteria.getStatus() != null) {
                specification = specification.and(buildSpecification(criteria.getStatus(), HkjTask_.status));
            }
            if (criteria.getPriority() != null) {
                specification = specification.and(buildSpecification(criteria.getPriority(), HkjTask_.priority));
            }
            if (criteria.getPoint() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getPoint(), HkjTask_.point));
            }
            if (criteria.getNotes() != null) {
                specification = specification.and(buildStringSpecification(criteria.getNotes(), HkjTask_.notes));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), HkjTask_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjTask_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjTask_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HkjTask_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getLastModifiedDate(), HkjTask_.lastModifiedDate));
            }
            if (criteria.getImagesId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getImagesId(), root -> root.join(HkjTask_.images, JoinType.LEFT).get(HkjTaskImage_.id))
                );
            }
            if (criteria.getMaterialsId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMaterialsId(), root ->
                        root.join(HkjTask_.materials, JoinType.LEFT).get(HkjMaterialUsage_.id)
                    )
                );
            }
            if (criteria.getEmployeeId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getEmployeeId(), root -> root.join(HkjTask_.employee, JoinType.LEFT).get(UserExtra_.id))
                );
            }
            if (criteria.getProjectId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getProjectId(), root -> root.join(HkjTask_.project, JoinType.LEFT).get(HkjProject_.id))
                );
            }
        }
        return specification;
    }
}
