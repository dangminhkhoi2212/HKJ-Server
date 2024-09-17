package com.server.hkj.repository;

import com.server.hkj.domain.HkjEmployee;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjEmployee entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjEmployeeRepository extends JpaRepository<HkjEmployee, Long>, JpaSpecificationExecutor<HkjEmployee> {}
