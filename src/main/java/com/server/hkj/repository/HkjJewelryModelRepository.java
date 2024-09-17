package com.server.hkj.repository;

import com.server.hkj.domain.HkjJewelryModel;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjJewelryModel entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjJewelryModelRepository extends JpaRepository<HkjJewelryModel, Long>, JpaSpecificationExecutor<HkjJewelryModel> {}
