package com.server.hkj.repository;

import com.server.hkj.domain.HkjTemplate;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjTemplate entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjTemplateRepository extends JpaRepository<HkjTemplate, Long>, JpaSpecificationExecutor<HkjTemplate> {}
