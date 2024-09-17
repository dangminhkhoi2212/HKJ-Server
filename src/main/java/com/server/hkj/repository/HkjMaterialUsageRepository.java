package com.server.hkj.repository;

import com.server.hkj.domain.HkjMaterialUsage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjMaterialUsage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjMaterialUsageRepository extends JpaRepository<HkjMaterialUsage, Long>, JpaSpecificationExecutor<HkjMaterialUsage> {}
