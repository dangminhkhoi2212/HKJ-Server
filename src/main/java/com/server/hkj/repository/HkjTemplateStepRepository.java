package com.server.hkj.repository;

import com.server.hkj.domain.HkjTemplateStep;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjTemplateStep entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjTemplateStepRepository extends JpaRepository<HkjTemplateStep, Long>, JpaSpecificationExecutor<HkjTemplateStep> {}
