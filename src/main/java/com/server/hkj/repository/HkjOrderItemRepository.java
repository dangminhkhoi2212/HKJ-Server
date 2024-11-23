package com.server.hkj.repository;

import com.server.hkj.domain.HkjOrderItem;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjOrderItem entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjOrderItemRepository extends JpaRepository<HkjOrderItem, Long>, JpaSpecificationExecutor<HkjOrderItem> {}
