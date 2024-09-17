package com.server.hkj.repository;

import com.server.hkj.domain.HkjTaskImage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjTaskImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjTaskImageRepository extends JpaRepository<HkjTaskImage, Long>, JpaSpecificationExecutor<HkjTaskImage> {}
