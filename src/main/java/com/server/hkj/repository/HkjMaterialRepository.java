package com.server.hkj.repository;

import com.server.hkj.domain.HkjMaterial;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjMaterial entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjMaterialRepository extends JpaRepository<HkjMaterial, Long>, JpaSpecificationExecutor<HkjMaterial> {}
