package com.server.hkj.repository;

import com.server.hkj.domain.HkjProject;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjProject entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjProjectRepository extends JpaRepository<HkjProject, Long>, JpaSpecificationExecutor<HkjProject> {}
