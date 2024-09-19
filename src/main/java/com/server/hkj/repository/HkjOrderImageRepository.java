package com.server.hkj.repository;

import com.server.hkj.domain.HkjOrderImage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjOrderImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjOrderImageRepository extends JpaRepository<HkjOrderImage, Long>, JpaSpecificationExecutor<HkjOrderImage> {}
