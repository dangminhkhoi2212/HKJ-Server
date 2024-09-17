package com.server.hkj.repository;

import com.server.hkj.domain.HkjJewelryImage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjJewelryImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjJewelryImageRepository extends JpaRepository<HkjJewelryImage, Long>, JpaSpecificationExecutor<HkjJewelryImage> {}
