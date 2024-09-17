package com.server.hkj.repository;

import com.server.hkj.domain.HkjPosition;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjPosition entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjPositionRepository extends JpaRepository<HkjPosition, Long>, JpaSpecificationExecutor<HkjPosition> {}
