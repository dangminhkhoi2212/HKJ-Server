package com.server.hkj.repository;

import com.server.hkj.domain.HkjOrder;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjOrder entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjOrderRepository extends JpaRepository<HkjOrder, Long>, JpaSpecificationExecutor<HkjOrder> {}
