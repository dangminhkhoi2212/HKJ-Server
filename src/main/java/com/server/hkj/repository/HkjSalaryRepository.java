package com.server.hkj.repository;

import com.server.hkj.domain.HkjSalary;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjSalary entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjSalaryRepository extends JpaRepository<HkjSalary, Long>, JpaSpecificationExecutor<HkjSalary> {}
