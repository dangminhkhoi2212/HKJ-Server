package com.server.hkj.repository;

import com.server.hkj.domain.HkjHire;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the HkjHire entity.
 */
@SuppressWarnings("unused")
@Repository
public interface HkjHireRepository extends JpaRepository<HkjHire, Long>, JpaSpecificationExecutor<HkjHire> {}
