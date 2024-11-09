package com.server.hkj.repository;

import com.server.hkj.domain.HkjCart;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjCart entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjCartRepository extends JpaRepository<HkjCart, Long>, JpaSpecificationExecutor<HkjCart> {}
