package com.server.hkj.service;

// for static metamodels
import com.server.hkj.domain.HkjMaterialImage;
import com.server.hkj.domain.HkjMaterialImage_;
import com.server.hkj.domain.HkjMaterial_;
import com.server.hkj.repository.HkjMaterialImageRepository;
import com.server.hkj.service.criteria.HkjMaterialImageCriteria;
import com.server.hkj.service.dto.HkjMaterialImageDTO;
import com.server.hkj.service.mapper.HkjMaterialImageMapper;
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
 * Service for executing complex queries for {@link HkjMaterialImage} entities in the database.
 * The main input is a {@link HkjMaterialImageCriteria} which gets converted to {@link Specification},
 * in a way that all the filters must apply.
 * It returns a {@link Page} of {@link HkjMaterialImageDTO} which fulfills the criteria.
 */
@Service
@Transactional(readOnly = true)
public class HkjMaterialImageQueryService extends QueryService<HkjMaterialImage> {

    private static final Logger LOG = LoggerFactory.getLogger(HkjMaterialImageQueryService.class);

    private final HkjMaterialImageRepository hkjMaterialImageRepository;

    private final HkjMaterialImageMapper hkjMaterialImageMapper;

    public HkjMaterialImageQueryService(
        HkjMaterialImageRepository hkjMaterialImageRepository,
        HkjMaterialImageMapper hkjMaterialImageMapper
    ) {
        this.hkjMaterialImageRepository = hkjMaterialImageRepository;
        this.hkjMaterialImageMapper = hkjMaterialImageMapper;
    }

    /**
     * Return a {@link Page} of {@link HkjMaterialImageDTO} which matches the criteria from the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @param page The page, which should be returned.
     * @return the matching entities.
     */
    @Transactional(readOnly = true)
    public Page<HkjMaterialImageDTO> findByCriteria(HkjMaterialImageCriteria criteria, Pageable page) {
        LOG.debug("find by criteria : {}, page: {}", criteria, page);
        final Specification<HkjMaterialImage> specification = createSpecification(criteria);
        return hkjMaterialImageRepository.findAll(specification, page).map(hkjMaterialImageMapper::toDto);
    }

    /**
     * Return the number of matching entities in the database.
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the number of matching entities.
     */
    @Transactional(readOnly = true)
    public long countByCriteria(HkjMaterialImageCriteria criteria) {
        LOG.debug("count by criteria : {}", criteria);
        final Specification<HkjMaterialImage> specification = createSpecification(criteria);
        return hkjMaterialImageRepository.count(specification);
    }

    /**
     * Function to convert {@link HkjMaterialImageCriteria} to a {@link Specification}
     * @param criteria The object which holds all the filters, which the entities should match.
     * @return the matching {@link Specification} of the entity.
     */
    protected Specification<HkjMaterialImage> createSpecification(HkjMaterialImageCriteria criteria) {
        Specification<HkjMaterialImage> specification = Specification.where(null);
        if (criteria != null) {
            // This has to be called first, because the distinct method returns null
            if (criteria.getDistinct() != null) {
                specification = specification.and(distinct(criteria.getDistinct()));
            }
            if (criteria.getId() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getId(), HkjMaterialImage_.id));
            }
            if (criteria.getUrl() != null) {
                specification = specification.and(buildStringSpecification(criteria.getUrl(), HkjMaterialImage_.url));
            }
            if (criteria.getIsDeleted() != null) {
                specification = specification.and(buildSpecification(criteria.getIsDeleted(), HkjMaterialImage_.isDeleted));
            }
            if (criteria.getCreatedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getCreatedBy(), HkjMaterialImage_.createdBy));
            }
            if (criteria.getCreatedDate() != null) {
                specification = specification.and(buildRangeSpecification(criteria.getCreatedDate(), HkjMaterialImage_.createdDate));
            }
            if (criteria.getLastModifiedBy() != null) {
                specification = specification.and(buildStringSpecification(criteria.getLastModifiedBy(), HkjMaterialImage_.lastModifiedBy));
            }
            if (criteria.getLastModifiedDate() != null) {
                specification = specification.and(
                    buildRangeSpecification(criteria.getLastModifiedDate(), HkjMaterialImage_.lastModifiedDate)
                );
            }
            if (criteria.getMaterialId() != null) {
                specification = specification.and(
                    buildSpecification(criteria.getMaterialId(), root ->
                        root.join(HkjMaterialImage_.material, JoinType.LEFT).get(HkjMaterial_.id)
                    )
                );
            }
        }
        return specification;
    }
}
