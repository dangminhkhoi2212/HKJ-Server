package com.server.hkj.repository;

import com.server.hkj.domain.HkjTask;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjTask entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjTaskRepository extends JpaRepository<HkjTask, Long>, JpaSpecificationExecutor<HkjTask> {}
