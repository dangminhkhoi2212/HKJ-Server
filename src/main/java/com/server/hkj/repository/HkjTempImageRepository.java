package com.server.hkj.repository;

import com.server.hkj.domain.HkjTempImage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjTempImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjTempImageRepository extends JpaRepository<HkjTempImage, Long>, JpaSpecificationExecutor<HkjTempImage> {}
