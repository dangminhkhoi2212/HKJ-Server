package com.server.hkj.repository;

import com.server.hkj.service.dto.JewelryImageDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@Slf4j
public class JewelryImageRepository {

    private final EntityManager entityManager;

    public JewelryImageRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public Page<JewelryImageDTO> getJewelryImage(Pageable pageable) {
        // Create the main query to fetch JewelryModel with associated images in JSON format
        Query query = entityManager.createNativeQuery(
            "SELECT j.*, " +
            "(SELECT jsonb_agg(ji) FROM hkj_jewelry_image ji WHERE ji.jewelry_model_id = j.id) AS images " +
            "FROM hkj_jewelry_model j",
            JewelryImageDTO.class
        );

        // Pagination
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());

        // Execute the query and cast the result list
        @SuppressWarnings("unchecked")
        List<JewelryImageDTO> results = (List<JewelryImageDTO>) query.getResultList();
        log.debug(results.toString());
        // Count query for pagination
        Query countQuery = entityManager.createNativeQuery("SELECT COUNT(*) FROM hkj_jewelry_model j");
        long total = ((Number) countQuery.getSingleResult()).longValue();

        return new PageImpl<>(results, pageable, total);
    }
}
