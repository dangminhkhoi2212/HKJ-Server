package com.server.hkj.repository;

import com.server.hkj.domain.UserExtra;
import java.util.Optional;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserExtra entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserExtraRepository extends JpaRepository<UserExtra, Long> {
    Optional<UserExtra> findOneByUserLogin(String login);
}
