package com.server.hkj.repository;

import com.server.hkj.domain.HkjTrackSearchImage;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjTrackSearchImage entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjTrackSearchImageRepository
    extends JpaRepository<HkjTrackSearchImage, Long>, JpaSpecificationExecutor<HkjTrackSearchImage> {}
