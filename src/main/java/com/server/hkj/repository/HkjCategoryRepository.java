package com.server.hkj.repository;

import com.server.hkj.domain.HkjCategory;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjCategory entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjCategoryRepository extends JpaRepository<HkjCategory, Long>, JpaSpecificationExecutor<HkjCategory> {}
