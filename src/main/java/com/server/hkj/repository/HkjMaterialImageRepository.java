package com.server.hkj.repository;

import com.server.hkj.domain.HkjMaterialImage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjMaterialImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjMaterialImageRepository extends JpaRepository<HkjMaterialImage, Long>, JpaSpecificationExecutor<HkjMaterialImage> {}
